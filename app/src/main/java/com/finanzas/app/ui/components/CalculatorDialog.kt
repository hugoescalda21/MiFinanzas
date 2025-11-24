package com.finanzas.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.finanzas.app.ui.theme.*

@Composable
fun CalculatorDialog(
    initialValue: String = "",
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var display by remember { mutableStateOf(initialValue.ifEmpty { "0" }) }
    var operation by remember { mutableStateOf<String?>(null) }
    var previousValue by remember { mutableStateOf<Double?>(null) }
    var shouldResetDisplay by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Calculadora",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (operation != null) {
                            Text(
                                text = "${previousValue ?: ""} $operation",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = display,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 36.sp,
                            maxLines = 1
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Calculator buttons
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Row 1: C, %, ÷
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalcButton(
                            text = "C",
                            modifier = Modifier.weight(1f),
                            color = ExpenseColor,
                            onClick = {
                                display = "0"
                                operation = null
                                previousValue = null
                                shouldResetDisplay = false
                            }
                        )
                        CalcButton(
                            text = "%",
                            modifier = Modifier.weight(1f),
                            color = AccentCyan,
                            onClick = {
                                val value = display.toDoubleOrNull()
                                if (value != null) {
                                    display = (value / 100).toString()
                                }
                            }
                        )
                        CalcButton(
                            text = "÷",
                            modifier = Modifier.weight(1f),
                            color = AccentPurple,
                            onClick = {
                                handleOperation("/", display, operation, previousValue) { newOp, newPrev ->
                                    operation = newOp
                                    previousValue = newPrev
                                    shouldResetDisplay = true
                                }
                            }
                        )
                    }
                    
                    // Row 2: 7, 8, 9, ×
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalcButton("7", Modifier.weight(1f)) { appendNumber("7", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("8", Modifier.weight(1f)) { appendNumber("8", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("9", Modifier.weight(1f)) { appendNumber("9", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("×", Modifier.weight(1f), AccentPurple) {
                            handleOperation("*", display, operation, previousValue) { newOp, newPrev ->
                                operation = newOp
                                previousValue = newPrev
                                shouldResetDisplay = true
                            }
                        }
                    }
                    
                    // Row 3: 4, 5, 6, -
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalcButton("4", Modifier.weight(1f)) { appendNumber("4", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("5", Modifier.weight(1f)) { appendNumber("5", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("6", Modifier.weight(1f)) { appendNumber("6", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("-", Modifier.weight(1f), AccentPurple) {
                            handleOperation("-", display, operation, previousValue) { newOp, newPrev ->
                                operation = newOp
                                previousValue = newPrev
                                shouldResetDisplay = true
                            }
                        }
                    }
                    
                    // Row 4: 1, 2, 3, +
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalcButton("1", Modifier.weight(1f)) { appendNumber("1", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("2", Modifier.weight(1f)) { appendNumber("2", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("3", Modifier.weight(1f)) { appendNumber("3", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton("+", Modifier.weight(1f), AccentPurple) {
                            handleOperation("+", display, operation, previousValue) { newOp, newPrev ->
                                operation = newOp
                                previousValue = newPrev
                                shouldResetDisplay = true
                            }
                        }
                    }
                    
                    // Row 5: 0, ., =
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CalcButton("0", Modifier.weight(2f)) { appendNumber("0", display, shouldResetDisplay) { display = it; shouldResetDisplay = false } }
                        CalcButton(".", Modifier.weight(1f)) {
                            if (!display.contains(".")) {
                                display = if (shouldResetDisplay) "0." else "$display."
                                shouldResetDisplay = false
                            }
                        }
                        CalcButton("=", Modifier.weight(1f), IncomeColor) {
                            if (operation != null && previousValue != null) {
                                val current = display.toDoubleOrNull() ?: 0.0
                                val result = calculate(previousValue, current, operation)
                                display = formatResult(result)
                                operation = null
                                previousValue = null
                                shouldResetDisplay = true
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Confirm button
                Button(
                    onClick = {
                        val value = display.toDoubleOrNull()
                        if (value != null && value > 0) {
                            onConfirm(value)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Usar este valor")
                }
            }
        }
    }
}

@Composable
private fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.15f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

private fun appendNumber(
    number: String,
    currentDisplay: String,
    shouldReset: Boolean,
    onUpdate: (String) -> Unit
) {
    if (shouldReset || currentDisplay == "0") {
        onUpdate(number)
    } else {
        onUpdate(currentDisplay + number)
    }
}

private fun handleOperation(
    newOp: String,
    currentDisplay: String,
    currentOperation: String?,
    currentPrevious: Double?,
    onUpdate: (String?, Double?) -> Unit
) {
    val current = currentDisplay.toDoubleOrNull() ?: 0.0
    
    if (currentOperation != null && currentPrevious != null) {
        // Calculate previous operation first
        val result = calculate(currentPrevious, current, currentOperation)
        onUpdate(newOp, result)
    } else {
        onUpdate(newOp, current)
    }
}

private fun calculate(first: Double?, second: Double, operation: String?): Double {
    if (first == null || operation == null) return second
    
    return when (operation) {
        "+" -> first + second
        "-" -> first - second
        "*" -> first * second
        "/" -> if (second != 0.0) first / second else 0.0
        else -> second
    }
}

private fun formatResult(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format("%.2f", value)
    }
}
