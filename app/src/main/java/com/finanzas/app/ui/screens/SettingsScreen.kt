package com.finanzas.app.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finanzas.app.data.Currency
import com.finanzas.app.data.ThemeMode
import com.finanzas.app.data.ThemePreferences
import com.finanzas.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themePreferences: ThemePreferences
) {
    val scope = rememberCoroutineScope()
    val currentTheme by themePreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val currentCurrency by themePreferences.currency.collectAsState(initial = Currency.ARS)
    
    var notificationsEnabled by remember { mutableStateOf(true) }
    var showCurrencyDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    
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
                ProfileSection()
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
                        onCheckedChange = { notificationsEnabled = it }
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
                        subtitle = "Exportar a CSV o Excel",
                        onClick = { /* TODO */ }
                    )
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    
                    SettingsItemClickable(
                        icon = Icons.Outlined.Download,
                        iconBgColor = AccentCyan,
                        title = "Importar Datos",
                        subtitle = "Importar desde archivo",
                        onClick = { /* TODO */ }
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
                        onClick = { /* TODO */ },
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
}

@Composable
private fun ProfileSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp),
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
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Usuario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Gestiona tu perfil",
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
