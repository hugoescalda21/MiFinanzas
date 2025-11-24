package com.finanzas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.finanzas.app.data.model.Category
import com.finanzas.app.ui.components.BudgetProgressCard
import com.finanzas.app.ui.components.getCategoryIcon
import com.finanzas.app.ui.theme.*
import com.finanzas.app.utils.formatCurrency
import com.finanzas.app.utils.formatMonthYear
import com.finanzas.app.utils.parseAmount
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddBudgetDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Presupuestos",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedCategory = null
                    showAddBudgetDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Añadir presupuesto"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Month Selector
            item {
                MonthSelector(
                    currentMonth = uiState.currentMonth,
                    onMonthChange = { viewModel.setMonth(it) }
                )
            }
            
            // Total Budget
            item {
                Text(
                    text = "Presupuesto Total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
            
            item {
                val totalBudget = uiState.totalBudget
                if (totalBudget != null) {
                    BudgetProgressCard(
                        spent = totalBudget.spent,
                        budget = totalBudget.budget.amount,
                        category = null,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                } else {
                    EmptyBudgetCard(
                        title = "Sin presupuesto total",
                        description = "Establece un presupuesto mensual para controlar tus gastos totales",
                        onClick = {
                            selectedCategory = null
                            showAddBudgetDialog = true
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }
            
            // Category Budgets
            item {
                Text(
                    text = "Presupuestos por Categoría",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )
            }
            
            items(uiState.categoryBudgets) { categoryInfo ->
                CategoryBudgetItem(
                    categoryInfo = categoryInfo,
                    onEdit = {
                        selectedCategory = categoryInfo.category
                        showAddBudgetDialog = true
                    },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
    
    // Add/Edit Budget Dialog
    if (showAddBudgetDialog) {
        BudgetDialog(
            category = selectedCategory,
            existingBudget = selectedCategory?.let { cat ->
                uiState.categoryBudgets.find { it.category == cat }?.budgetWithSpent
            } ?: uiState.totalBudget,
            onDismiss = { showAddBudgetDialog = false },
            onSave = { amount, threshold ->
                viewModel.saveBudget(amount, selectedCategory, threshold)
                showAddBudgetDialog = false
            },
            onDelete = if (selectedCategory != null || uiState.totalBudget != null) {
                {
                    viewModel.deleteBudget(selectedCategory)
                    showAddBudgetDialog = false
                }
            } else null
        )
    }
}

@Composable
private fun MonthSelector(
    currentMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Mes anterior"
            )
        }
        
        Text(
            text = formatMonthYear(currentMonth),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        
        IconButton(
            onClick = { 
                if (currentMonth < YearMonth.now()) {
                    onMonthChange(currentMonth.plusMonths(1))
                }
            },
            enabled = currentMonth < YearMonth.now()
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Mes siguiente",
                tint = if (currentMonth < YearMonth.now()) 
                    MaterialTheme.colorScheme.onSurface 
                else 
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun CategoryBudgetItem(
    categoryInfo: CategoryBudgetInfo,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = Color(categoryInfo.category.colorHex)
    
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onEdit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(categoryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(categoryInfo.category),
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryInfo.category.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                val budgetWithSpent = categoryInfo.budgetWithSpent
                if (budgetWithSpent != null) {
                    val percentage = budgetWithSpent.percentage
                    val color = when {
                        budgetWithSpent.isExceeded -> ExpenseColor
                        percentage >= 80 -> WarningColor
                        else -> categoryColor
                    }
                    
                    Text(
                        text = "${formatCurrency(categoryInfo.spent)} / ${formatCurrency(budgetWithSpent.budget.amount)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = color
                    )
                } else {
                    Text(
                        text = "Sin presupuesto • ${formatCurrency(categoryInfo.spent)} gastado",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyBudgetCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.AddCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun BudgetDialog(
    category: Category?,
    existingBudget: com.finanzas.app.data.model.BudgetWithSpent?,
    onDismiss: () -> Unit,
    onSave: (Double, Int) -> Unit,
    onDelete: (() -> Unit)?
) {
    var amount by remember { mutableStateOf(existingBudget?.budget?.amount?.toString() ?: "") }
    var threshold by remember { mutableStateOf(existingBudget?.budget?.alertThreshold?.toString() ?: "80") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (category == null) "Presupuesto Total" else category.displayName,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = threshold,
                    onValueChange = { threshold = it },
                    label = { Text("Alerta al (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = { Text("Recibirás una alerta al alcanzar este porcentaje") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedAmount = parseAmount(amount)
                    val parsedThreshold = threshold.toIntOrNull()
                    
                    if (parsedAmount != null && parsedAmount > 0 && 
                        parsedThreshold != null && parsedThreshold in 1..100) {
                        onSave(parsedAmount, parsedThreshold)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Row {
                if (onDelete != null) {
                    TextButton(onClick = onDelete) {
                        Text("Eliminar", color = ExpenseColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}
