package com.finanzas.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finanzas.app.data.model.Budget
import com.finanzas.app.data.model.BudgetWithSpent
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.repository.BudgetRepository
import com.finanzas.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth

data class BudgetUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val totalBudget: BudgetWithSpent? = null,
    val categoryBudgets: List<CategoryBudgetInfo> = emptyList(),
    val totalSpent: Double = 0.0,
    val isLoading: Boolean = true
)

data class CategoryBudgetInfo(
    val category: Category,
    val budgetWithSpent: BudgetWithSpent?,
    val spent: Double
)

class BudgetViewModel(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()
    
    init {
        loadBudgets()
    }
    
    private fun loadBudgets() {
        viewModelScope.launch {
            val month = _uiState.value.currentMonth
            
            combine(
                budgetRepository.getBudgetWithSpent(month, null),
                transactionRepository.getMonthTotal(com.finanzas.app.data.model.TransactionType.EXPENSE, month),
                transactionRepository.getExpensesByCategory(month)
            ) { totalBudget, totalSpent, expensesByCategory ->
                
                // Get all expense categories
                val categoryInfos = Category.entries
                    .filter { it.type == com.finanzas.app.data.model.TransactionType.EXPENSE }
                    .map { category ->
                        val spent = expensesByCategory
                            .find { it.category == category }
                            ?.total ?: 0.0
                        
                        CategoryBudgetInfo(
                            category = category,
                            budgetWithSpent = null, // Will be loaded individually
                            spent = spent
                        )
                    }
                
                BudgetUiState(
                    currentMonth = month,
                    totalBudget = totalBudget,
                    categoryBudgets = categoryInfos,
                    totalSpent = totalSpent,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
                
                // Load individual category budgets
                loadCategoryBudgets(state.currentMonth)
            }
        }
    }
    
    private fun loadCategoryBudgets(month: YearMonth) {
        viewModelScope.launch {
            val updatedCategoryBudgets = _uiState.value.categoryBudgets.map { info ->
                val budgetWithSpent = budgetRepository.getBudgetWithSpent(month, info.category).first()
                info.copy(budgetWithSpent = budgetWithSpent)
            }
            
            _uiState.value = _uiState.value.copy(categoryBudgets = updatedCategoryBudgets)
        }
    }
    
    fun setMonth(yearMonth: YearMonth) {
        _uiState.value = _uiState.value.copy(currentMonth = yearMonth, isLoading = true)
        loadBudgets()
    }
    
    fun saveBudget(amount: Double, category: Category?, alertThreshold: Int = 80) {
        viewModelScope.launch {
            val month = _uiState.value.currentMonth
            
            // Check if budget already exists
            val existingBudget = if (category == null) {
                budgetRepository.getTotalBudgetForMonth(month).first()
            } else {
                budgetRepository.getCategoryBudgetForMonth(month, category).first()
            }
            
            if (existingBudget != null) {
                // Update existing budget
                budgetRepository.updateBudget(
                    existingBudget.copy(
                        amount = amount,
                        alertThreshold = alertThreshold
                    )
                )
            } else {
                // Create new budget
                budgetRepository.insertBudget(
                    Budget(
                        category = category,
                        amount = amount,
                        yearMonth = month,
                        alertThreshold = alertThreshold
                    )
                )
            }
            
            loadBudgets()
        }
    }
    
    fun deleteBudget(category: Category?) {
        viewModelScope.launch {
            val month = _uiState.value.currentMonth
            
            val budget = if (category == null) {
                budgetRepository.getTotalBudgetForMonth(month).first()
            } else {
                budgetRepository.getCategoryBudgetForMonth(month, category).first()
            }
            
            budget?.let {
                budgetRepository.deleteBudget(it)
                loadBudgets()
            }
        }
    }
}

class BudgetViewModelFactory(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(budgetRepository, transactionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
