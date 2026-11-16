
package com.rakamin.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rakamin.newsapp.data.local.dao.BookmarkedNewsDao
import com.rakamin.newsapp.data.local.entity.BookmarkedNewsEntity

@Database(
    entities = [BookmarkedNewsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun bookmarkedNewsDao(): BookmarkedNewsDao
}
