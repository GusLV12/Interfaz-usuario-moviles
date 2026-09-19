package com.gusslinaresv.catalogoui.views

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.lifecycle.ViewModelProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test fun navigationAndTextToCollectionFlowWork() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                activity.showSection(CatalogSection.TEXT)
                val model = ViewModelProvider(activity)[CatalogViewModel::class.java]
                model.updatePendingText("Elemento de prueba")
                assertTrue(model.addPendingText())
                activity.showSection(CatalogSection.LISTS)
                assertEquals("Elemento de prueba", model.items.value!!.last().title)
            }
        }
    }
}
