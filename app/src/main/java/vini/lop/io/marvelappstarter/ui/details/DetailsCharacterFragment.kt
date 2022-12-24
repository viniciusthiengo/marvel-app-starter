package vini.lop.io.marvelappstarter.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import vini.lop.io.marvelappstarter.databinding.FragmentDetailsCharacterBinding
import vini.lop.io.marvelappstarter.ui.base.BaseFragment

class DetailsCharacterFragment :
    BaseFragment<FragmentDetailsCharacterBinding, DetailsCharacterViewModel>() {
    override val viewModel: DetailsCharacterViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsCharacterBinding =
        FragmentDetailsCharacterBinding.inflate(
            inflater,
            container,
            false
        )
}