package com.rakamin.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rakamin.newsapp.di.RepositoryModule
import com.rakamin.newsapp.domain.usecase.GetNewsUseCase
import com.rakamin.newsapp.domain.usecase.SearchNewsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val searchParams = MutableStateFlow<SearchUiState?>(SearchUiState())

    val searchResults = searchParams
        .flatMapLatest { params ->
            if (params == null || params.query.isBlank()) {
                getNewsUseCase(null)
            } else {
                searchNewsUseCase(
                    query = params.query.trim(),
                    from = params.fromDate,
                    to = params.toDate,
                    sortBy = params.sortBy,
                    sources = params.sources
                        .trim()
                        .takeIf { it.isNotEmpty() },
                    domains = params.domains
                        .trim()
                        .takeIf { it.isNotEmpty() }
                )
            }
        }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
        if (query.isBlank()) {
            searchParams.value = _uiState.value
        }
    }

    fun onFromDateChange(date: String?) {
        _uiState.update {
            it.copy(fromDate = date)
        }
    }

    fun onToDateChange(date: String?) {
        _uiState.update {
            it.copy(toDate = date)
        }
    }

    fun onSortByChange(sortBy: String) {
        _uiState.update {
            it.copy(sortBy = sortBy)
        }
    }

    fun onSourcesChange(sources: String) {
        _uiState.update {
            it.copy(sources = sources)
        }
    }

    fun onDomainsChange(domains: String) {
        _uiState.update {
            it.copy(domains = domains)
        }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                fromDate = null,
                toDate = null,
                sortBy = "publishedAt",
                sources = "",
                domains = ""
            )
        }
    }

    fun searchNews() {
        searchParams.value = _uiState.value
    }

    companion object {
        fun create(): SearchViewModel {
            val repository = RepositoryModule.newsRepository

            return SearchViewModel(
                searchNewsUseCase = SearchNewsUseCase(repository),
                getNewsUseCase = GetNewsUseCase(repository)
            )
        }
    }
}