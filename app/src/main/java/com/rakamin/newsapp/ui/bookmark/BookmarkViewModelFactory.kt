
package com.rakamin.newsapp.ui.bookmark

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rakamin.newsapp.di.RepositoryModule
import com.rakamin.newsapp.domain.usecase.bookmark.AddBookmarkUseCase
import com.rakamin.newsapp.domain.usecase.bookmark.GetBookmarkedNewsUseCase
import com.rakamin.newsapp.domain.usecase.bookmark.RemoveBookmarkUseCase

class BookmarkViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {

    private val bookmarkRepository =
        RepositoryModule.provideBookmarkRepository(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookmarkViewModel(
                getBookmarkedNewsUseCase = GetBookmarkedNewsUseCase(bookmarkRepository),
                addBookmarkUseCase = AddBookmarkUseCase(bookmarkRepository),
                removeBookmarkUseCase = RemoveBookmarkUseCase(bookmarkRepository)
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
