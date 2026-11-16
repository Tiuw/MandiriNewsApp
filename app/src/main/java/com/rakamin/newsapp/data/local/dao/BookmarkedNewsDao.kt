
package com.rakamin.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rakamin.newsapp.data.local.entity.BookmarkedNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkedNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(news: BookmarkedNewsEntity)

    @Query("DELETE FROM bookmarked_news WHERE url = :articleUrl")
    suspend fun deleteBookmark(articleUrl: String)

    @Query("SELECT * FROM bookmarked_news ORDER BY publishedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkedNewsEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_news WHERE url = :articleUrl)")
    fun isBookmarked(articleUrl: String): Flow<Boolean>
}
