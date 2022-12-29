package vini.lop.io.marvelappstarter.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import vini.lop.io.marvelappstarter.R
import vini.lop.io.marvelappstarter.databinding.FragmentSearchCharacterBinding
import vini.lop.io.marvelappstarter.ui.adapters.CharacterAdapter
import vini.lop.io.marvelappstarter.ui.base.BaseFragment
import vini.lop.io.marvelappstarter.ui.state.ResourceState
import vini.lop.io.marvelappstarter.util.Constants.DEFAULT_QUERY
import vini.lop.io.marvelappstarter.util.Constants.LAST_SEARCH_QUERY
import vini.lop.io.marvelappstarter.util.hide
import vini.lop.io.marvelappstarter.util.show
import vini.lop.io.marvelappstarter.util.toast

@AndroidEntryPoint
class SearchCharacterFragment :
    BaseFragment<FragmentSearchCharacterBinding, SearchCharacterViewModel>() {

    override val viewModel: SearchCharacterViewModel by viewModels()
    private val characterAdapter by lazy {
        CharacterAdapter()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchCharacterBinding =
        FragmentSearchCharacterBinding.inflate(
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

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        searchInit(query)
    }

    private fun setupRecyclerView() = with(binding) {
        rvSearchCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setClickListener() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = SearchCharacterFragmentDirections
                .actionSearchCharacterFragmentToDetailsCharacterFragment(characterModel)

            findNavController().navigate(action)
        }
    }

    private fun searchInit(query: String) = with(binding) {
        edSearchCharacter.setText(query)

        edSearchCharacter.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateCharacterList()
                true
            } else {
                false
            }
        }

        edSearchCharacter.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                updateCharacterList()
                true
            } else {
                false
            }
        }
    }

    private fun updateCharacterList() = with(binding) {
        val query = edSearchCharacter.editableText.trim()
        if (query.isNotEmpty()) {
            searchQuery(query.toString())
        }
    }

    private fun searchQuery(query: String) {
        viewModel.fetch(query)
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.searchCharacter.collect { resource ->
            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { value ->
                        binding.progressbarSearch.hide()
                        characterAdapter.characters = value.data.results
                    }
                }
                is ResourceState.Error -> {
                    binding.progressbarSearch.hide()
                    resource.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))

                        Timber
                            .tag(this@SearchCharacterFragment::class.java.name)
                            .e("Error -> $message")
                    }
                }
                is ResourceState.Loading ->
                    binding.progressbarSearch.show()
                else -> {}
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(
            LAST_SEARCH_QUERY,
            binding.edSearchCharacter.text.toString().trim()
        )
    }
}