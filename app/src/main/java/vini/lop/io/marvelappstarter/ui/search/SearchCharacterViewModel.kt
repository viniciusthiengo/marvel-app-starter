package vini.lop.io.marvelappstarter.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import vini.lop.io.marvelappstarter.data.model.character.CharacterModelResponse
import vini.lop.io.marvelappstarter.repository.MarvelRepository
import vini.lop.io.marvelappstarter.ui.state.ResourceState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
) : ViewModel() {

    private val _searchCharacter = MutableStateFlow<ResourceState<CharacterModelResponse>>(
        ResourceState.Empty()
    )
    val searchCharacter: StateFlow<ResourceState<CharacterModelResponse>> = _searchCharacter

    fun fetch(nameStartsWith: String? = null) = viewModelScope.launch {
        safeFetch(nameStartsWith)
    }

    private suspend fun safeFetch(nameStartsWith: String?) {
        _searchCharacter.value = ResourceState.Loading()

        try {
            val response = repository.list(nameStartsWith)
            _searchCharacter.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException ->
                    _searchCharacter.value = ResourceState.InternetError()
                else ->
                    _searchCharacter.value = ResourceState.ParseError()
            }
        }
    }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResourceState<CharacterModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }

        return ResourceState.Error(response.message())
    }
}
