package com.example.newsapp.database

import android.content.Context
import androidx.room.*
import com.example.newsapp.tools.Converter

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ArticlesDatabase: RoomDatabase() {

    companion object{
        var articlesDatabase: ArticlesDatabase? = null

        fun getDatabaseInstance(context: Context): ArticlesDatabase{
            if(articlesDatabase == null)
                articlesDatabase = Room.databaseBuilder(context, ArticlesDatabase::class.java,"ArticlesDatabase")
                                    .fallbackToDestructiveMigration()
                                    .allowMainThreadQueries()
                                    .build()
            return articlesDatabase!!
        }
    }
    abstract fun ArticlesDao(): ArticlesDAO
}