
package com.rakamin.newsapp.ui.search

data class SearchUiState(
    val query: String = "",
    val fromDate: String? = null,
    val toDate: String? = null,
    val sortBy: String = "publishedAt",
    val sources: String = "",
    val domains: String = ""
)
