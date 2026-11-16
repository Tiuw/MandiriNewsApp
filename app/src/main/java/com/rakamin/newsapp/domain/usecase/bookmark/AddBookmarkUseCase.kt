
package com.rakamin.newsapp.domain.usecase.bookmark

import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.domain.repository.BookmarkRepository

class AddBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(news: News) {
        bookmarkRepository.addBookmark(news)
    }
}
