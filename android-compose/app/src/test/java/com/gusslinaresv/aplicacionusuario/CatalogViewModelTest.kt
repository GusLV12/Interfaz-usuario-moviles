package com.gusslinaresv.aplicacionusuario

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogViewModelTest {
    @Test
    fun invalidTextIsNotAddedToCollection() {
        val viewModel = CatalogViewModel()
        val initialSize = viewModel.uiState.value.items.size

        viewModel.updatePendingText("ab")

        assertFalse(viewModel.addPendingText())
        assertEquals(initialSize, viewModel.uiState.value.items.size)
    }

    @Test
    fun validTextIsAddedAndInputIsCleared() {
        val viewModel = CatalogViewModel()
        viewModel.updatePendingText("Nueva tarjeta")

        assertTrue(viewModel.addPendingText())
        assertEquals("", viewModel.uiState.value.pendingText)
        assertEquals("Nueva tarjeta", viewModel.uiState.value.items.last().title)
    }

    @Test
    fun collectionCanBeClearedAndRestored() {
        val viewModel = CatalogViewModel()

        viewModel.clearItems()
        assertTrue(viewModel.uiState.value.items.isEmpty())

        viewModel.resetItems()
        assertEquals(15, viewModel.uiState.value.items.size)
    }
}
