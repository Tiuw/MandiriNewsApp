package com.rakamin.newsapp.data.repository

import com.rakamin.newsapp.data.local.dao.BookmarkedNewsDao
import com.rakamin.newsapp.data.local.entity.toDomain
import com.rakamin.newsapp.data.local.entity.toEntity
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepositoryImpl(
    private val bookmarkedNewsDao: BookmarkedNewsDao
) : BookmarkRepository {

    override fun getBookmarkedNews(): Flow<List<News>> {
        return bookmarkedNewsDao.getAllBookmarks().map { bookmarks ->
            bookmarks.map { it.toDomain() }
        }
    }

    override fun isBookmarked(url: String): Flow<Boolean> {
        return bookmarkedNewsDao.isBookmarked(url)
    }

    override suspend fun addBookmark(news: News) {
        val entity = news.toEntity() ?: return
        bookmarkedNewsDao.insertBookmark(entity)
    }

    override suspend fun removeBookmark(url: String) {
        bookmarkedNewsDao.deleteBookmark(url)
    }
}
