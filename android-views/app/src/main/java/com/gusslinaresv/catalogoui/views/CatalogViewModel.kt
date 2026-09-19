package com.gusslinaresv.catalogoui.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class CatalogItem(val id: Int, val title: String, val category: String)

class CatalogViewModel : ViewModel() {
    private val _items = MutableLiveData(defaultItems())
    val items: LiveData<List<CatalogItem>> = _items
    private val _pendingText = MutableLiveData("")
    val pendingText: LiveData<String> = _pendingText

    fun updatePendingText(value: String) { _pendingText.value = value }
    fun addPendingText(): Boolean {
        val value = _pendingText.value.orEmpty().trim()
        if (value.length < 3) return false
        val current = _items.value.orEmpty()
        _items.value = current + CatalogItem((current.maxOfOrNull { it.id } ?: 0) + 1, value, "Agregado")
        _pendingText.value = ""
        return true
    }
    fun remove(id: Int) { _items.value = _items.value.orEmpty().filterNot { it.id == id } }
    fun clear() { _items.value = emptyList() }
    fun reset() { _items.value = defaultItems() }
}

fun defaultItems() = List(15) { index -> CatalogItem(index + 1, "Elemento ${index + 1}", if (index % 2 == 0) "Diseño" else "Interacción") }
