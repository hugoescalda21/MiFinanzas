package com.finanzas.app.utils

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "AR")).apply {
    maximumFractionDigits = 2
    minimumFractionDigits = 0
}

private val dateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM", Locale("es", "ES"))
private val dateFullFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))

fun formatCurrency(amount: Double): String {
    return currencyFormatter.format(amount)
}

fun formatDate(date: LocalDateTime): String {
    val today = LocalDate.now()
    val transactionDate = date.toLocalDate()
    
    return when {
        transactionDate == today -> "Hoy"
        transactionDate == today.minusDays(1) -> "Ayer"
        transactionDate.isAfter(today.minusDays(7)) -> {
            transactionDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                .replaceFirstChar { it.uppercase() }
        }
        else -> date.format(dateFormatter)
    }
}

fun formatDateFull(date: LocalDateTime): String {
    return date.format(dateFullFormatter).replaceFirstChar { it.uppercase() }
}

fun formatTime(date: LocalDateTime): String {
    return date.format(timeFormatter)
}

fun formatMonthYear(yearMonth: YearMonth): String {
    return yearMonth.format(monthYearFormatter).replaceFirstChar { it.uppercase() }
}

fun formatYearMonth(yearMonth: YearMonth): String {
    return yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))
}

fun getGreeting(): String {
    val hour = LocalDateTime.now().hour
    return when {
        hour < 12 -> "Buenos días"
        hour < 19 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}

fun Double.toPercentageString(): String {
    return "${(this * 100).toInt()}%"
}

fun parseAmount(text: String): Double? {
    return text
        .replace(",", ".")
        .replace("[^0-9.]".toRegex(), "")
        .toDoubleOrNull()
}
