package com.finanzas.app

import android.app.Application
import com.finanzas.app.data.FinanzasDatabase
import com.finanzas.app.data.ThemePreferences
import com.finanzas.app.data.repository.TransactionRepository

class FinanzasApplication : Application() {
    
    val database: FinanzasDatabase by lazy { FinanzasDatabase.getDatabase(this) }
    
    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }
    
    val themePreferences: ThemePreferences by lazy {
        ThemePreferences(this)
    }
}
