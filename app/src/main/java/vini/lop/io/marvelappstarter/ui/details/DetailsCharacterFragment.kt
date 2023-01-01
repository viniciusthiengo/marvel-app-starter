package vini.lop.io.marvelappstarter.ui.details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import vini.lop.io.marvelappstarter.R
import vini.lop.io.marvelappstarter.data.model.character.CharacterModel
import vini.lop.io.marvelappstarter.databinding.FragmentDetailsCharacterBinding
import vini.lop.io.marvelappstarter.ui.adapters.ComicAdapter
import vini.lop.io.marvelappstarter.ui.base.BaseFragment
import vini.lop.io.marvelappstarter.ui.state.ResourceState
import vini.lop.io.marvelappstarter.util.hide
import vini.lop.io.marvelappstarter.util.preciseSubstring
import vini.lop.io.marvelappstarter.util.show
import vini.lop.io.marvelappstarter.util.toast

@AndroidEntryPoint
class DetailsCharacterFragment :
    BaseFragment<FragmentDetailsCharacterBinding, DetailsCharacterViewModel>() {
    override val viewModel: DetailsCharacterViewModel by viewModels()

    private val characterModel by lazy {
        navArgs<DetailsCharacterFragmentArgs>().value.character
    }
    private val comicAdapter by lazy {
        ComicAdapter()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsCharacterBinding =
        FragmentDetailsCharacterBinding.inflate(
            inflater,
            container,
            false
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupCharacter(characterModel)
        collectObserver()

        viewModel.fetch(characterModel.id)
    }

    private fun setupRecyclerView() = with(binding) {
        rvComics.apply {
            adapter = comicAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupCharacter(characterModel: CharacterModel) = with(binding) {
        tvNameCharacterDetails.text = characterModel.name

        if (characterModel.description.trim().isEmpty()) {
            tvDescriptionCharacterDetails.text = getString(R.string.text_description_empty)
        } else {
            tvDescriptionCharacterDetails.setOnClickListener {
                onShowDialog(characterModel)
            }

            tvDescriptionCharacterDetails.text = characterModel
                .description
                .preciseSubstring(length = 100)
        }

        Glide.with(requireContext())
            .load(characterModel.thumbnailModel.getFullPath())
            .into(imgCharacterDetails)
    }

    private fun onShowDialog(characterModel: CharacterModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(characterModel.name)
            .setMessage(characterModel.description)
            .setNegativeButton(getString(R.string.close_dialog)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.details.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        binding.progressBarDetail.hide()

                        if (values.data.result.isNotEmpty()) {
                            comicAdapter.comics = values.data.result
                        } else {
                            toast(getString(R.string.empty_list_comics))
                        }
                    }
                }
                is ResourceState.Error -> {
                    binding.progressBarDetail.hide()
                    resource.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))

                        Timber
                            .tag(this@DetailsCharacterFragment::class.java.name)
                            .e("Error -> $message")
                    }
                }
                is ResourceState.Loading ->
                    binding.progressBarDetail.show()
                else -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu,
        inflater: MenuInflater
    ) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                viewModel.insert(characterModel)
                toast(getString(R.string.saved_successfully))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}