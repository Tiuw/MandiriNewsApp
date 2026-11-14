package com.rakamin.newsapp.data.repository

import com.rakamin.newsapp.BuildConfig
import com.rakamin.newsapp.data.mapper.toDomain
import com.rakamin.newsapp.data.remote.api.NewsApiService
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.CancellationException

class NewsRepositoryImpl(
    private val newsApiService: NewsApiService
) : NewsRepository {

    override suspend fun getTopHeadlines(
        country: String,
        page: Int,
        pageSize: Int
    ): Result<List<News>> {
        return try {
            val response = newsApiService.getTopHeadlines(
                country = country,
                page = page,
                pageSize = pageSize,
                apiKey = BuildConfig.NEWS_API_KEY
            )

            Result.success(
                response.articles.map { it.toDomain() }
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
