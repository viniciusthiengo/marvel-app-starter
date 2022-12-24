package vini.lop.io.marvelappstarter.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import vini.lop.io.marvelappstarter.databinding.FragmentFavoriteCharacterBinding
import vini.lop.io.marvelappstarter.ui.base.BaseFragment

class FavoriteCharacterFragment :
    BaseFragment<FragmentFavoriteCharacterBinding, FavoriteCharacterViewModel>() {
    override val viewModel: FavoriteCharacterViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteCharacterBinding =
        FragmentFavoriteCharacterBinding.inflate(
            inflater,
            container,
            false
        )
}