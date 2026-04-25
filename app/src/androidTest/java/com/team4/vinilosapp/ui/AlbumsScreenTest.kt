package com.team4.vinilosapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.team4.vinilosapp.ui.screens.albums.AlbumsScreen
import org.junit.Rule
import org.junit.Test

class AlbumsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun albumsScreen_showsAlbumsList() {
        composeTestRule.setContent {
            AlbumsScreen(navController = rememberNavController())
        }

        composeTestRule
            .onNodeWithTag("albums_lazy_column")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun albumsScreen_showsAddAlbumButton() {
        composeTestRule.setContent {
            AlbumsScreen(navController = rememberNavController())
        }

        composeTestRule
            .onNodeWithText("Añadir álbum", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun albumsScreen_opensFilterBottomSheet_whenFilterButtonIsClicked() {
        composeTestRule.setContent {
            AlbumsScreen(navController = rememberNavController())
        }

        composeTestRule
            .onNodeWithTag("filter_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Filtros")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun albumsScreen_appliesFilter_andClosesBottomSheet() {
        composeTestRule.setContent {
            AlbumsScreen(navController = rememberNavController())
        }

        composeTestRule
            .onNodeWithTag("filter_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Aplicar filtros")
            .performClick()

        composeTestRule
            .onNodeWithText("Filtros")
            .assertDoesNotExist()
    }
}