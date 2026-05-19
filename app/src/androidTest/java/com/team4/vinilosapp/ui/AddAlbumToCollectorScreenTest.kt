package com.team4.vinilosapp.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.team4.vinilosapp.ui.screens.albums.AddAlbumToCollectorScreen
import androidx.compose.ui.test.onAllNodesWithText
import org.junit.Rule
import org.junit.Test

class AddAlbumToCollectorScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addAlbumToCollectorScreen_showsFormFields() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            AddAlbumToCollectorScreen(
                navController = navController,
                albumId = "1"
            )
        }

        composeTestRule.onNodeWithText("Agregar a mi colección").assertExists()
        composeTestRule.onNodeWithTag("price_input").assertExists()
        composeTestRule.onNodeWithTag("status_input").assertExists()
        composeTestRule.onNodeWithTag("add_album_collector_button").assertExists()
    }

    @Test
    fun addAlbumToCollectorScreen_allowsWritingPrice() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            AddAlbumToCollectorScreen(
                navController = navController,
                albumId = "1"
            )
        }

        composeTestRule
            .onNodeWithTag("price_input")
            .performTextInput("50000")

        composeTestRule
            .onNodeWithText("50000")
            .assertExists()
    }

    @Test
    fun addAlbumToCollectorScreen_showsErrorWhenPriceIsEmpty() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            AddAlbumToCollectorScreen(
                navController = navController,
                albumId = "1"
            )
        }

        composeTestRule
            .onNodeWithTag("add_album_collector_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Ingresa un precio válido")
            .assertExists()
    }

    @Test
    fun addAlbumToCollectorScreen_showsStatusOptions() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            AddAlbumToCollectorScreen(
                navController = navController,
                albumId = "1"
            )
        }

        composeTestRule
            .onNodeWithTag("status_input")
            .performClick()

        composeTestRule
            .onAllNodesWithText("Activo")[1]
            .assertExists()

        composeTestRule
            .onNodeWithText("Inactivo")
            .assertExists()
    }
}