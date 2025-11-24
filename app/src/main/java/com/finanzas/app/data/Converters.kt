package com.finanzas.app.data

import androidx.room.TypeConverter
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.RecurringPeriod
import com.finanzas.app.data.model.TransactionType
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class Converters {
    
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
    
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(dateTimeFormatter)
    }
    
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }
    
    @TypeConverter
    fun fromYearMonth(value: YearMonth?): String? {
        return value?.format(yearMonthFormatter)
    }
    
    @TypeConverter
    fun toYearMonth(value: String?): YearMonth? {
        return value?.let { YearMonth.parse(it, yearMonthFormatter) }
    }
    
    @TypeConverter
    fun fromCategory(value: Category?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toCategory(value: String?): Category? {
        return value?.let { Category.valueOf(it) }
    }
    
    @TypeConverter
    fun fromTransactionType(value: TransactionType?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toTransactionType(value: String?): TransactionType? {
        return value?.let { TransactionType.valueOf(it) }
    }
    
    @TypeConverter
    fun fromRecurringPeriod(value: RecurringPeriod?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toRecurringPeriod(value: String?): RecurringPeriod? {
        return value?.let { RecurringPeriod.valueOf(it) }
    }
}
