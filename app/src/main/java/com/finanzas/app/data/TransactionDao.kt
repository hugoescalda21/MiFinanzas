package com.finanzas.app.data

import androidx.room.*
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): Transaction?
    
    @Query("""
        SELECT * FROM transactions 
        WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now', 'localtime')
        ORDER BY date DESC
    """)
    fun getCurrentMonthTransactions(): Flow<List<Transaction>>
    
    @Query("""
        SELECT * FROM transactions 
        WHERE strftime('%Y-%m', date) = :yearMonth
        ORDER BY date DESC
    """)
    fun getTransactionsByMonth(yearMonth: String): Flow<List<Transaction>>
    
    @Query("""
        SELECT * FROM transactions 
        WHERE type = :type 
        ORDER BY date DESC
    """)
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    
    @Query("""
        SELECT * FROM transactions 
        WHERE category = :category 
        ORDER BY date DESC
    """)
    fun getTransactionsByCategory(category: Category): Flow<List<Transaction>>
    
    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = :type 
        AND strftime('%Y-%m', date) = strftime('%Y-%m', 'now', 'localtime')
    """)
    fun getCurrentMonthTotal(type: TransactionType): Flow<Double>
    
    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = :type 
        AND strftime('%Y-%m', date) = :yearMonth
    """)
    fun getMonthTotal(type: TransactionType, yearMonth: String): Flow<Double>
    
    @Query("""
        SELECT COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END), 0) 
        FROM transactions
    """)
    fun getTotalBalance(): Flow<Double>
    
    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM transactions 
        WHERE type = 'EXPENSE' 
        AND category = :category
        AND strftime('%Y-%m', date) = :yearMonth
    """)
    fun getMonthExpenseByCategory(category: Category, yearMonth: String): Flow<Double>
    
    @Query("""
        SELECT COALESCE(SUM(amount), 0) 
        FROM transactions 
        WHERE type = 'INCOME' 
        AND category = :category
        AND strftime('%Y-%m', date) = :yearMonth
    """)
    fun getMonthIncomeByCategory(category: Category, yearMonth: String): Flow<Double>
    
    @Query("""
        SELECT * FROM transactions 
        ORDER BY date DESC 
        LIMIT :limit
    """)
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>>
    
    @Query("""
        SELECT category, SUM(amount) as total 
        FROM transactions 
        WHERE type = 'EXPENSE' 
        AND strftime('%Y-%m', date) = :yearMonth
        GROUP BY category
        ORDER BY total DESC
    """)
    fun getExpensesByCategory(yearMonth: String): Flow<List<CategorySum>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction): Long
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)
    
    @Query("SELECT * FROM transactions WHERE isRecurring = 1 ORDER BY date DESC")
    suspend fun getRecurringTransactions(): List<Transaction>
}

data class CategorySum(
    val category: Category,
    val total: Double
)
