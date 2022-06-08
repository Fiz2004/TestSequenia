package com.fiz.testsequenia.data.data_sources.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGenres(value: String): List<String> {
        return value.split(SEPARATOR)
    }

    @TypeConverter
    fun listGenresToGenres(list: List<String>): String {
        return list.joinToString(SEPARATOR)
    }

    companion object {
        const val SEPARATOR = ";"
    }
}