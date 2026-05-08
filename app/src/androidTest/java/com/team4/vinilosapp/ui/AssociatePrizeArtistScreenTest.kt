package com.team4.vinilosapp.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.team4.vinilosapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AssociatePrizeArtistScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun goToAssociatePrizeArtistScreen() {
        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodes(hasTestTagPrefix("artist_card_"), useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onAllNodes(hasTestTagPrefix("artist_card_"), useUnmergedTree = true)[0]
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("artist_detail_scroll", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("artist_detail_scroll", useUnmergedTree = true)
            .performScrollToNode(hasTestTag("assosiate_prize_artist_button"))

        composeTestRule
            .onNodeWithTag("assosiate_prize_artist_button", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("prize_selected", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Test
    fun navigateToAssociatePrizeArtistScreen_andValidateFormIsDisplayed() {
        goToAssociatePrizeArtistScreen()

        composeTestRule
            .onNodeWithTag("prize_selected", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("prize_premiation_date_input", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("prize_associate_button", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun associatePrizeToArtist_andValidateItIsDisplayedInArtistDetail() {
        goToAssociatePrizeArtistScreen()

        composeTestRule
            .onNodeWithTag("prize_selected", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.waitUntil(timeoutMillis = 15_000) {
            composeTestRule
                .onAllNodesWithTag("prize_option_100", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("prize_option_100", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("prize_premiation_date_input", useUnmergedTree = true)
            .performTextInput("1980-12-10")

        composeTestRule
            .onNodeWithTag("prize_associate_button", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule
                .onAllNodesWithTag("artist_detail_scroll", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithTag("artist_detail_scroll", useUnmergedTree = true)
            .performScrollToNode(hasTestTag("artist_detail_prizes_section"))

        composeTestRule.waitUntil(timeoutMillis = 20_000) {
            composeTestRule
                .onAllNodesWithTag("artist_detail_prize_item", useUnmergedTree = true)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onAllNodesWithTag("artist_detail_prize_item", useUnmergedTree = true)[0]
            .assertIsDisplayed()

        composeTestRule.waitForIdle()
    }

    private fun hasTestTagPrefix(prefix: String): SemanticsMatcher {
        return SemanticsMatcher("has test tag prefix $prefix") { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)?.startsWith(prefix) == true
        }
    }
}