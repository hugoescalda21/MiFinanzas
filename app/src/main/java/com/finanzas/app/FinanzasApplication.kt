package com.finanzas.app

import android.app.Application
import androidx.work.*
import com.finanzas.app.data.FinanzasDatabase
import com.finanzas.app.data.ThemePreferences
import com.finanzas.app.data.repository.AccountRepository
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
    
    val accountRepository: AccountRepository by lazy {
        AccountRepository(database.accountDao())
    }
    
    val themePreferences: ThemePreferences by lazy {
        ThemePreferences(this)
    }
    
    override fun onCreate() {
        super.onCreate()
        setupRecurringTransactionWorker()
        setupDailyReminderWorker()
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
    
    private fun setupDailyReminderWorker() {
        val constraints = Constraints.Builder()
            .build()
        
        val dailyReminderRequest = PeriodicWorkRequestBuilder<com.finanzas.app.workers.DailyReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyReminderRequest
        )
    }
    
    private fun calculateInitialDelay(): Long {
        // Schedule notification for 8 PM (20:00)
        val calendar = java.util.Calendar.getInstance()
        val now = calendar.timeInMillis
        
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 20)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        
        var scheduledTime = calendar.timeInMillis
        
        // If 8 PM has already passed today, schedule for tomorrow
        if (scheduledTime <= now) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
            scheduledTime = calendar.timeInMillis
        }
        
        return scheduledTime - now
    }
}
