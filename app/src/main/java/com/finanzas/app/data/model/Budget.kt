package com.finanzas.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.YearMonth

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: Category? = null, // null means total budget
    val amount: Double,
    val yearMonth: YearMonth,
    val alertThreshold: Int = 80 // Percentage
)

data class BudgetWithSpent(
    val budget: Budget,
    val spent: Double
) {
    val remaining: Double get() = budget.amount - spent
    val percentage: Int get() = if (budget.amount > 0) ((spent / budget.amount) * 100).toInt() else 0
    val isExceeded: Boolean get() = spent > budget.amount
    val shouldAlert: Boolean get() = percentage >= budget.alertThreshold
}
