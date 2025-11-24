package com.finanzas.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.TransactionType
import com.finanzas.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class TrendsUiState(
    val monthlyData: List<MonthlyTrendData> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedType: TransactionType = TransactionType.EXPENSE,
    val periodMonths: Int = 6, // Show last 6 months by default
    val isLoading: Boolean = true
)

data class MonthlyTrendData(
    val yearMonth: YearMonth,
    val monthLabel: String,
    val totalExpense: Double,
    val totalIncome: Double,
    val categoryAmount: Double = 0.0
)

class TrendsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TrendsUiState())
    val uiState: StateFlow<TrendsUiState> = _uiState.asStateFlow()
    
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    private val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
    
    init {
        loadTrends()
    }
    
    fun setSelectedCategory(category: Category?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        loadTrends()
    }
    
    fun setSelectedType(type: TransactionType) {
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            selectedCategory = null // Reset category when changing type
        )
        loadTrends()
    }
    
    fun setPeriodMonths(months: Int) {
        _uiState.value = _uiState.value.copy(periodMonths = months)
        loadTrends()
    }
    
    private fun loadTrends() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val currentMonth = YearMonth.now()
            val monthsToShow = _uiState.value.periodMonths
            val selectedCategory = _uiState.value.selectedCategory
            
            val monthlyData = mutableListOf<MonthlyTrendData>()
            
            // Generate data for each month
            for (i in (monthsToShow - 1) downTo 0) {
                val targetMonth = currentMonth.minusMonths(i.toLong())
                val monthString = targetMonth.format(yearMonthFormatter)
                
                // Get total expense and income for the month
                val totalExpense = repository.getMonthTotal(TransactionType.EXPENSE, targetMonth).first()
                val totalIncome = repository.getMonthTotal(TransactionType.INCOME, targetMonth).first()
                
                // Get category-specific amount if a category is selected
                val categoryAmount = if (selectedCategory != null) {
                    if (selectedCategory.type == TransactionType.EXPENSE) {
                        repository.getMonthExpenseByCategory(selectedCategory, targetMonth).first()
                    } else {
                        repository.getMonthIncomeByCategory(selectedCategory, targetMonth).first()
                    }
                } else {
                    0.0
                }
                
                monthlyData.add(
                    MonthlyTrendData(
                        yearMonth = targetMonth,
                        monthLabel = targetMonth.format(monthFormatter).uppercase(),
                        totalExpense = totalExpense,
                        totalIncome = totalIncome,
                        categoryAmount = categoryAmount
                    )
                )
            }
            
            _uiState.value = _uiState.value.copy(
                monthlyData = monthlyData,
                isLoading = false
            )
        }
    }
}

class TrendsViewModelFactory(
    private val repository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrendsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
