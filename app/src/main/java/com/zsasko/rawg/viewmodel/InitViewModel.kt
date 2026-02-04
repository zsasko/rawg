package com.zsasko.rawg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsasko.rawg.data.services.ConfigurationService
import com.zsasko.rawg.domain.repository.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InitViewModel @Inject constructor(
    private val configurationService: ConfigurationService,
    genreRepository: GenreRepository
) : ViewModel() {

    val exceptionHandler = CoroutineExceptionHandler { e, t ->
        t.printStackTrace()
    }

    init {
        viewModelScope.launch(exceptionHandler) {
            configurationService.fetchConfiguration()
        }
    }

    val hasSelectedGenres =
        flow {
            val hasGenres = genreRepository.hasSelectedGenres()
            emit(hasGenres)
        }.flowOn(Dispatchers.IO)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

}