package com.rakamin.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rakamin.newsapp.di.RepositoryModule
import com.rakamin.newsapp.domain.usecase.SearchNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class SearchViewModel(
    private val searchNewsUseCase: SearchNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    val searchResults = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                kotlinx.coroutines.flow.flowOf(
                    androidx.paging.PagingData.empty()
                )
            } else {
                searchNewsUseCase(query)
            }
        }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    fun searchNews() {
        searchQuery.value = _uiState.value.query.trim()
    }

    companion object {
        fun create(): SearchViewModel {
            val repository = RepositoryModule.newsRepository

            return SearchViewModel(
                searchNewsUseCase = SearchNewsUseCase(repository)
            )
        }
    }
}