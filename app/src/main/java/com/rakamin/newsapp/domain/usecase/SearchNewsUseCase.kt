
package com.rakamin.newsapp.domain.usecase

import com.rakamin.newsapp.domain.repository.NewsRepository

class SearchNewsUseCase(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(
        query: String,
        from: String? = null,
        to: String? = null,
        sortBy: String = "publishedAt",
        sources: String? = null,
        domains: String? = null
    ) = newsRepository.searchNews(
        query = query,
        from = from,
        to = to,
        sortBy = sortBy,
        sources = sources,
        domains = domains
    )
}
