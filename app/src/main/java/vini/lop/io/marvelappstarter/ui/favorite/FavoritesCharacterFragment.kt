package vini.lop.io.marvelappstarter.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import vini.lop.io.marvelappstarter.R
import vini.lop.io.marvelappstarter.databinding.FragmentFavoritesCharacterBinding
import vini.lop.io.marvelappstarter.ui.adapters.CharacterAdapter
import vini.lop.io.marvelappstarter.ui.base.BaseFragment
import vini.lop.io.marvelappstarter.ui.state.ResourceState
import vini.lop.io.marvelappstarter.util.hide
import vini.lop.io.marvelappstarter.util.show
import vini.lop.io.marvelappstarter.util.toast

@AndroidEntryPoint
class FavoritesCharacterFragment :
    BaseFragment<FragmentFavoritesCharacterBinding, FavoritesCharacterViewModel>() {

    override val viewModel: FavoritesCharacterViewModel by viewModels()
    private val characterAdapter by lazy {
        CharacterAdapter()
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesCharacterBinding =
        FragmentFavoritesCharacterBinding.inflate(
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
        setupObserver()
        setupClickListener()

        viewModel.fetch()
    }

    private fun setupRecyclerView() = with(binding) {
        rvFavoriteCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        ItemTouchHelper(itemTouchHelperCallback())
            .attachToRecyclerView(rvFavoriteCharacter)
    }

    private fun setupObserver() = lifecycleScope.launch {
        viewModel.favorites.collect { resource ->

            when (resource) {
                is ResourceState.Success -> {
                    resource.data?.let { values ->
                        binding.tvEmptyList.hide()
                        characterAdapter.characters = values
                    }
                }
                else -> {
                    binding.tvEmptyList.show()
                }
            }
        }
    }

    private fun setupClickListener() {
        characterAdapter.setOnClickListener { character ->
            val action = FavoritesCharacterFragmentDirections
                .actionFavoriteCharacterFragmentToDetailsCharacterFragment(character)

            findNavController().navigate(action)
        }
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val character = characterAdapter.getCharacterPosition(viewHolder.adapterPosition)

                viewModel.delete(character).also {
                    toast(getString(R.string.message_delete_character))
                }
            }
        }
    }
}