package com.gusslinaresv.aplicacionusuario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gusslinaresv.aplicacionusuario.ui.theme.AplicacionusuarioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AplicacionusuarioTheme { CatalogApp() } }
    }
}

enum class CatalogSection(val route: String, val title: String, val description: String, val icon: ImageVector) {
    TEXT("texto", "Entrada de texto", "Campos y patrones para capturar información.", Icons.Default.TextFields),
    ACTIONS("acciones", "Botones y acciones", "Controles que activan una respuesta visible.", Icons.Default.TouchApp),
    SELECTION("seleccion", "Elementos de selección", "Opciones, valores y preferencias del usuario.", Icons.Default.Tune),
    LISTS("listas", "Listas y colecciones", "Datos organizados, pestañas y estados de lista.", Icons.AutoMirrored.Filled.List),
    FEEDBACK("retroalimentacion", "Información y retroalimentación", "Mensajes, progreso e información contextual.", Icons.Default.Info),
    LAYOUT("estructura", "Contenedores y estructura", "Patrones para organizar interfaces móviles.", Icons.Default.ViewAgenda)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogApp(viewModel: CatalogViewModel = viewModel()) {
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route ?: "inicio"
    val currentSection = CatalogSection.entries.firstOrNull { it.route == currentRoute }

    Scaffold(
        topBar = { TopAppBar(title = { Text(currentSection?.title ?: "Catálogo UI") }) },
        bottomBar = { CatalogNavigationBar(navController, currentRoute) }
    ) { padding ->
        NavHost(navController, "inicio", Modifier.padding(padding)) {
            composable("inicio") { HomeScreen(navController) }
            CatalogSection.entries.forEach { section ->
                composable(section.route) {
                    when (section) {
                        CatalogSection.TEXT -> TextInputsScreen(viewModel)
                        CatalogSection.ACTIONS -> ActionsScreen()
                        CatalogSection.SELECTION -> SelectionScreen()
                        CatalogSection.LISTS -> CollectionsScreen(viewModel)
                        else -> PlaceholderSection(section)
                    }
                }
            }
        }
    }
}

@Composable
private fun CatalogNavigationBar(navController: NavHostController, currentRoute: String) {
    NavigationBar {
        listOf(CatalogSection.TEXT, CatalogSection.SELECTION, CatalogSection.LISTS, CatalogSection.FEEDBACK).forEach { section ->
            NavigationBarItem(
                selected = currentRoute == section.route,
                onClick = {
                    navController.navigate(section.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(section.icon, section.title) },
                label = { Text(section.title.substringBefore(" ")) }
            )
        }
    }
}

@Composable
private fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Explora componentes móviles", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Cada sección incluye una explicación y controles que puedes probar.")
        CatalogSection.entries.forEach { section ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { navController.navigate(section.route) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(section.icon, null, Modifier.size(24.dp))
                    Text(section.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(section.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun PlaceholderSection(section: CatalogSection) {
    Column(
        modifier = Modifier.fillMaxSize().padding(PaddingValues(24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(section.icon, null, Modifier.size(56.dp))
        Text(section.title, style = MaterialTheme.typography.headlineSmall)
        Text("Esta sección se habilitará en el siguiente bloque de trabajo.")
    }
}
