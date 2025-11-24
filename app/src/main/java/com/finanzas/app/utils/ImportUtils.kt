package com.finanzas.app.utils

import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.RecurringPeriod
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.data.model.TransactionType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ImportUtils {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    
    data class ImportResult(
        val transactions: List<Transaction>,
        val successCount: Int,
        val errorCount: Int,
        val errors: List<String>
    )
    
    /**
     * Parse CSV file content and return list of transactions
     * Expected format: Fecha,Tipo,Categoría,Descripción,Monto,Nota,Recurrente,Periodo
     */
    fun parseCSV(csvContent: String): ImportResult {
        val transactions = mutableListOf<Transaction>()
        val errors = mutableListOf<String>()
        var successCount = 0
        var errorCount = 0
        
        val lines = csvContent.lines()
        if (lines.isEmpty()) {
            return ImportResult(emptyList(), 0, 0, listOf("El archivo está vacío"))
        }
        
        // Skip header if exists
        val dataLines = if (lines[0].contains("Fecha") || lines[0].contains("Date")) {
            lines.drop(1)
        } else {
            lines
        }
        
        dataLines.forEachIndexed { index, line ->
            if (line.trim().isEmpty()) return@forEachIndexed
            
            try {
                val parts = parseCSVLine(line)
                if (parts.size < 5) {
                    errors.add("Línea ${index + 2}: formato inválido (faltan columnas)")
                    errorCount++
                    return@forEachIndexed
                }
                
                // Parse fields
                val dateStr = parts[0].trim()
                val typeStr = parts[1].trim()
                val categoryStr = parts[2].trim()
                val description = parts[3].trim()
                val amountStr = parts[4].trim()
                val note = if (parts.size > 5) parts[5].trim() else ""
                val isRecurringStr = if (parts.size > 6) parts[6].trim() else "false"
                val periodStr = if (parts.size > 7) parts[7].trim() else ""
                
                // Validate and parse
                val date = try {
                    LocalDateTime.parse(dateStr, dateFormatter)
                } catch (e: Exception) {
                    errors.add("Línea ${index + 2}: fecha inválida '$dateStr' (usar formato: yyyy-MM-dd HH:mm)")
                    errorCount++
                    return@forEachIndexed
                }
                
                val type = when (typeStr.uppercase()) {
                    "GASTO", "EXPENSE", "EGRESO" -> TransactionType.EXPENSE
                    "INGRESO", "INCOME" -> TransactionType.INCOME
                    else -> {
                        errors.add("Línea ${index + 2}: tipo inválido '$typeStr' (usar: GASTO o INGRESO)")
                        errorCount++
                        return@forEachIndexed
                    }
                }
                
                val category = findCategory(categoryStr)
                if (category == null) {
                    errors.add("Línea ${index + 2}: categoría desconocida '$categoryStr'")
                    errorCount++
                    return@forEachIndexed
                }
                
                val amount = amountStr.replace(",", ".").toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    errors.add("Línea ${index + 2}: monto inválido '$amountStr'")
                    errorCount++
                    return@forEachIndexed
                }
                
                if (description.isBlank()) {
                    errors.add("Línea ${index + 2}: descripción vacía")
                    errorCount++
                    return@forEachIndexed
                }
                
                val isRecurring = isRecurringStr.lowercase() in listOf("true", "sí", "si", "yes", "1")
                val recurringPeriod = if (isRecurring) {
                    when (periodStr.uppercase()) {
                        "DIARIO", "DAILY" -> RecurringPeriod.DAILY
                        "SEMANAL", "WEEKLY" -> RecurringPeriod.WEEKLY
                        "MENSUAL", "MONTHLY" -> RecurringPeriod.MONTHLY
                        "ANUAL", "YEARLY" -> RecurringPeriod.YEARLY
                        else -> RecurringPeriod.MONTHLY // Default
                    }
                } else null
                
                // Create transaction
                val transaction = Transaction(
                    amount = amount,
                    description = description,
                    category = category,
                    type = type,
                    date = date,
                    note = note,
                    isRecurring = isRecurring,
                    recurringPeriod = recurringPeriod
                )
                
                transactions.add(transaction)
                successCount++
                
            } catch (e: Exception) {
                errors.add("Línea ${index + 2}: error inesperado - ${e.message}")
                errorCount++
            }
        }
        
        return ImportResult(transactions, successCount, errorCount, errors)
    }
    
    /**
     * Parse a CSV line handling quoted fields and commas within quotes
     */
    private fun parseCSVLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        var i = 0
        
        while (i < line.length) {
            val char = line[i]
            
            when {
                char == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        // Escaped quote
                        current.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current = StringBuilder()
                }
                else -> {
                    current.append(char)
                }
            }
            i++
        }
        
        result.add(current.toString())
        return result
    }
    
    /**
     * Find category by name (case insensitive, supports Spanish and English)
     */
    private fun findCategory(name: String): Category? {
        val normalized = name.trim().uppercase()
        
        return Category.entries.find { category ->
            category.displayName.uppercase() == normalized ||
            category.name.uppercase() == normalized ||
            when (category) {
                Category.FOOD -> normalized in listOf("ALIMENTACIÓN", "ALIMENTACION", "COMIDA", "FOOD")
                Category.TRANSPORT -> normalized in listOf("TRANSPORTE", "TRANSPORT", "TRANSPORTATION")
                Category.ENTERTAINMENT -> normalized in listOf("ENTRETENIMIENTO", "ENTERTAINMENT", "OCIO")
                Category.HEALTH -> normalized in listOf("SALUD", "HEALTH", "MEDICINA", "MEDICAL")
                Category.EDUCATION -> normalized in listOf("EDUCACIÓN", "EDUCACION", "EDUCATION")
                Category.SHOPPING -> normalized in listOf("COMPRAS", "SHOPPING")
                Category.BILLS -> normalized in listOf("SERVICIOS", "BILLS", "FACTURAS", "UTILITIES")
                Category.HOME -> normalized in listOf("HOGAR", "HOME", "CASA", "HOUSE")
                Category.SALARY -> normalized in listOf("SALARIO", "SALARY", "SUELDO", "WAGE")
                Category.INVESTMENT -> normalized in listOf("INVERSIONES", "INVESTMENT", "INVERSION")
                Category.GIFT -> normalized in listOf("REGALOS", "GIFT", "REGALO", "GIFTS")
                Category.OTHER -> normalized in listOf("OTROS", "OTHER", "OTRO", "VARIOUS")
                else -> false
            }
        }
    }
    
    /**
     * Generate example CSV content for user reference
     */
    fun generateExampleCSV(): String {
        return """
Fecha,Tipo,Categoría,Descripción,Monto,Nota,Recurrente,Periodo
2024-01-15 10:30,GASTO,Alimentación,Supermercado,5000,Compra semanal,false,
2024-01-16 14:20,GASTO,Transporte,Uber,800,Ida al trabajo,false,
2024-01-20 09:00,INGRESO,Salario,Sueldo mensual,50000,Enero 2024,true,MENSUAL
2024-01-22 18:45,GASTO,Entretenimiento,Cine,1200,Película con amigos,false,
2024-01-25 12:00,GASTO,Servicios,Luz,3500,Factura mensual,true,MENSUAL
        """.trimIndent()
    }
}
