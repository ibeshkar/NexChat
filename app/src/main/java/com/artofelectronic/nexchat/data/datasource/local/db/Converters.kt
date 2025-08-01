package com.artofelectronic.nexchat.data.datasource.local.db

import androidx.room.TypeConverter
import com.google.firebase.Timestamp

object Converters {
    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun toList(value: String): List<String> = value.split(",")

    @TypeConverter
    fun fromTimestamp(value: Long?): Timestamp? {
        return value?.let { Timestamp(it, 0) }
    }

    @TypeConverter
    fun toTimestamp(value: Timestamp?): Long? {
        return value?.seconds
    }

    @TypeConverter
    fun fromMap(map: Map<String, Any>?): String {
        return map?.entries?.joinToString(",") { "${it.key}:${it.value}" } ?: ""
    }

    @TypeConverter
    fun toMap(data: String): Map<String, Any> {
        return data.split(",")
            .mapNotNull {
                val parts = it.split(":")
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            .toMap()
    }
}