package vini.lop.io.marvelappstarter.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import vini.lop.io.marvelappstarter.databinding.FragmentSearchCharacterBinding
import vini.lop.io.marvelappstarter.ui.base.BaseFragment

class SearchCharacterFragment :
    BaseFragment<FragmentSearchCharacterBinding, SearchCharacterViewModel>() {

    override val viewModel: SearchCharacterViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchCharacterBinding =
        FragmentSearchCharacterBinding.inflate(
            inflater,
            container,
            false
        )
}