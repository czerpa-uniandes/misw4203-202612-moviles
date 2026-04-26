package com.team4.vinilosapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team4.vinilosapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddTrackAlbumScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun goToAddTrackScreen() {
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("bottom_nav_albums", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("bottom_nav_albums", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithText("Ver más")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onAllNodesWithText("Ver más")[0]
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("album_detail_scroll", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("album_detail_scroll", useUnmergedTree = true)
            .performScrollToNode(hasTestTag("add_track_nav_button"))

        composeTestRule
            .onNodeWithTag("add_track_nav_button", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("track_name_input", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("track_name_input", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun navigateToAddTrackScreen_andValidateFormIsDisplayed() {
        goToAddTrackScreen()

        composeTestRule
            .onNodeWithTag("track_name_input", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("track_minutes_input", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("track_seconds_input", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("add_track_button", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun fillAddTrackForm_andSubmit() {
        goToAddTrackScreen()

        composeTestRule
            .onNodeWithTag("track_name_input", useUnmergedTree = true)
            .performTextInput("Pedro Navaja")

        composeTestRule
            .onNodeWithTag("track_minutes_input", useUnmergedTree = true)
            .performTextInput("5")

        composeTestRule
            .onNodeWithTag("track_seconds_input", useUnmergedTree = true)
            .performTextInput("20")

        composeTestRule
            .onNodeWithTag("add_track_button", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("album_detail_scroll", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("album_detail_scroll", useUnmergedTree = true)
            .performScrollToNode(hasTestTag("add_track_nav_button"))


        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithText("Pedro Navaja", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onAllNodesWithText("Pedro Navaja", useUnmergedTree = true)[0]
            .assertIsDisplayed()

        composeTestRule.waitForIdle()
    }
}