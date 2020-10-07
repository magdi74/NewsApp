package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class ArticlesDatabase: RoomDatabase() {

    companion object{
        var articlesDatabase: ArticlesDatabase? = null

        fun getDatabaseInstance(context: Context): ArticlesDatabase{
            if(articlesDatabase == null)
                articlesDatabase = Room.databaseBuilder(context, ArticlesDatabase::class.java,"ArticlesDatabase")
                                    .fallbackToDestructiveMigration()
                                    .build()
            return articlesDatabase!!
        }
    }
    abstract fun ArticlesDao(): ArticlesDAO
}