package com.rakamin.newsapp.domain.repository

import com.rakamin.newsapp.domain.model.News
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getBookmarkedNews(): Flow<List<News>>

    fun isBookmarked(url: String): Flow<Boolean>

    suspend fun addBookmark(news: News)

    suspend fun removeBookmark(url: String)
}
