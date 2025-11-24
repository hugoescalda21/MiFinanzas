package com.finanzas.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val category: Category,
    val type: TransactionType,
    val date: LocalDateTime = LocalDateTime.now(),
    val note: String = "",
    val isRecurring: Boolean = false,
    val recurringPeriod: RecurringPeriod? = null
)

enum class TransactionType {
    INCOME,
    EXPENSE
}

enum class Category(
    val displayName: String,
    val icon: String,
    val colorHex: Long,
    val type: TransactionType
) {
    // Expense categories
    FOOD("Alimentación", "restaurant", 0xFFF97316, TransactionType.EXPENSE),
    TRANSPORT("Transporte", "directions_car", 0xFF3B82F6, TransactionType.EXPENSE),
    ENTERTAINMENT("Entretenimiento", "movie", 0xFFEC4899, TransactionType.EXPENSE),
    HEALTH("Salud", "medical_services", 0xFFEF4444, TransactionType.EXPENSE),
    EDUCATION("Educación", "school", 0xFF8B5CF6, TransactionType.EXPENSE),
    SHOPPING("Compras", "shopping_bag", 0xFFF59E0B, TransactionType.EXPENSE),
    BILLS("Servicios", "receipt_long", 0xFF64748B, TransactionType.EXPENSE),
    HOME("Hogar", "home", 0xFF06B6D4, TransactionType.EXPENSE),
    
    // Income categories
    SALARY("Salario", "payments", 0xFF10B981, TransactionType.INCOME),
    INVESTMENT("Inversiones", "trending_up", 0xFF14B8A6, TransactionType.INCOME),
    GIFT("Regalos", "card_giftcard", 0xFFEC4899, TransactionType.INCOME),
    
    // General
    OTHER("Otros", "more_horiz", 0xFF6B7280, TransactionType.EXPENSE);
    
    companion object {
        fun getByType(type: TransactionType): List<Category> {
            return entries.filter { it.type == type || it == OTHER }
        }
    }
}

enum class RecurringPeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
}
