package com.rakamin.newsapp.domain.usecase.bookmark

import com.rakamin.newsapp.domain.repository.BookmarkRepository

class RemoveBookmarkUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    suspend operator fun invoke(url: String) {
        bookmarkRepository.removeBookmark(url)
    }
}
