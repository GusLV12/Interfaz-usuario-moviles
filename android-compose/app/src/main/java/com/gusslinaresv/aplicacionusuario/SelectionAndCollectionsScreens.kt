package com.gusslinaresv.aplicacionusuario

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun SelectionScreen() {
    var triState by remember { mutableStateOf(ToggleableState.Indeterminate) }
    var choice by remember { mutableStateOf("Opción A") }
    var enabled by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf(35f) }
    var range by remember { mutableStateOf(20f..75f) }
    var menuExpanded by remember { mutableStateOf(false) }
    var menuChoice by remember { mutableStateOf("Sistema") }
    var date by remember { mutableStateOf("Sin fecha") }
    var time by remember { mutableStateOf("Sin hora") }
    var filters by remember { mutableStateOf(setOf<String>()) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    CatalogPage {
        Text("Elige valores y observa los estados individuales, excluyentes y de rango.")
        CatalogCard("Casillas de verificación", "La casilla puede estar marcada, desmarcada o en estado indeterminado.") {
            Row { TriStateCheckbox(triState, { triState = if (triState == ToggleableState.On) ToggleableState.Off else ToggleableState.On }); Text("Estado: $triState", Modifier.padding(top = 12.dp)) }
        }
        CatalogCard("Botones de opción", "Solo una opción del grupo puede estar activa a la vez.") {
            listOf("Opción A", "Opción B", "Opción C").forEach { option ->
                Row { RadioButton(choice == option, { choice = option }); Text(option, Modifier.padding(top = 12.dp)) }
            }
        }
        CatalogCard("Interruptor", "Activa o desactiva una preferencia binaria.") { Switch(enabled, { enabled = it }); Text(if (enabled) "Notificaciones activas" else "Notificaciones desactivadas") }
        CatalogCard("Deslizadores", "Sirven para seleccionar un valor único o los límites de un intervalo.") {
            Text("Valor: ${value.toInt()}"); Slider(value, { value = it })
            Text("Rango: ${range.start.toInt()} a ${range.endInclusive.toInt()}"); RangeSlider(range, { range = it }, valueRange = 0f..100f)
        }
        CatalogCard("Lista desplegable", "Presenta opciones cuando el espacio disponible es limitado.") {
            OutlinedButton({ menuExpanded = true }) { Text(menuChoice) }
            DropdownMenu(menuExpanded, { menuExpanded = false }) { listOf("Sistema", "Claro", "Oscuro").forEach { item -> DropdownMenuItem({ Text(item) }, { menuChoice = item; menuExpanded = false }) } }
        }
        CatalogCard("Fecha y hora", "Abren selectores nativos del dispositivo.") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { DatePickerDialog(context, { _, y, m, d -> date = "$d/${m + 1}/$y" }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show() }) { Text("Fecha") }
                Button(onClick = { TimePickerDialog(context, { _, h, min -> time = "%02d:%02d".format(h, min) }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show() }) { Text("Hora") }
            }
            Text("$date · $time")
        }
        CatalogCard("Chips de filtro", "Permiten activar varias categorías de manera independiente.") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Diseño", "Código", "Favorito").forEach { filter -> FilterChip(filter in filters, { filters = filters.toggle(filter) }, { Text(filter) }) } }
        }
    }
}

private fun Set<String>.toggle(value: String) = if (value in this) this - value else this + value

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CollectionsScreen(viewModel: CatalogViewModel) {
    val state by viewModel.uiState.collectAsState()
    var selectedItem by remember { mutableStateOf<CatalogItem?>(null) }
    var tab by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column(Modifier.fillMaxSize()) {
        TabRow(tab) { listOf("Lista", "Cuadrícula").forEachIndexed { index, label -> Tab(tab == index, { tab = index }, text = { Text(label) }) } }
        if (tab == 0) {
            Column(
                Modifier.weight(1f).pointerInput(Unit) {
                    var draggedDistance = 0f
                    detectVerticalDragGestures(
                        onVerticalDrag = { _, amount -> draggedDistance += amount },
                        onDragEnd = { if (draggedDistance > 120f) viewModel.resetItems(); draggedDistance = 0f },
                        onDragCancel = { draggedDistance = 0f }
                    )
                }
            ) {
                if (state.items.isEmpty()) EmptyCollection(viewModel)
                else LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    item { Text("Arrastra hacia abajo para restaurar. Mantén pulsado un elemento para ver su detalle; deslízalo para eliminarlo.", style = MaterialTheme.typography.bodySmall) }
                    state.items.groupBy { it.category }.forEach { (category, entries) ->
                        item { Text(category, Modifier.padding(top = 12.dp, bottom = 4.dp), fontWeight = FontWeight.Bold) }
                        items(entries, key = { it.id }) { item -> DismissibleItem(item, { selectedItem = item }, viewModel::removeItem) }
                    }
                    item { OutlinedButton({ viewModel.clearItems() }, Modifier.fillMaxWidth()) { Text("Vaciar colección") } }
                    selectedItem?.let { item { Text("Detalle: ${it.title} (${it.category})", style = MaterialTheme.typography.titleSmall) } }
                }
            }
        } else {
            LazyVerticalGrid(GridCells.Fixed(2), Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.items, key = { it.id }) { item -> CatalogCard(item.title, item.category) { Text("Elemento de cuadrícula") } }
            }
        }
        HorizontalDivider()
        Text("Pestañas con desplazamiento", Modifier.padding(12.dp), fontWeight = FontWeight.Bold)
        HorizontalPager(pagerState, Modifier.height(74.dp).fillMaxWidth()) { page -> Text(if (page == 0) "Desliza horizontalmente para ver la segunda pestaña." else "Segunda pestaña activa.", Modifier.padding(16.dp)) }
    }
}

@Composable
private fun EmptyCollection(viewModel: CatalogViewModel) {
    Column(Modifier.fillMaxSize().padding(32.dp), verticalArrangement = Arrangement.Center) {
        Text("☰", style = MaterialTheme.typography.displayMedium)
        Text("No hay elementos", style = MaterialTheme.typography.headlineSmall)
        Text("Agrega uno desde Entrada de texto o restaura la colección de ejemplo.")
        Button({ viewModel.resetItems() }) { Text("Restaurar elementos") }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DismissibleItem(item: CatalogItem, onDetail: () -> Unit, onRemove: (Int) -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
        if (value != SwipeToDismissBoxValue.Settled) onRemove(item.id)
        true
    })
    SwipeToDismissBox(dismissState, backgroundContent = {}, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().combinedClickable(onClick = onDetail, onLongClick = onDetail).padding(12.dp)) { Text(item.title, Modifier.weight(1f)); Text(item.category, style = MaterialTheme.typography.bodySmall) }
    }
}
