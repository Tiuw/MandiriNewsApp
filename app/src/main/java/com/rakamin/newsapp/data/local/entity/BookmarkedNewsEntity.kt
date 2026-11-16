
package com.rakamin.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rakamin.newsapp.domain.model.News

@Entity(tableName = "bookmarked_news")
data class BookmarkedNewsEntity(
    @PrimaryKey
    val url: String,

    val sourceName: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val content: String?
)

fun BookmarkedNewsEntity.toDomain(): News {
    return News(
        sourceName = sourceName,
        author = author,
        title = title,
        description = description,
        url = url,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        content = content
    )
}

fun News.toEntity(): BookmarkedNewsEntity? {
    val articleUrl = url?.takeIf { it.isNotBlank() } ?: return null

    return BookmarkedNewsEntity(
        url = articleUrl,
        sourceName = sourceName,
        author = author,
        title = title,
        description = description,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        content = content
    )
}
