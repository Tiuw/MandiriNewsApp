
package com.rakamin.newsapp.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.domain.usecase.bookmark.AddBookmarkUseCase
import com.rakamin.newsapp.domain.usecase.bookmark.GetBookmarkedNewsUseCase
import com.rakamin.newsapp.domain.usecase.bookmark.RemoveBookmarkUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val getBookmarkedNewsUseCase: GetBookmarkedNewsUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase
) : ViewModel() {

    val bookmarkedNews = getBookmarkedNewsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addBookmark(news: News) {
        viewModelScope.launch {
            addBookmarkUseCase(news)
        }
    }

    fun removeBookmark(url: String) {
        viewModelScope.launch {
            removeBookmarkUseCase(url)
        }
    }

    fun toggleBookmark(news: News, isBookmarked: Boolean) {
        val url = news.url ?: return

        if (isBookmarked) {
            removeBookmark(url)
        } else {
            addBookmark(news)
        }
    }
}
