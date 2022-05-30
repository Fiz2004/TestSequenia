package com.fiz.testsequenia.data.data_sources.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGenres(value: String): List<String> {
        val result = value.split(";")
        return result.toList()
    }

    @TypeConverter
    fun listGenresToGenres(list: List<String>): String {
        var result = ""
        list.forEach { result += "$it;" }
        if (result != "")
            result = result.substring(0, result.length - 1)
        return result
    }
}