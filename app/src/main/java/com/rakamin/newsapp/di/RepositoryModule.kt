package com.rakamin.newsapp.di

import com.rakamin.newsapp.data.repository.NewsRepositoryImpl
import com.rakamin.newsapp.domain.repository.NewsRepository

object RepositoryModule {

    val newsRepository: NewsRepository by lazy {
        NewsRepositoryImpl(
            newsApiService = NetworkModule.newsApiService
        )
    }
}
