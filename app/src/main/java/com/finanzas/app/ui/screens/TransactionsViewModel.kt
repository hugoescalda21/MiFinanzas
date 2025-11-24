package com.finanzas.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.data.model.TransactionType
import com.finanzas.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth

data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.now(),
    val selectedType: TransactionType? = null,
    val selectedCategory: Category? = null,
    val searchQuery: String = "",
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val isLoading: Boolean = true
)

class TransactionsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()
    
    init {
        loadTransactions()
    }
    
    private fun loadTransactions() {
        viewModelScope.launch {
            combine(
                repository.allTransactions,
                repository.currentMonthIncome,
                repository.currentMonthExpense
            ) { transactions, income, expense ->
                val filtered = applyFilters(
                    transactions,
                    _uiState.value.selectedMonth,
                    _uiState.value.selectedType,
                    _uiState.value.selectedCategory,
                    _uiState.value.searchQuery
                )
                
                _uiState.value.copy(
                    transactions = transactions,
                    filteredTransactions = filtered,
                    monthlyIncome = income,
                    monthlyExpense = expense,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    fun setMonth(yearMonth: YearMonth) {
        _uiState.value = _uiState.value.copy(
            selectedMonth = yearMonth,
            filteredTransactions = applyFilters(
                _uiState.value.transactions,
                yearMonth,
                _uiState.value.selectedType,
                _uiState.value.selectedCategory,
                _uiState.value.searchQuery
            )
        )
    }
    
    fun setTypeFilter(type: TransactionType?) {
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            filteredTransactions = applyFilters(
                _uiState.value.transactions,
                _uiState.value.selectedMonth,
                type,
                _uiState.value.selectedCategory,
                _uiState.value.searchQuery
            )
        )
    }
    
    fun setCategoryFilter(category: Category?) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            filteredTransactions = applyFilters(
                _uiState.value.transactions,
                _uiState.value.selectedMonth,
                _uiState.value.selectedType,
                category,
                _uiState.value.searchQuery
            )
        )
    }
    
    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredTransactions = applyFilters(
                _uiState.value.transactions,
                _uiState.value.selectedMonth,
                _uiState.value.selectedType,
                _uiState.value.selectedCategory,
                query
            )
        )
    }
    
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }
    
    private fun applyFilters(
        transactions: List<Transaction>,
        month: YearMonth,
        type: TransactionType?,
        category: Category?,
        query: String
    ): List<Transaction> {
        return transactions.filter { transaction ->
            val matchesMonth = YearMonth.from(transaction.date) == month
            val matchesType = type == null || transaction.type == type
            val matchesCategory = category == null || transaction.category == category
            val matchesQuery = query.isBlank() || 
                transaction.description.contains(query, ignoreCase = true) ||
                transaction.category.displayName.contains(query, ignoreCase = true)
            
            matchesMonth && matchesType && matchesCategory && matchesQuery
        }
    }
}

class TransactionsViewModelFactory(
    private val repository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
