package com.team4.vinilosapp.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("album_list_item_1")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("album_detail_title")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_genre")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_label")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_release_date")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("album_detail_cover")
            .assertIsDisplayed()
    }
}