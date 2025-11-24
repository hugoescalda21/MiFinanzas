package com.finanzas.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.finanzas.app.data.Currency
import com.finanzas.app.data.ThemeMode
import com.finanzas.app.ui.MainNavigation
import com.finanzas.app.ui.theme.FinanzasTheme
import com.finanzas.app.utils.setCurrency

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge
        enableEdgeToEdge()
        
        // Get repository and preferences from application
        val app = application as FinanzasApplication
        val repository = app.transactionRepository
        val themePreferences = app.themePreferences
        
        setContent {
            val themeMode by themePreferences.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
            val currency by themePreferences.currency.collectAsState(initial = Currency.ARS)
            
            // Apply currency change
            LaunchedEffect(currency) {
                setCurrency(currency)
            }
            
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            
            FinanzasTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        repository = repository,
                        themePreferences = themePreferences,
                        currentCurrency = currency
                    )
                }
            }
        }
    }
}
