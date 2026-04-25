package com.team4.vinilosapp.ui

import com.team4.vinilosapp.ui.screens.albums.CreateAlbumScreen
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class CreateAlbumScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createAlbumScreen_displaysMainFields() {
        composeTestRule.setContent {
            CreateAlbumScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("TÍTULO DEL ÁLBUM").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("URL DE CARATULA").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("SELLO DISCOGRÁFICO").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("GENERO").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Sony Music").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Rock").assertExists().assertIsDisplayed()
    }

    @Test
    fun createAlbumScreen_allowsTypingInTextFields() {
        composeTestRule.setContent {
            CreateAlbumScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Ej: Artaud").performTextInput("Artaud")
        composeTestRule.onNodeWithText("Ej: https://image.url")
            .performTextInput("https://mi-imagen.com/cover.jpg")

        composeTestRule.onNodeWithText("Artaud").assertExists()
        composeTestRule.onNodeWithText("https://mi-imagen.com/cover.jpg").assertExists()
    }

    @Test
    fun createAlbumScreen_displaysGenreAndLabelOptions() {
        composeTestRule.setContent {
            CreateAlbumScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Sony Music").assertExists()
        composeTestRule.onNodeWithText("EMI").assertExists()
        composeTestRule.onNodeWithText("Discos Fuentes").assertExists()
        composeTestRule.onNodeWithText("Elektra").assertExists()
        composeTestRule.onNodeWithText("Fania Records").assertExists()

        composeTestRule.onNodeWithText("Classical").assertExists()
        composeTestRule.onNodeWithText("Salsa").assertExists()
        composeTestRule.onNodeWithText("Rock").assertExists()
        composeTestRule.onNodeWithText("Folk").assertExists()
    }

    @Test
    fun createAlbumScreen_allowsSelectingGenreAndRecordLabel() {
        composeTestRule.setContent {
            CreateAlbumScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Sony Music").performClick()
        composeTestRule.onNodeWithText("Rock").performClick()

        composeTestRule.onNodeWithText("Sony Music").assertExists()
        composeTestRule.onNodeWithText("Rock").assertExists()
    }

    @Test
    fun createAlbumScreen_showsCreateButton() {
        composeTestRule.setContent {
            CreateAlbumScreen(navController = rememberNavController())
        }

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("CREATE_ALBUM_TEST")

        composeTestRule.onNodeWithText("Crear Album", useUnmergedTree = true).assertExists().assertIsDisplayed()
    }
}