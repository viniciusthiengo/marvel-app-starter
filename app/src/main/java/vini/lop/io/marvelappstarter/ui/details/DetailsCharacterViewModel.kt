package vini.lop.io.marvelappstarter.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import vini.lop.io.marvelappstarter.data.model.character.CharacterModel
import vini.lop.io.marvelappstarter.data.model.comic.ComicModelResponse
import vini.lop.io.marvelappstarter.repository.MarvelRepository
import vini.lop.io.marvelappstarter.ui.state.ResourceState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
) : ViewModel() {

    private val _details = MutableStateFlow<ResourceState<ComicModelResponse>>(
        ResourceState.Empty()
    )
    val details: StateFlow<ResourceState<ComicModelResponse>> = _details

    fun fetch(characterId: Int) = viewModelScope.launch {
        safeFetch(characterId)
    }

    private suspend fun safeFetch(characterId: Int) {
        _details.value = ResourceState.Loading()

        try {
            val response = repository.getComics(characterId = characterId)
            _details.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException ->
                    _details.value = ResourceState.InternetError()
                else ->
                    _details.value = ResourceState.ParseError()
            }
        }
    }

    private fun handleResponse(response: Response<ComicModelResponse>): ResourceState<ComicModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceState.Success(data = values)
            }
        }

        return ResourceState.Error(message = response.message())
    }

    fun insert(characterModel: CharacterModel) = viewModelScope.launch {
        repository.insert(characterModel)
    }
}
