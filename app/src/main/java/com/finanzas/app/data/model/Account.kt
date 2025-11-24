package com.finanzas.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String, // Icon name from Material Icons
    val colorHex: Long,
    val initialBalance: Double = 0.0,
    val isDefault: Boolean = false
)

// Predefined account types
object AccountType {
    val PERSONAL = Account(
        id = 1,
        name = "Personal",
        icon = "person",
        colorHex = 0xFF3B82F6,
        isDefault = true
    )
    
    val HOUSEHOLD = Account(
        id = 2,
        name = "Hogar",
        icon = "home",
        colorHex = 0xFF10B981
    )
    
    val SAVINGS = Account(
        id = 3,
        name = "Ahorros",
        icon = "savings",
        colorHex = 0xFFF59E0B
    )
    
    val BUSINESS = Account(
        id = 4,
        name = "Negocio",
        icon = "business",
        colorHex = 0xFF8B5CF6
    )
    
    fun getDefaultAccounts(): List<Account> {
        return listOf(PERSONAL, HOUSEHOLD)
    }
}
