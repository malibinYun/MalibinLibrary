package com.malibin.library.room.converter

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun toDate(milliseconds: Long?): Date? = milliseconds?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time
}
