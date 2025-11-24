package com.finanzas.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.finanzas.app.data.repository.TransactionRepository
import com.finanzas.app.ui.screens.*
import com.finanzas.app.ui.theme.Primary

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : Screen(
        route = "home",
        title = "Inicio",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    
    object Transactions : Screen(
        route = "transactions",
        title = "Movimientos",
        selectedIcon = Icons.Filled.SwapHoriz,
        unselectedIcon = Icons.Outlined.SwapHoriz
    )
    
    object Stats : Screen(
        route = "stats",
        title = "Estadísticas",
        selectedIcon = Icons.Filled.PieChart,
        unselectedIcon = Icons.Outlined.PieChart
    )
    
    object Settings : Screen(
        route = "settings",
        title = "Ajustes",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
    
    object AddTransaction : Screen(
        route = "add_transaction?isExpense={isExpense}&transactionId={transactionId}",
        title = "Nueva Transacción",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    ) {
        fun createRoute(isExpense: Boolean = true, transactionId: Long? = null): String {
            return "add_transaction?isExpense=$isExpense&transactionId=${transactionId ?: -1}"
        }
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Transactions,
    Screen.Stats,
    Screen.Settings
)

@Composable
fun MainNavigation(
    repository: TransactionRepository
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }
    
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(
                bottom = if (showBottomBar) paddingValues.calculateBottomPadding() else 0.dp
            )
        ) {
            composable(
                route = Screen.Home.route,
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                val viewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(repository)
                )
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToTransactions = {
                        navController.navigate(Screen.Transactions.route)
                    },
                    onNavigateToAddTransaction = { isExpense ->
                        navController.navigate(Screen.AddTransaction.createRoute(isExpense))
                    },
                    onTransactionClick = { transactionId ->
                        navController.navigate(Screen.AddTransaction.createRoute(transactionId = transactionId))
                    }
                )
            }
            
            composable(
                route = Screen.Transactions.route,
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                val viewModel: TransactionsViewModel = viewModel(
                    factory = TransactionsViewModelFactory(repository)
                )
                TransactionsScreen(
                    viewModel = viewModel,
                    onTransactionClick = { transactionId ->
                        navController.navigate(Screen.AddTransaction.createRoute(transactionId = transactionId))
                    },
                    onAddTransaction = {
                        navController.navigate(Screen.AddTransaction.createRoute())
                    }
                )
            }
            
            composable(
                route = Screen.Stats.route,
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                val viewModel: StatsViewModel = viewModel(
                    factory = StatsViewModelFactory(repository)
                )
                StatsScreen(viewModel = viewModel)
            }
            
            composable(
                route = Screen.Settings.route,
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                SettingsScreen()
            }
            
            composable(
                route = Screen.AddTransaction.route,
                arguments = listOf(
                    navArgument("isExpense") { 
                        type = NavType.BoolType
                        defaultValue = true
                    },
                    navArgument("transactionId") { 
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                ),
                enterTransition = { 
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = { 
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(300)
                    )
                }
            ) { backStackEntry ->
                val isExpense = backStackEntry.arguments?.getBoolean("isExpense") ?: true
                val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: -1L
                
                val viewModel: AddTransactionViewModel = viewModel(
                    factory = AddTransactionViewModelFactory(repository)
                )
                
                AddTransactionScreen(
                    viewModel = viewModel,
                    transactionId = if (transactionId > 0) transactionId else null,
                    isExpense = isExpense,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Primary.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { screen ->
                val isSelected = currentRoute == screen.route
                
                NavBarItem(
                    screen = screen,
                    isSelected = isSelected,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    screen: Screen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedWeight by animateFloatAsState(
        targetValue = if (isSelected) 1.5f else 1f,
        animationSpec = tween(300),
        label = "weight"
    )
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) 
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) 
        else 
            MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                contentDescription = screen.title,
                tint = if (isSelected) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}