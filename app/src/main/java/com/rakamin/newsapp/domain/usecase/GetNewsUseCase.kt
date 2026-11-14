package com.rakamin.newsapp.domain.usecase

import androidx.paging.PagingData
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetNewsUseCase(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<PagingData<News>> {
        return newsRepository.getNews()
    }
}
