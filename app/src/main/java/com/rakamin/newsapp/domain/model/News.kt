package com.rakamin.newsapp.domain.model

import java.io.Serializable

data class News(
    val sourceName: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val content: String?
) : Serializable
