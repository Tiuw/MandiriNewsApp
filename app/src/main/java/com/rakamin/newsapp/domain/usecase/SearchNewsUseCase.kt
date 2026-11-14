package com.rakamin.newsapp.domain.usecase

import com.rakamin.newsapp.domain.repository.NewsRepository

class SearchNewsUseCase(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(query: String) =
        newsRepository.searchNews(query)
}