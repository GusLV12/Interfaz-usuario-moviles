package com.gusslinaresv.aplicacionusuario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CatalogPage(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
fun CatalogCard(title: String, description: String, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodyMedium)
                content()
            }
        )
    }
}

@Composable
fun TextInputsScreen(viewModel: CatalogViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var simpleText by remember { mutableStateOf("") }
    var validatedText by remember { mutableStateOf("") }
    var validationTouched by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var number by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var selectedSuggestion by remember { mutableStateOf("Selecciona una ciudad") }
    var search by remember { mutableStateOf("") }
    val suggestions = listOf("Ciudad de México", "Guadalajara", "Monterrey")

    CatalogPage {
        Text("Prueba distintos tipos de captura y observa cómo cambia el teclado y la validación.")
        CatalogCard("Campo simple", "Captura texto de una sola línea con una etiqueta descriptiva.") {
            OutlinedTextField(simpleText, { simpleText = it }, Modifier.fillMaxWidth(), label = { Text("Nombre") })
        }
        CatalogCard("Campo con validación", "Muestra un error visible cuando el contenido no cumple la regla solicitada.") {
            val invalid = validationTouched && validatedText.length < 3
            OutlinedTextField(
                value = validatedText,
                onValueChange = { validatedText = it; validationTouched = true },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Usuario (mínimo 3 caracteres)") },
                isError = invalid,
                supportingText = { if (invalid) Text("Escribe al menos 3 caracteres.") }
            )
        }
        CatalogCard("Contraseña", "El icono permite alternar entre ocultar y mostrar el contenido.") {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, "Mostrar u ocultar contraseña")
                    }
                }
            )
        }
        CatalogCard("Tipos de teclado", "El tipo de dato orienta al teclado que se presenta en el dispositivo.") {
            OutlinedTextField(number, { number = it }, Modifier.fillMaxWidth(), label = { Text("Número") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Correo electrónico") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            OutlinedTextField(phone, { phone = it }, Modifier.fillMaxWidth(), label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
        }
        CatalogCard("Campo multilínea", "Útil para comentarios, notas o descripciones extensas.") {
            OutlinedTextField(notes, { notes = it }, Modifier.fillMaxWidth(), label = { Text("Comentarios") }, minLines = 3)
        }
        CatalogCard("Sugerencias", "Un conjunto de opciones facilita elegir valores frecuentes.") {
            Text(selectedSuggestion)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                suggestions.forEach { suggestion ->
                    FilterChip(selectedSuggestion == suggestion, { selectedSuggestion = suggestion }, label = { Text(suggestion) })
                }
            }
        }
        CatalogCard("Barra de búsqueda", "Filtra opciones conforme se escribe una consulta.") {
            OutlinedTextField(
                search,
                { search = it },
                Modifier.fillMaxWidth(),
                label = { Text("Buscar componente") },
                leadingIcon = { Icon(Icons.Default.Search, null) }
            )
            val results = listOf("Botón", "Interruptor", "Tarjeta", "Deslizador").filter { it.contains(search, true) }
            Text(if (results.isEmpty()) "Sin coincidencias" else results.joinToString(" · "))
        }
        CatalogCard("Agregar a la lista", "Este valor se enviará a la sección de listas cuando cumpla la validación.") {
            OutlinedTextField(uiState.pendingText, viewModel::updatePendingText, Modifier.fillMaxWidth(), label = { Text("Nuevo elemento") })
            Button(onClick = { viewModel.addPendingText() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Add, null)
                Text(" Agregar a la colección")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf(false) }
    fun show(message: String) = scope.launch { snackbarHostState.showSnackbar(message) }

    CatalogPage {
        SnackbarHost(hostState = snackbarHostState)
        Text("Cada acción genera una confirmación visible para demostrar su respuesta al toque.")
        CatalogCard("Variantes de botón", "Un mismo comando puede presentarse con diferente jerarquía visual.") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { show("Botón relleno pulsado") }) { Text("Relleno") }
                OutlinedButton(onClick = { show("Botón con contorno pulsado") }) { Text("Contorno") }
                TextButton(onClick = { show("Botón de texto pulsado") }) { Text("Texto") }
            }
        }
        CatalogCard("Botones con ícono", "Los íconos hacen más reconocible una acción frecuente.") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(onClick = { show("Acción de eliminar") }) { Icon(Icons.Default.Delete, "Eliminar") }
                Button(onClick = { show("Acción de enviar") }) { Icon(Icons.Default.Send, null); Text(" Enviar") }
            }
        }
        CatalogCard("Acciones flotantes", "Destacan la acción principal sobre el resto del contenido.") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FloatingActionButton(onClick = { show("FAB pulsado") }) { Icon(Icons.Default.Add, "Agregar") }
                ExtendedFloatingActionButton(onClick = { show("FAB extendido pulsado") }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Crear") })
            }
        }
        CatalogCard("Selector de alternancia", "Cambia un estado entre dos opciones al pulsar el chip.") {
            FilterChip(selected, { selected = !selected; show(if (selected) "Modo activo" else "Modo inactivo") }, label = { Text(if (selected) "Activo" else "Inactivo") })
        }
        CatalogCard("Estados especiales", "Un botón puede impedir acciones o informar que una tarea está en curso.") {
            Button(onClick = {}, enabled = false) { Text("Deshabilitado") }
            Button(onClick = { show("La acción terminó") }) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Text("  Cargando")
            }
        }
    }
}
