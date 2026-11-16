
package com.rakamin.newsapp.di

import android.content.Context
import com.rakamin.newsapp.data.repository.BookmarkRepositoryImpl
import com.rakamin.newsapp.data.repository.NewsRepositoryImpl
import com.rakamin.newsapp.domain.repository.BookmarkRepository
import com.rakamin.newsapp.domain.repository.NewsRepository

object RepositoryModule {

    val newsRepository: NewsRepository by lazy {
        NewsRepositoryImpl(
            newsApiService = NetworkModule.newsApiService
        )
    }

    fun provideBookmarkRepository(context: Context): BookmarkRepository {
        val database = DatabaseModule.provideDatabase(context)

        return BookmarkRepositoryImpl(
            bookmarkedNewsDao = database.bookmarkedNewsDao()
        )
    }
}
