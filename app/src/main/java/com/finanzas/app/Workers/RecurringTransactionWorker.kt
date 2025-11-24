package com.finanzas.app.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.finanzas.app.data.FinanzasDatabase
import com.finanzas.app.data.model.RecurringPeriod
import com.finanzas.app.data.model.Transaction
import java.time.LocalDateTime

class RecurringTransactionWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = FinanzasDatabase.getDatabase(applicationContext)
            val transactionDao = database.transactionDao()
            
            // Get all recurring transactions
            val recurringTransactions = transactionDao.getRecurringTransactions()
            
            val now = LocalDateTime.now()
            val today = now.toLocalDate()
            
            recurringTransactions.forEach { transaction ->
                val lastDate = transaction.date.toLocalDate()
                
                val shouldCreate = when (transaction.recurringPeriod) {
                    RecurringPeriod.DAILY -> {
                        lastDate.isBefore(today)
                    }
                    RecurringPeriod.WEEKLY -> {
                        lastDate.plusWeeks(1).isBefore(today) || lastDate.plusWeeks(1).isEqual(today)
                    }
                    RecurringPeriod.MONTHLY -> {
                        lastDate.plusMonths(1).isBefore(today) || lastDate.plusMonths(1).isEqual(today)
                    }
                    RecurringPeriod.YEARLY -> {
                        lastDate.plusYears(1).isBefore(today) || lastDate.plusYears(1).isEqual(today)
                    }
                    null -> false
                }
                
                if (shouldCreate) {
                    // Calculate next date
                    val nextDate = when (transaction.recurringPeriod) {
                        RecurringPeriod.DAILY -> lastDate.plusDays(1)
                        RecurringPeriod.WEEKLY -> lastDate.plusWeeks(1)
                        RecurringPeriod.MONTHLY -> lastDate.plusMonths(1)
                        RecurringPeriod.YEARLY -> lastDate.plusYears(1)
                        null -> today
                    }
                    
                    // Create new transaction with updated date
                    val newTransaction = transaction.copy(
                        id = 0, // Room will auto-generate
                        date = nextDate.atTime(transaction.date.toLocalTime())
                    )
                    
                    transactionDao.insertTransaction(newTransaction)
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
