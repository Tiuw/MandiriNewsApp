package com.rakamin.newsapp.domain.usecase

import com.rakamin.newsapp.domain.repository.NewsRepository

class GetNewsUseCase(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(category: String? = null) =
        newsRepository.getNews(category)
}