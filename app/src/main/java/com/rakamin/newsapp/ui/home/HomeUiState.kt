package com.rakamin.newsapp.ui.home

import com.rakamin.newsapp.domain.model.News

data class HomeUiState(
    val isLoading: Boolean = false,
    val headlines: List<News> = emptyList(),
    val error: String? = null
)
