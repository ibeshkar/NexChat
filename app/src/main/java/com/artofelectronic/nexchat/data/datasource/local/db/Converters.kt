package com.artofelectronic.nexchat.data.datasource.local.db

import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toList(value: String): List<String> = value.split(",")
}