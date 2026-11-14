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

    fun getNews(): Flow<PagingData<News>>

    fun searchNews(
        query: String
    ): Flow<PagingData<News>>
}
