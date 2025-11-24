package com.finanzas.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finanzas.app.data.model.Category
import com.finanzas.app.data.model.TransactionType
import com.finanzas.app.ui.components.getCategoryIcon
import com.finanzas.app.ui.theme.*
import com.finanzas.app.utils.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(
    viewModel: TrendsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tendencias",
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Type Selector (Income/Expense)
            TypeSelector(
                selectedType = uiState.selectedType,
                onTypeSelected = { viewModel.setSelectedType(it) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Period Selector
            PeriodSelector(
                selectedPeriod = uiState.periodMonths,
                onPeriodSelected = { viewModel.setPeriodMonths(it) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Line Chart
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.monthlyData.isEmpty()) {
                EmptyTrendsCard()
            } else {
                LineChart(
                    data = uiState.monthlyData,
                    selectedType = uiState.selectedType,
                    selectedCategory = uiState.selectedCategory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(horizontal = 20.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Statistics Cards
                TrendStatistics(
                    data = uiState.monthlyData,
                    selectedType = uiState.selectedType
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Category Filters
                if (uiState.selectedCategory == null) {
                    Text(
                        text = "Filtrar por Categoría",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    CategoryFilterList(
                        type = uiState.selectedType,
                        onCategorySelected = { viewModel.setSelectedCategory(it) }
                    )
                } else {
                    // Show selected category with option to clear
                    val selectedCategory = uiState.selectedCategory
                    if (selectedCategory != null) {
                        SelectedCategoryCard(
                            category = selectedCategory,
                            onClear = { viewModel.setSelectedCategory(null) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TypeChip(
            label = "Gastos",
            icon = Icons.Outlined.TrendingDown,
            color = ExpenseColor,
            isSelected = selectedType == TransactionType.EXPENSE,
            onClick = { onTypeSelected(TransactionType.EXPENSE) },
            modifier = Modifier.weight(1f)
        )
        
        TypeChip(
            label = "Ingresos",
            icon = Icons.Outlined.TrendingUp,
            color = IncomeColor,
            isSelected = selectedType == TransactionType.INCOME,
            onClick = { onTypeSelected(TransactionType.INCOME) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TypeChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, color) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PeriodSelector(
    selectedPeriod: Int,
    onPeriodSelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listOf(3, 6, 12)) { months ->
            FilterChip(
                selected = selectedPeriod == months,
                onClick = { onPeriodSelected(months) },
                label = {
                    Text(
                        text = "$months meses",
                        fontWeight = if (selectedPeriod == months) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            )
        }
    }
}

@Composable
private fun LineChart(
    data: List<MonthlyTrendData>,
    selectedType: TransactionType,
    selectedCategory: Category?,
    modifier: Modifier = Modifier
) {
    val lineColor = when {
        selectedCategory != null -> Color(selectedCategory.colorHex)
        selectedType == TransactionType.EXPENSE -> ExpenseColor
        else -> IncomeColor
    }
    
    val animationProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "chart_animation"
    )
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (data.isEmpty()) return@Canvas
                
                val values = data.map {
                    when {
                        selectedCategory != null -> it.categoryAmount
                        selectedType == TransactionType.EXPENSE -> it.totalExpense
                        else -> it.totalIncome
                    }
                }
                
                val maxValue = values.maxOrNull() ?: 0.0
                if (maxValue == 0.0) return@Canvas
                
                val padding = 50f
                val chartWidth = size.width - padding * 2
                val chartHeight = size.height - padding * 2
                
                val stepX = chartWidth / (data.size - 1).coerceAtLeast(1)
                
                // Draw grid lines
                for (i in 0..4) {
                    val y = padding + (chartHeight * i / 4)
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.1f),
                        start = Offset(padding, y),
                        end = Offset(size.width - padding, y),
                        strokeWidth = 1f
                    )
                    
                    // Y-axis labels
                    val value = maxValue * (1 - i / 4.0)
                    drawContext.canvas.nativeCanvas.drawText(
                        "${(value / 1000).toInt()}k",
                        10f,
                        y + 5f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 24f
                        }
                    )
                }
                
                // Draw line chart
                val path = Path()
                val animatedStepX = stepX * animationProgress
                
                data.forEachIndexed { index, monthData ->
                    val value = values[index]
                    val x = padding + index * animatedStepX
                    val y = padding + chartHeight - (value / maxValue * chartHeight).toFloat()
                    
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                    
                    // Draw points
                    drawCircle(
                        color = lineColor,
                        radius = 6f,
                        center = Offset(x, y)
                    )
                    
                    drawCircle(
                        color = Color.White,
                        radius = 3f,
                        center = Offset(x, y)
                    )
                    
                    // X-axis labels
                    drawContext.canvas.nativeCanvas.drawText(
                        monthData.monthLabel,
                        x - 20f,
                        size.height - 10f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 28f
                        }
                    )
                }
                
                // Draw path
                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 4f)
                )
            }
        }
    }
}

@Composable
private fun TrendStatistics(
    data: List<MonthlyTrendData>,
    selectedType: TransactionType
) {
    val values = data.map {
        if (selectedType == TransactionType.EXPENSE) it.totalExpense else it.totalIncome
    }
    
    val average = if (values.isNotEmpty()) values.average() else 0.0
    val max = values.maxOrNull() ?: 0.0
    val min = values.minOrNull() ?: 0.0
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            label = "Promedio",
            value = formatCurrency(average),
            icon = Icons.Outlined.BarChart,
            color = AccentCyan,
            modifier = Modifier.weight(1f)
        )
        
        StatCard(
            label = "Máximo",
            value = formatCurrency(max),
            icon = Icons.Outlined.TrendingUp,
            color = AccentPurple,
            modifier = Modifier.weight(1f)
        )
        
        StatCard(
            label = "Mínimo",
            value = formatCurrency(min),
            icon = Icons.Outlined.TrendingDown,
            color = AccentCoral,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun CategoryFilterList(
    type: TransactionType,
    onCategorySelected: (Category) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(Category.getByType(type)) { category ->
            CategoryFilterChip(
                category = category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun CategoryFilterChip(
    category: Category,
    onClick: () -> Unit
) {
    val categoryColor = Color(category.colorHex)
    
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = categoryColor.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getCategoryIcon(category),
                contentDescription = null,
                tint = categoryColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.bodySmall,
                color = categoryColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SelectedCategoryCard(
    category: Category,
    onClear: () -> Unit
) {
    val categoryColor = Color(category.colorHex)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = categoryColor.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(categoryColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(category),
                        contentDescription = null,
                        tint = categoryColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = categoryColor
                )
            }
            
            IconButton(onClick = onClear) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Limpiar filtro",
                    tint = categoryColor
                )
            }
        }
    }
}

@Composable
private fun EmptyTrendsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.ShowChart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No hay datos suficientes",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Registra más transacciones para ver tendencias",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}