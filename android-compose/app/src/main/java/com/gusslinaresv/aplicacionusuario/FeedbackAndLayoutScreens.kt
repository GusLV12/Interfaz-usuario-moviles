package com.gusslinaresv.aplicacionusuario

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    CatalogPage {
        SnackbarHost(snackbarHost)
        CatalogCard("Tipografía", "Los estilos, tamaños y énfasis crean una jerarquía fácil de recorrer.") {
            Text("Título destacado", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Texto de cuerpo para explicar una decisión de interfaz.")
            Text("Nota secundaria", style = MaterialTheme.typography.labelMedium)
        }
        CatalogCard("Imágenes", "Se muestran una imagen del proyecto y otra cargada mediante URL, con escalado distinto.") {
            Image(painterResource(R.mipmap.ic_launcher), "Imagen local", Modifier.size(96.dp).clip(MaterialTheme.shapes.medium), contentScale = ContentScale.Crop)
            AsyncImage("https://images.unsplash.com/photo-1551650975-87deedd944c3?auto=format&fit=crop&w=640&q=80", "Imagen remota", Modifier.fillMaxWidth().height(150.dp).clip(MaterialTheme.shapes.medium), contentScale = ContentScale.Crop)
        }
        CatalogCard("Indicadores de progreso", "Comunican avance conocido o actividad cuya duración no se puede calcular.") {
            LinearProgressIndicator(0.62f, Modifier.fillMaxWidth())
            LinearProgressIndicator(Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { CircularProgressIndicator(progress = { 0.62f }); CircularProgressIndicator() }
        }
        CatalogCard("Mensajes", "Toast y snackbar ofrecen confirmación breve; el snackbar también puede incluir una acción.") {
            Button({ Toast.makeText(context, "Toast mostrado", Toast.LENGTH_SHORT).show() }) { Text("Mostrar toast") }
            OutlinedButton({ scope.launch { snackbarHost.showSnackbar("Elemento guardado", "Deshacer") } }) { Text("Mostrar snackbar") }
        }
        CatalogCard("Diálogo y hoja inferior", "Solicitan confirmación o muestran contenido contextual sin salir de la pantalla.") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button({ showDialog = true }) { Text("Confirmar") }
                OutlinedButton({ showSheet = true }) { Text("Abrir hoja") }
            }
        }
        CatalogCard("Tarjeta, separador y distintivo", "Organizan información y resaltan cantidades pendientes.") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Notificaciones", Modifier.weight(1f))
                BadgedBox(badge = { Badge { Text("3") } }) { Text("Bandeja") }
            }
            HorizontalDivider()
            Text("La tarjeta contiene contenido relacionado en una superficie compartida.")
        }
    }
    if (showDialog) AlertDialog(
        onDismissRequest = { showDialog = false },
        title = { Text("¿Confirmar acción?") },
        text = { Text("Esta demostración muestra un diálogo de confirmación.") },
        confirmButton = { Button({ showDialog = false }) { Text("Aceptar") } },
        dismissButton = { OutlinedButton({ showDialog = false }) { Text("Cancelar") } }
    )
    if (showSheet) ModalBottomSheet(onDismissRequest = { showSheet = false }, sheetState = sheetState) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Hoja inferior", style = MaterialTheme.typography.titleLarge)
            Text("Este contenido aparece desde la parte inferior y se puede descartar.")
            Button({ showSheet = false }) { Text("Cerrar") }
        }
    }
}

@Composable
fun LayoutScreen() {
    CatalogPage {
        Text("La barra superior y la navegación inferior que rodean esta pantalla son parte del Scaffold principal.")
        CatalogCard("Fila", "Organiza componentes de forma horizontal.") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { ColorBlock("A", Color(0xFF006B5C), Modifier.weight(1f)); ColorBlock("B", Color(0xFF9D3A2D), Modifier.weight(1f)); ColorBlock("C", Color(0xFF315E9E), Modifier.weight(1f)) }
        }
        CatalogCard("Columna", "Apila componentes verticalmente en el orden de lectura.") {
            ColorBlock("Primer elemento", MaterialTheme.colorScheme.primary, Modifier.fillMaxWidth())
            ColorBlock("Segundo elemento", MaterialTheme.colorScheme.secondary, Modifier.fillMaxWidth())
        }
        CatalogCard("Superposición", "Box permite ubicar elementos uno encima de otro.") {
            Box(Modifier.fillMaxWidth().height(120.dp).background(MaterialTheme.colorScheme.primaryContainer)) {
                Text("Fondo", Modifier.align(Alignment.Center))
                Badge(Modifier.align(Alignment.TopEnd).padding(12.dp)) { Text("Nuevo") }
            }
        }
        CatalogCard("Pesos proporcionales", "Los pesos distribuyen el ancho disponible entre componentes.") {
            Row(Modifier.fillMaxWidth()) { ColorBlock("1/3", Color(0xFF006B5C), Modifier.weight(1f)); ColorBlock("2/3", Color(0xFF315E9E), Modifier.weight(2f)) }
        }
        CatalogCard("Desplazamiento vertical", "Esta página usa scroll vertical para mantener accesibles todos sus ejemplos.") { Text("Desplázate para volver a los contenedores anteriores.") }
    }
}

@Composable
private fun ColorBlock(text: String, color: Color, modifier: Modifier) {
    Box(modifier.height(44.dp).background(color), contentAlignment = Alignment.Center) { Text(text, color = Color.White) }
}
