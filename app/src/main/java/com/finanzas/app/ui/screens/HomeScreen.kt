package com.finanzas.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.ui.components.*
import com.finanzas.app.ui.theme.*
import com.finanzas.app.utils.formatDate
import com.finanzas.app.utils.getGreeting
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToTransactions: () -> Unit,
    onNavigateToAddTransaction: (isExpense: Boolean) -> Unit,
    onTransactionClick: (Long) -> Unit,
    onNavigateToBudget: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header with greeting
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp)
            ) {
                Text(
                    text = getGreeting(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Aquí está tu resumen financiero",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Balance Card
        item {
            BalanceCard(
                totalBalance = uiState.totalBalance,
                monthlyIncome = uiState.monthlyIncome,
                monthlyExpense = uiState.monthlyExpense,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp)
            )
        }
        
        // Quick Actions
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp)
            ) {
                Text(
                    text = "Acciones Rápidas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickActionButton(
                        icon = Icons.Filled.Add,
                        label = "Ingreso",
                        backgroundColor = IncomeColor,
                        onClick = { onNavigateToAddTransaction(false) }
                    )
                    
                    QuickActionButton(
                        icon = Icons.Filled.Remove,
                        label = "Gasto",
                        backgroundColor = ExpenseColor,
                        onClick = { onNavigateToAddTransaction(true) }
                    )
                    
                    QuickActionButton(
                        icon = Icons.Outlined.AccountBalanceWallet,
                        label = "Presupuesto",
                        backgroundColor = AccentPurple,
                        onClick = onNavigateToBudget
                    )
                    
                    QuickActionButton(
                        icon = Icons.Outlined.BarChart,
                        label = "Estadísticas",
                        backgroundColor = AccentCyan,
                        onClick = onNavigateToStats
                    )
                }
            }
        }
        
        // Recent Transactions Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Movimientos Recientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                TextButton(onClick = onNavigateToTransactions) {
                    Text(
                        text = "Ver todo",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        // Recent Transactions List or Empty State
        if (uiState.recentTransactions.isEmpty() && !uiState.isLoading) {
            item {
                EmptyStateCard(
                    icon = Icons.Outlined.AccountBalanceWallet,
                    title = "Sin movimientos",
                    description = "Añade tu primer ingreso o gasto para comenzar a llevar el control de tus finanzas",
                    actionLabel = "Añadir transacción",
                    onAction = { onNavigateToAddTransaction(true) },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp)
                )
            }
        } else {
            // Group transactions by date
            val groupedTransactions = uiState.recentTransactions.groupBy { 
                it.date.toLocalDate() 
            }
            
            groupedTransactions.forEach { (date, transactions) ->
                item {
                    Text(
                        text = formatDate(date.atStartOfDay()),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                
                items(
                    items = transactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onClick = { onTransactionClick(transaction.id) },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(vertical = 4.dp)
                            .animateItem()
                    )
                }
            }
        }
        
        // Loading indicator
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
