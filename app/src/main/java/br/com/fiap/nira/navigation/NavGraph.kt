package br.com.fiap.nira.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Group

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import br.com.fiap.nira.ui.screens.* // importa todas as telas
import br.com.fiap.nira.ui.theme.BottomBarPurple
import br.com.fiap.nira.ui.theme.White

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val MAP = "map"
    const val CONTACTS = "contacts"
    const val FEEDBACK = "feedback"
    const val CHATBOT = "chatbot"
}

private data class BottomItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val bottomItems = listOf(
    BottomItem(Routes.HOME, label = "Início", icon = Icons.Filled.Home),
    BottomItem(Routes.MAP, label = "Mapa", icon = Icons.Filled.Place),
    BottomItem(Routes.CONTACTS, label = "Contatos", icon = Icons.Filled.Group),
    BottomItem(Routes.CHATBOT, label = "Mais", icon = Icons.Filled.Menu)

)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (currentRoute(navController) != Routes.LOGIN) {
                NavigationBar(containerColor = BottomBarPurple) {
                    bottomItems.forEach { item ->
                        val selected = currentRoute(navController) == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.label,
                                    tint = White
                                )
                            },
                            label = { Text(item.label, color = White) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    onEntrar = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onCadastrar = { /* TODO */ },
                    onModoSeguro = { /* TODO */ }
                )
            }
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.MAP) { MapScreen() }
            composable(Routes.CONTACTS) { ContactsScreen() }
            composable(Routes.FEEDBACK) { FeedbackScreen() }
            composable(Routes.CHATBOT) { ChatbotScreen() }
        }
    }
}

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route
}
