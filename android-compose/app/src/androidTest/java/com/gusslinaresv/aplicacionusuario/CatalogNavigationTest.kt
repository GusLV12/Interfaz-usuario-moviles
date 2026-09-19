package com.gusslinaresv.aplicacionusuario

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gusslinaresv.aplicacionusuario.ui.theme.AplicacionusuarioTheme
import org.junit.Rule
import org.junit.Test

class CatalogNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeNavigatesToTextInputs() {
        composeRule.setContent { AplicacionusuarioTheme { CatalogApp() } }

        composeRule.onNodeWithText("Entrada de texto").performClick()

        composeRule.onNodeWithText("Campo simple").assertExists()
    }
}
