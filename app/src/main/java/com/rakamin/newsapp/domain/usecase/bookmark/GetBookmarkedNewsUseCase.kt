package com.rakamin.newsapp.domain.usecase.bookmark

import com.rakamin.newsapp.domain.repository.BookmarkRepository

class GetBookmarkedNewsUseCase(
    private val bookmarkRepository: BookmarkRepository
) {
    operator fun invoke() = bookmarkRepository.getBookmarkedNews()
}
