package com.team4.vinilosapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.team4.vinilosapp.MainActivity
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.swipeUp

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
                .onAllNodesWithTag("collector_detail_nav_button", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onAllNodesWithTag("collector_detail_nav_button", useUnmergedTree = true)[0]
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
            .onNodeWithText("Comentarios", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        repeat(2) {
            composeTestRule
                .onNodeWithTag("collector_detail_content", useUnmergedTree = true)
                .performTouchInput {
                    swipeUp()
                }
        }

        composeTestRule
            .onNodeWithText("Artistas favoritos")
            .assertExists()
            .assertIsDisplayed()
    }
}