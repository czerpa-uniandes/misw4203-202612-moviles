package com.team4.vinilosapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team4.vinilosapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddPrizeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun goToAddPrizeScreen() {
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("bottom_nav_awards", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("bottom_nav_awards", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithText("Premios")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("bottom_add_prizes", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("bottom_add_prizes", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("prize_name_input", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun navigateToAddPrizeScreen_andValidateFormIsDisplayed() {
        goToAddPrizeScreen()

        composeTestRule
            .onNodeWithTag("prize_name_input", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("prize_description_input", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("prize_organization_input", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("add_prize_button", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun fillAddPrizeForm_andSubmit_returnsToPrizesSection() {
        goToAddPrizeScreen()

        val shortId = java.util.UUID.randomUUID()
            .toString()
            .replace("-", "")
            .take(8)

        val prizeName = "Premio UI Test $shortId"

        composeTestRule
            .onNodeWithTag("prize_name_input", useUnmergedTree = true)
            .performTextInput(prizeName)

        composeTestRule
            .onNodeWithTag("prize_description_input", useUnmergedTree = true)
            .performTextInput("Premio creado desde prueba automática")

        composeTestRule
            .onNodeWithTag("prize_organization_input", useUnmergedTree = true)
            .performTextInput("Organización UI Test")

        composeTestRule
            .onNodeWithTag("add_prize_button", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule
                .onAllNodesWithText("Premios", substring = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onAllNodesWithText("Premios", substring = true)[0]
            .assertIsDisplayed()
    }
}