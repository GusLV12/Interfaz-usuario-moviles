package com.gusslinaresv.aplicacionusuario

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CatalogItem(val id: Int, val title: String, val category: String)

data class CatalogUiState(
    val pendingText: String = "",
    val items: List<CatalogItem> = defaultItems()
)

class CatalogViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState = _uiState.asStateFlow()

    fun updatePendingText(value: String) = _uiState.update { it.copy(pendingText = value) }

    fun addPendingText(): Boolean {
        val value = _uiState.value.pendingText.trim()
        if (value.length < 3) return false
        _uiState.update { state ->
            state.copy(
                pendingText = "",
                items = state.items + CatalogItem((state.items.maxOfOrNull { it.id } ?: 0) + 1, value, "Agregado")
            )
        }
        return true
    }

    fun removeItem(id: Int) = _uiState.update { state -> state.copy(items = state.items.filterNot { it.id == id }) }

    fun clearItems() = _uiState.update { it.copy(items = emptyList()) }

    fun resetItems() = _uiState.update { it.copy(items = defaultItems()) }
}

fun defaultItems() = List(15) { index ->
    CatalogItem(index + 1, "Elemento ${index + 1}", if (index % 2 == 0) "Diseño" else "Interacción")
}
