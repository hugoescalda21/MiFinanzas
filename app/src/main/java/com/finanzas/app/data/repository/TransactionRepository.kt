package com.finanzas.app.data.repository

import com.finanzas.app.data.CategorySum
import com.finanzas.app.data.TransactionDao
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class TransactionRepository(private val transactionDao: TransactionDao) {
    
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
    
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()
    
    val currentMonthTransactions: Flow<List<Transaction>> = 
        transactionDao.getCurrentMonthTransactions()
    
    val totalBalance: Flow<Double> = transactionDao.getTotalBalance()
    
    val currentMonthIncome: Flow<Double> = 
        transactionDao.getCurrentMonthTotal(TransactionType.INCOME)
    
    val currentMonthExpense: Flow<Double> = 
        transactionDao.getCurrentMonthTotal(TransactionType.EXPENSE)
    
    fun getRecentTransactions(limit: Int = 5): Flow<List<Transaction>> =
        transactionDao.getRecentTransactions(limit)
    
    fun getTransactionsByMonth(yearMonth: YearMonth): Flow<List<Transaction>> =
        transactionDao.getTransactionsByMonth(yearMonth.format(yearMonthFormatter))
    
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        transactionDao.getTransactionsByType(type)
    
    fun getTransactionsByCategory(category: Category): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategory(category)
    
    fun getMonthTotal(type: TransactionType, yearMonth: YearMonth): Flow<Double> =
        transactionDao.getMonthTotal(type, yearMonth.format(yearMonthFormatter))
    
    fun getMonthExpenseByCategory(category: Category, yearMonth: YearMonth): Flow<Double> =
        transactionDao.getMonthExpenseByCategory(category, yearMonth.format(yearMonthFormatter))
    
    fun getExpensesByCategory(yearMonth: YearMonth): Flow<List<CategorySum>> =
        transactionDao.getExpensesByCategory(yearMonth.format(yearMonthFormatter))
    
    suspend fun getTransactionById(id: Long): Transaction? =
        transactionDao.getTransactionById(id)
    
    suspend fun insertTransaction(transaction: Transaction): Long =
        transactionDao.insertTransaction(transaction)
    
    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)
    
    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)
    
    suspend fun deleteTransactionById(id: Long) =
        transactionDao.deleteTransactionById(id)
}
