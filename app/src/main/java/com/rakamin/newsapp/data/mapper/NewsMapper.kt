package com.rakamin.newsapp.data.mapper

import com.rakamin.newsapp.data.remote.response.ArticleResponse
import com.rakamin.newsapp.domain.model.News

fun ArticleResponse.toDomain(): News {
    return News(
        sourceName = source?.name,
        author = author,
        title = title,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}
