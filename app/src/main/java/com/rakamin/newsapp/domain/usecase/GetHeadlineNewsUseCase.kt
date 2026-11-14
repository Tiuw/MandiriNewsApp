package com.rakamin.newsapp.domain.usecase

import com.rakamin.newsapp.domain.repository.NewsRepository

class GetHeadlineNewsUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(
        country: String = "us",
        page: Int = 1,
        pageSize: Int = 20
    ) = newsRepository.getTopHeadlines(
        country = country,
        page = page,
        pageSize = pageSize
    )
}
