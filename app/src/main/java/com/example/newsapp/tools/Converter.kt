package com.example.newsapp.tools

import androidx.room.TypeConverter
import com.example.newsapp.models.Source
import com.google.gson.Gson

class Converter {
    @TypeConverter
    fun toJson(source: Source): String{
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(json: String): Source{
        return Gson().fromJson(json, Source::class.java)
    }
}