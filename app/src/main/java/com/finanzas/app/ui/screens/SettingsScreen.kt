package com.finanzas.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finanzas.app.data.Currency
import com.finanzas.app.data.ThemeMode
import com.finanzas.app.data.ThemePreferences
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.data.repository.TransactionRepository
import com.finanzas.app.ui.theme.*
import com.finanzas.app.utils.ExportUtils
import com.finanzas.app.utils.formatCurrency
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themePreferences: ThemePreferences,
    transactionRepository: TransactionRepository
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val currentTheme by themePreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val currentCurrency by themePreferences.currency.collectAsState(initial = Currency.ARS)
    val notificationsEnabled by themePreferences.notificationsEnabled.collectAsState(initial = true)
    
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var showImportResultDialog by remember { mutableStateOf(false) }
    var importResult by remember { mutableStateOf<com.finanzas.app.utils.ImportUtils.ImportResult?>(null) }
    var isExporting by remember { mutableStateOf(false) }
    var isImporting by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ajustes",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Profile Section
            item {
                ProfileSection(
                    themePreferences = themePreferences,
                    onEditProfile = { showEditProfileDialog = true }
                )
            }
            
            // Preferences Section
            item {
                SectionHeader(title = "Preferencias")
            }
            
            item {
                SettingsCard {
                    SettingsItemClickable(
                        icon = Icons.Outlined.DarkMode,
                        iconBgColor = AccentPurple,
                        title = "Tema",
                        subtitle = when (currentTheme) {
                            ThemeMode.LIGHT -> "Claro"
                            ThemeMode.DARK -> "Oscuro"
                            ThemeMode.SYSTEM -> "Sistema"
                        },
                        onClick = { showThemeDialog = true }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    
                    SettingsItemClickable(
                        icon = Icons.Outlined.Payments,
                        iconBgColor = IncomeColor,
                        title = "Moneda",
                        subtitle = "${currentCurrency.code} - ${currentCurrency.displayName}",
                        onClick = { showCurrencyDialog = true }
                    )
                }
            }
            
            // Notifications Section
            item {
                SectionHeader(title = "Notificaciones")
            }
            
            item {
                SettingsCard {
                    SettingsItemWithSwitch(
                        icon = Icons.Outlined.Notifications,
                        iconBgColor = AccentCoral,
                        title = "Notificaciones",
                        subtitle = "Recibe alertas y recordatorios",
                        isChecked = notificationsEnabled,
                        onCheckedChange = { 
                            scope.launch {
                                themePreferences.setNotificationsEnabled(it)
                            }
                        }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    
                    SettingsItemClickable(
                        icon = Icons.Outlined.Schedule,
                        iconBgColor = AccentCyan,
                        title = "Recordatorio Diario",
                        subtitle = "20:00",
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            // Data Section
            item {
                SectionHeader(title = "Datos")
            }
            
            item {
                SettingsCard {
                    SettingsItemClickable(
                        icon = Icons.Outlined.Upload,
                        iconBgColor = AccentPurple,
                        title = "Exportar Datos",
                        subtitle = "Exportar a archivo CSV",
                        onClick = { showExportDialog = true }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    
                    SettingsItemClickable(
                        icon = Icons.Outlined.Download,
                        iconBgColor = AccentCyan,
                        title = "Importar Datos",
                        subtitle = "Importar desde archivo CSV",
                        onClick = { showImportDialog = true }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    
                    SettingsItemClickable(
                        icon = Icons.Outlined.DeleteForever,
                        iconBgColor = ExpenseColor,
                        title = "Eliminar Todos los Datos",
                        subtitle = "Esta acción no se puede deshacer",
                        onClick = { showDeleteDialog = true },
                        titleColor = ExpenseColor
                    )
                }
            }
            
            // About Section
            item {
                SectionHeader(title = "Acerca de")
            }
            
            item {
                SettingsCard {
                    SettingsItemClickable(
                        icon = Icons.Outlined.Info,
                        iconBgColor = Secondary,
                        title = "Versión",
                        subtitle = "1.0.0",
                        onClick = { }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    
                    SettingsItemClickable(
                        icon = Icons.Outlined.Policy,
                        iconBgColor = Secondary,
                        title = "Política de Privacidad",
                        onClick = { /* TODO */ }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    
                    SettingsItemClickable(
                        icon = Icons.Outlined.Article,
                        iconBgColor = Secondary,
                        title = "Términos de Uso",
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
    
    // Currency Dialog
    if (showCurrencyDialog) {
        AlertDialog(
            onDismissRequest = { showCurrencyDialog = false },
            title = { Text("Seleccionar Moneda") },
            text = {
                Column {
                    Currency.entries.forEach { currency ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch {
                                        themePreferences.setCurrency(currency)
                                    }
                                    showCurrencyDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentCurrency == currency,
                                onClick = {
                                    scope.launch {
                                        themePreferences.setCurrency(currency)
                                    }
                                    showCurrencyDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${currency.symbol} ${currency.code}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = currency.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCurrencyDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Theme Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Seleccionar Tema") },
            text = {
                Column {
                    listOf(
                        ThemeMode.LIGHT to "Claro",
                        ThemeMode.DARK to "Oscuro",
                        ThemeMode.SYSTEM to "Sistema"
                    ).forEach { (mode, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    scope.launch {
                                        themePreferences.setThemeMode(mode)
                                    }
                                    showThemeDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentTheme == mode,
                                onClick = {
                                    scope.launch {
                                        themePreferences.setThemeMode(mode)
                                    }
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Upload,
                    contentDescription = null,
                    tint = AccentPurple
                )
            },
            title = { Text("Exportar Datos") },
            text = { 
                Text("Se exportarán todas tus transacciones a un archivo CSV que podrás guardar o compartir.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        isExporting = true
                        showExportDialog = false
                        scope.launch {
                            val transactions = transactionRepository.allTransactions.first()
                            ExportUtils.exportToCsv(
                                context = context,
                                transactions = transactions,
                                onSuccess = { uri ->
                                    isExporting = false
                                    ExportUtils.shareFile(context, uri, "MiFinanzas.csv")
                                },
                                onError = { error ->
                                    isExporting = false
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                    },
                    enabled = !isExporting
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Exportar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Delete All Data Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.DeleteForever,
                    contentDescription = null,
                    tint = ExpenseColor
                )
            },
            title = { Text("Eliminar Todos los Datos") },
            text = { 
                Text("¿Estás seguro de que deseas eliminar todas las transacciones? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val transactions = transactionRepository.allTransactions.first()
                            transactions.forEach { transaction ->
                                transactionRepository.deleteTransaction(transaction)
                            }
                            showDeleteDialog = false
                            Toast.makeText(context, "Datos eliminados", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ExpenseColor
                    )
                ) {
                    Text("Eliminar Todo")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Import Dialog
    if (showImportDialog) {
        var csvText by remember { mutableStateOf(com.finanzas.app.utils.ImportUtils.generateExampleCSV()) }
        
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    contentDescription = null,
                    tint = AccentCyan
                )
            },
            title = { Text("Importar Datos", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Pega o escribe el contenido CSV a continuación:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = csvText,
                        onValueChange = { csvText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        placeholder = { Text("Fecha,Tipo,Categoría,Descripción,Monto,...") },
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Formato: Fecha,Tipo,Categoría,Descripción,Monto,Nota,Recurrente,Periodo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isImporting = true
                        scope.launch {
                            try {
                                val result = com.finanzas.app.utils.ImportUtils.parseCSV(csvText)
                                
                                // Insert successful transactions into database
                                result.transactions.forEach { transaction ->
                                    transactionRepository.insertTransaction(transaction)
                                }
                                
                                importResult = result
                                showImportDialog = false
                                showImportResultDialog = true
                                
                                if (result.successCount > 0) {
                                    Toast.makeText(
                                        context,
                                        "${result.successCount} transacciones importadas",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Error al importar: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } finally {
                                isImporting = false
                            }
                        }
                    },
                    enabled = !isImporting && csvText.isNotBlank()
                ) {
                    if (isImporting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Importar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Import Result Dialog
    if (showImportResultDialog && importResult != null) {
        AlertDialog(
            onDismissRequest = { showImportResultDialog = false },
            icon = {
                Icon(
                    imageVector = if (importResult!!.errorCount == 0) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                    contentDescription = null,
                    tint = if (importResult!!.errorCount == 0) IncomeColor else WarningColor
                )
            },
            title = { Text("Resultado de Importación", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "✅ Importadas: ${importResult!!.successCount}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = IncomeColor
                    )
                    
                    if (importResult!!.errorCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "❌ Errores: ${importResult!!.errorCount}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ExpenseColor
                        )
                        
                        if (importResult!!.errors.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Primeros errores:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            importResult!!.errors.take(3).forEach { error ->
                                Text(
                                    text = "• $error",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (importResult!!.errors.size > 3) {
                                Text(
                                    text = "... y ${importResult!!.errors.size - 3} más",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { 
                    showImportResultDialog = false
                    importResult = null
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
    
    // Edit Profile Dialog
    if (showEditProfileDialog) {
        val currentName by themePreferences.userName.collectAsState(initial = "Usuario")
        var newName by remember { mutableStateOf(currentName) }
        
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = { Text("Editar Perfil", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Nombre de usuario",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        placeholder = { Text("Tu nombre") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            if (newName.isNotBlank()) {
                                themePreferences.setUserName(newName.trim())
                            }
                            showEditProfileDialog = false
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ProfileSection(
    themePreferences: ThemePreferences,
    onEditProfile: () -> Unit
) {
    val userName by themePreferences.userName.collectAsState(initial = "Usuario")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp)
            .clickable(onClick = onEditProfile),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(GradientStart, GradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Toca para editar perfil",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
private fun SettingsCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(content = content)
    }
}

@Composable
private fun SettingsItemWithSwitch(
    icon: ImageVector,
    iconBgColor: Color,
    title: String,
    subtitle: String? = null,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBgColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconBgColor,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun SettingsItemClickable(
    icon: ImageVector,
    iconBgColor: Color,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    titleColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBgColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconBgColor,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = titleColor
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
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
