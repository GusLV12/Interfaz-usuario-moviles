package com.gusslinaresv.catalogoui.views

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.Test

class CatalogViewModelTest {
    @get:Rule val executorRule = InstantTaskExecutorRule()
    @Test fun invalidTextIsRejected() { val model=CatalogViewModel(); val size=model.items.value!!.size; model.updatePendingText("ab"); assertFalse(model.addPendingText()); assertEquals(size,model.items.value!!.size) }
    @Test fun validTextIsAddedAndCleared() { val model=CatalogViewModel(); model.updatePendingText("Nueva tarjeta"); assertTrue(model.addPendingText()); assertEquals("",model.pendingText.value); assertEquals("Nueva tarjeta",model.items.value!!.last().title) }
    @Test fun itemsCanBeClearedAndRestored() { val model=CatalogViewModel(); model.clear(); assertTrue(model.items.value!!.isEmpty()); model.reset(); assertEquals(15,model.items.value!!.size) }
}
