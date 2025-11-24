package com.finanzas.app.utils

import com.finanzas.app.data.Currency
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private var currentCurrency: Currency = Currency.ARS

fun setCurrency(currency: Currency) {
    currentCurrency = currency
}

fun getCurrency(): Currency = currentCurrency

private fun getCurrencyFormatter(): NumberFormat {
    val locale = when (currentCurrency) {
        Currency.ARS -> Locale("es", "AR")
        Currency.USD -> Locale("en", "US")
        Currency.EUR -> Locale("es", "ES")
        Currency.MXN -> Locale("es", "MX")
        Currency.CLP -> Locale("es", "CL")
        Currency.COP -> Locale("es", "CO")
    }
    return NumberFormat.getCurrencyInstance(locale).apply {
        maximumFractionDigits = if (currentCurrency == Currency.CLP) 0 else 2
        minimumFractionDigits = 0
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("d 'de' MMMM", Locale("es", "ES"))
private val dateFullFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))

fun formatCurrency(amount: Double): String {
    return getCurrencyFormatter().format(amount)
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
