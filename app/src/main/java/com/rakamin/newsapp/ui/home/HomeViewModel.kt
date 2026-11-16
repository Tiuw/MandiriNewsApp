
package com.rakamin.newsapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rakamin.newsapp.di.RepositoryModule
import com.rakamin.newsapp.domain.usecase.GetHeadlineNewsUseCase
import com.rakamin.newsapp.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val getHeadlineNewsUseCase: GetHeadlineNewsUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    val newsPagingFlow = _selectedCategory
        .flatMapLatest { category ->
            getNewsUseCase(category)
        }
        .cachedIn(viewModelScope)

    init {
        getHeadlineNews()
    }

    fun onCategorySelected(category: String?) {
        _selectedCategory.value = category
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
            val repository = RepositoryModule.newsRepository

            return HomeViewModel(
                getHeadlineNewsUseCase = GetHeadlineNewsUseCase(repository),
                getNewsUseCase = GetNewsUseCase(repository)
            )
        }
    }
}
