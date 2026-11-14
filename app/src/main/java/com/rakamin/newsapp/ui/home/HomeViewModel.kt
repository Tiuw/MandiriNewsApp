package com.rakamin.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rakamin.newsapp.di.RepositoryModule
import com.rakamin.newsapp.domain.usecase.GetHeadlineNewsUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getHeadlineNewsUseCase: GetHeadlineNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getHeadlineNews()
    }

    fun getHeadlineNews() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }

            try {
                getHeadlineNewsUseCase().fold(
                    onSuccess = { news ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                headlines = news,
                                error = null
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Failed to load news"
                            )
                        }
                    }
                )
            } catch (exception: CancellationException) {
                throw exception
            }
        }
    }

    companion object {
        fun create(): HomeViewModel {
            return HomeViewModel(
                getHeadlineNewsUseCase = GetHeadlineNewsUseCase(
                    RepositoryModule.newsRepository
                )
            )
        }
    }
}
