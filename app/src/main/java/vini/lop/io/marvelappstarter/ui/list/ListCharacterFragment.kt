package vini.lop.io.marvelappstarter.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import vini.lop.io.marvelappstarter.R
import vini.lop.io.marvelappstarter.databinding.FragmentListCharacterBinding
import vini.lop.io.marvelappstarter.ui.adapters.CharacterAdapter
import vini.lop.io.marvelappstarter.ui.base.BaseFragment
import vini.lop.io.marvelappstarter.ui.state.ResourceState
import vini.lop.io.marvelappstarter.util.hide
import vini.lop.io.marvelappstarter.util.show
import vini.lop.io.marvelappstarter.util.toast

@AndroidEntryPoint
class ListCharacterFragment : BaseFragment<FragmentListCharacterBinding, ListCharacterViewModel>() {

    override val viewModel: ListCharacterViewModel by viewModels()
    private val characterAdapter: CharacterAdapter by lazy {
        CharacterAdapter()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListCharacterBinding =
        FragmentListCharacterBinding.inflate(
            inflater,
            container,
            false
        )

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setClickListener()
        collectObserver()
    }

    private fun setupRecyclerView() = with(binding) {
        rvCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setClickListener() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = ListCharacterFragmentDirections
                .actionListCharacterFragmentToDetailsCharacterFragment(characterModel)

            findNavController().navigate(action)
        }
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.list.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        binding.progressCircular.hide()
                        characterAdapter.characters = values.data.results
                    }
                }
                is ResourceState.Error -> {
                    binding.progressCircular.hide()
                    resource.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))

                        Timber
                            .tag(this@ListCharacterFragment::class.java.name)
                            .e("Error -> $message")
                    }
                }
                is ResourceState.Loading ->
                    binding.progressCircular.show()
                else -> {}
            }
        }
    }
}