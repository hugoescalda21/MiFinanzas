package com.finanzas.app.data

import androidx.room.*
import com.finanzas.app.data.model.Budget
import com.finanzas.app.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    
    @Query("SELECT * FROM budgets ORDER BY yearMonth DESC")
    fun getAllBudgets(): Flow<List<Budget>>
    
    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): Budget?
    
    @Query("""
        SELECT * FROM budgets 
        WHERE yearMonth = :yearMonth 
        AND category IS NULL
    """)
    fun getTotalBudgetForMonth(yearMonth: String): Flow<Budget?>
    
    @Query("""
        SELECT * FROM budgets 
        WHERE yearMonth = :yearMonth 
        AND category = :category
    """)
    fun getCategoryBudgetForMonth(yearMonth: String, category: Category): Flow<Budget?>
    
    @Query("""
        SELECT * FROM budgets 
        WHERE yearMonth = :yearMonth
    """)
    fun getBudgetsForMonth(yearMonth: String): Flow<List<Budget>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget): Long
    
    @Update
    suspend fun updateBudget(budget: Budget)
    
    @Delete
    suspend fun deleteBudget(budget: Budget)
}
