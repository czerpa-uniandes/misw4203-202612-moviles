package com.team4.vinilosapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.team4.vinilosapp.MainActivity
import org.junit.Rule
import org.junit.Test

class CollectorDetailScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun goToDetailCollectorScreen() {
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("bottom_nav_collectors", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("bottom_nav_collectors", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithText("Ver colección")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onAllNodesWithText("Ver colección")[0]
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("collector_detail_name", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun navigateToCollectorDetail_andValidateContent() {
        goToDetailCollectorScreen()

        composeTestRule
            .onNodeWithTag("collector_detail_name", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Comentarios")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Artistas favoritos")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Álbumes del coleccionista")
            .assertIsDisplayed()
    }
}