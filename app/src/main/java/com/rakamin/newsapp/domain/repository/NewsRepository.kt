
package com.rakamin.newsapp.domain.repository

import androidx.paging.PagingData
import com.rakamin.newsapp.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getTopHeadlines(
        country: String = "us",
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<News>>

    fun getNews(
        category: String? = null
    ): Flow<PagingData<News>>

    fun searchNews(
        query: String,
        from: String? = null,
        to: String? = null,
        sortBy: String = "publishedAt",
        sources: String? = null,
        domains: String? = null
    ): Flow<PagingData<News>>
}
