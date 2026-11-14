package com.rakamin.newsapp.domain.repository

import com.rakamin.newsapp.domain.model.News

interface NewsRepository {

    suspend fun getTopHeadlines(
        country: String = "us",
        page: Int = 1,
        pageSize: Int = 20
    ): Result<List<News>>
}
