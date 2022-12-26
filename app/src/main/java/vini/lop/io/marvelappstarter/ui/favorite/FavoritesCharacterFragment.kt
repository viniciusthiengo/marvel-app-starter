package vini.lop.io.marvelappstarter.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import vini.lop.io.marvelappstarter.databinding.FragmentFavoritesCharacterBinding
import vini.lop.io.marvelappstarter.ui.base.BaseFragment

@AndroidEntryPoint
class FavoritesCharacterFragment :
    BaseFragment<FragmentFavoritesCharacterBinding, FavoritesCharacterViewModel>() {
    override val viewModel: FavoritesCharacterViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesCharacterBinding =
        FragmentFavoritesCharacterBinding.inflate(
            inflater,
            container,
            false
        )
}