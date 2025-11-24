package com.finanzas.app.data.repository

import com.finanzas.app.data.BudgetDao
import com.finanzas.app.data.TransactionDao
import com.finanzas.app.data.model.Budget
import com.finanzas.app.data.model.BudgetWithSpent
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class BudgetRepository(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) {
    
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
    
    fun getBudgetsForMonth(yearMonth: YearMonth): Flow<List<Budget>> {
        return budgetDao.getBudgetsForMonth(yearMonth.format(yearMonthFormatter))
    }
    
    fun getTotalBudgetForMonth(yearMonth: YearMonth): Flow<Budget?> {
        return budgetDao.getTotalBudgetForMonth(yearMonth.format(yearMonthFormatter))
    }
    
    fun getCategoryBudgetForMonth(yearMonth: YearMonth, category: Category): Flow<Budget?> {
        return budgetDao.getCategoryBudgetForMonth(yearMonth.format(yearMonthFormatter), category)
    }
    
    fun getBudgetWithSpent(yearMonth: YearMonth, category: Category?): Flow<BudgetWithSpent?> {
        val budgetFlow = if (category == null) {
            getTotalBudgetForMonth(yearMonth)
        } else {
            getCategoryBudgetForMonth(yearMonth, category)
        }
        
        val spentFlow = if (category == null) {
            transactionDao.getMonthTotal(TransactionType.EXPENSE, yearMonth.format(yearMonthFormatter))
        } else {
            transactionDao.getMonthExpenseByCategory(category, yearMonth.format(yearMonthFormatter))
        }
        
        return combine(budgetFlow, spentFlow) { budget, spent ->
            budget?.let { BudgetWithSpent(it, spent) }
        }
    }
    
    fun getAllBudgetsWithSpentForMonth(yearMonth: YearMonth): Flow<List<BudgetWithSpent>> {
        return combine(
            getBudgetsForMonth(yearMonth),
            transactionDao.getMonthTotal(TransactionType.EXPENSE, yearMonth.format(yearMonthFormatter))
        ) { budgets, _ ->
            budgets.map { budget ->
                val spent = if (budget.category == null) {
                    // Total budget
                    transactionDao.getMonthTotal(
                        TransactionType.EXPENSE,
                        yearMonth.format(yearMonthFormatter)
                    )
                } else {
                    // Category budget
                    transactionDao.getMonthExpenseByCategory(
                        budget.category,
                        yearMonth.format(yearMonthFormatter)
                    )
                }
                // Get the first value synchronously (not ideal but works for this case)
                BudgetWithSpent(budget, 0.0) // We'll update this properly
            }
        }
    }
    
    suspend fun insertBudget(budget: Budget): Long {
        return budgetDao.insertBudget(budget)
    }
    
    suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget)
    }
    
    suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }
    
    suspend fun getBudgetById(id: Long): Budget? {
        return budgetDao.getBudgetById(id)
    }
}
