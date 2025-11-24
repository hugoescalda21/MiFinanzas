package com.finanzas.app

import android.app.Application
import androidx.work.*
import com.finanzas.app.data.FinanzasDatabase
import com.finanzas.app.data.ThemePreferences
import com.finanzas.app.data.repository.BudgetRepository
import com.finanzas.app.data.repository.TransactionRepository
import com.finanzas.app.workers.RecurringTransactionWorker
import java.util.concurrent.TimeUnit

class FinanzasApplication : Application() {
    
    val database: FinanzasDatabase by lazy { FinanzasDatabase.getDatabase(this) }
    
    val transactionRepository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }
    
    val budgetRepository: BudgetRepository by lazy {
        BudgetRepository(database.budgetDao(), database.transactionDao())
    }
    
    val themePreferences: ThemePreferences by lazy {
        ThemePreferences(this)
    }
    
    override fun onCreate() {
        super.onCreate()
        setupRecurringTransactionWorker()
    }
    
    private fun setupRecurringTransactionWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()
        
        val recurringWorkRequest = PeriodicWorkRequestBuilder<RecurringTransactionWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "recurring_transactions",
            ExistingPeriodicWorkPolicy.KEEP,
            recurringWorkRequest
        )
    }
}
