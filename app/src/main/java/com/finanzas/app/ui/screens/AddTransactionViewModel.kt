package com.finanzas.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.RecurringPeriod
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.data.model.TransactionType
import com.finanzas.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

data class AddTransactionUiState(
    val amount: String = "",
    val description: String = "",
    val category: Category = Category.OTHER,
    val type: TransactionType = TransactionType.EXPENSE,
    val date: LocalDateTime = LocalDateTime.now(),
    val note: String = "",
    val isRecurring: Boolean = false,
    val recurringPeriod: RecurringPeriod? = null,
    val accountId: Long = 1, // Default to Personal account
    val isEditing: Boolean = false,
    val transactionId: Long? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class AddTransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()
    
    fun loadTransaction(transactionId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            repository.getTransactionById(transactionId)?.let { transaction ->
                _uiState.value = _uiState.value.copy(
                    amount = transaction.amount.toString(),
                    description = transaction.description,
                    category = transaction.category,
                    type = transaction.type,
                    date = transaction.date,
                    note = transaction.note,
                    isRecurring = transaction.isRecurring,
                    recurringPeriod = transaction.recurringPeriod,
                    accountId = transaction.accountId,
                    isEditing = true,
                    transactionId = transaction.id,
                    isLoading = false
                )
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Transacción no encontrada"
                )
            }
        }
    }
    
    fun setInitialType(isExpense: Boolean) {
        if (!_uiState.value.isEditing) {
            _uiState.value = _uiState.value.copy(
                type = if (isExpense) TransactionType.EXPENSE else TransactionType.INCOME,
                category = if (isExpense) Category.OTHER else Category.SALARY
            )
        }
    }
    
    fun updateAmount(amount: String) {
        // Allow only valid decimal input
        val filtered = amount.filter { it.isDigit() || it == '.' || it == ',' }
            .replace(',', '.')
        
        // Ensure only one decimal point
        val parts = filtered.split('.')
        val cleaned = if (parts.size > 2) {
            parts[0] + "." + parts.drop(1).joinToString("")
        } else {
            filtered
        }
        
        _uiState.value = _uiState.value.copy(amount = cleaned)
    }
    
    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }
    
    fun updateCategory(category: Category) {
        _uiState.value = _uiState.value.copy(category = category)
    }
    
    fun updateType(type: TransactionType) {
        val newCategory = if (type != _uiState.value.type) {
            Category.getByType(type).firstOrNull() ?: Category.OTHER
        } else {
            _uiState.value.category
        }
        
        _uiState.value = _uiState.value.copy(
            type = type,
            category = newCategory
        )
    }
    
    fun updateDate(date: LocalDateTime) {
        _uiState.value = _uiState.value.copy(date = date)
    }
    
    fun updateNote(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }
    
    fun updateIsRecurring(isRecurring: Boolean) {
        _uiState.value = _uiState.value.copy(
            isRecurring = isRecurring,
            recurringPeriod = if (isRecurring && _uiState.value.recurringPeriod == null) {
                RecurringPeriod.MONTHLY
            } else if (!isRecurring) {
                null
            } else {
                _uiState.value.recurringPeriod
            }
        )
    }
    
    fun updateRecurringPeriod(period: RecurringPeriod) {
        _uiState.value = _uiState.value.copy(recurringPeriod = period)
    }
    
    fun saveTransaction() {
        val state = _uiState.value
        
        // Validation
        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.value = state.copy(error = "Ingresa una cantidad válida")
            return
        }
        
        if (state.description.isBlank()) {
            _uiState.value = state.copy(error = "Ingresa una descripción")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, error = null)
            
            try {
                val transaction = Transaction(
                    id = state.transactionId ?: 0,
                    amount = amount,
                    description = state.description.trim(),
                    category = state.category,
                    type = state.type,
                    date = state.date,
                    note = state.note.trim(),
                    isRecurring = state.isRecurring,
                    recurringPeriod = if (state.isRecurring) state.recurringPeriod else null,
                    accountId = state.accountId
                )
                
                if (state.isEditing && state.transactionId != null) {
                    repository.updateTransaction(transaction)
                } else {
                    repository.insertTransaction(transaction)
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al guardar: ${e.message}"
                )
            }
        }
    }
    
    fun deleteTransaction() {
        val transactionId = _uiState.value.transactionId ?: return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                repository.deleteTransactionById(transactionId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al eliminar: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

class AddTransactionViewModelFactory(
    private val repository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddTransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
