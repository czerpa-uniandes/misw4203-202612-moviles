package com.team4.vinilosapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team4.vinilosapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlbumDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun navigateToAlbumDetail_and_validateDetailScreenContent() {
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule.onNodeWithTag("bottom_nav_albums").performClick()
            composeTestRule.onAllNodesWithText("Ver más").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onAllNodesWithText("Ver más")[0]
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodes(hasTestTag("album_detail_title"), useUnmergedTree = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("album_detail_title", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_genre", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_label", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_release_date", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_cover", useUnmergedTree = true)
            .assertIsDisplayed()
    }
}