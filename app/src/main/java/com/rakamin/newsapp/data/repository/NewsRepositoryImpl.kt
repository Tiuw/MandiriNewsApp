
package com.rakamin.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rakamin.newsapp.BuildConfig
import com.rakamin.newsapp.data.mapper.toDomain
import com.rakamin.newsapp.data.remote.api.NewsApiService
import com.rakamin.newsapp.data.remote.paging.NewsPagingSource
import com.rakamin.newsapp.data.remote.paging.SearchNewsPagingSource
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

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

    override fun getNews(): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsApiService)
            }
        ).flow.map { pagingData ->
            pagingData.map { article ->
                article.toDomain()
            }
        }
    }

    override fun searchNews(
        query: String
    ): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    newsApiService = newsApiService,
                    query = query.trim()
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { article ->
                article.toDomain()
            }
        }
    }
}
