
package com.rakamin.newsapp.di

import android.content.Context
import androidx.room.Room
import com.rakamin.newsapp.data.local.NewsDatabase

object DatabaseModule {

    @Volatile
    private var INSTANCE: NewsDatabase? = null

    fun provideDatabase(context: Context): NewsDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                NewsDatabase::class.java,
                "news_database"
            ).build().also { INSTANCE = it }
        }
    }
}
