package com.team4.vinilosapp.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.team4.vinilosapp.ui.screens.albums.CommentAlbumScreen
import org.junit.Rule
import org.junit.Test

class CommentAlbumScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun commentAlbumScreen_showsFormFields() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            CommentAlbumScreen(
                navController = navController,
                albumId = "100"
            )
        }

        composeTestRule.onNodeWithTag("comment_input").assertExists()
        composeTestRule.onNodeWithTag("rating_input").assertExists()
        composeTestRule.onNodeWithTag("send_comment_button").assertExists()
    }

    @Test
    fun commentAlbumScreen_allowsUserToWriteCommentAndRating() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            CommentAlbumScreen(
                navController = navController,
                albumId = "1"
            )
        }

        composeTestRule
            .onNodeWithTag("comment_input")
            .performTextInput("Excelente álbum")

        composeTestRule
            .onNodeWithTag("rating_input")
            .performTextInput("5")

        composeTestRule.onNodeWithTag("send_comment_button").assertExists()
    }

    @Test
    fun commentAlbumScreen_showsErrorWhenCommentIsEmpty() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            CommentAlbumScreen(
                navController = navController,
                albumId = "1"
            )
        }

        composeTestRule
            .onNodeWithTag("rating_input")
            .performTextInput("5")

        composeTestRule
            .onNodeWithTag("send_comment_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Debes escribir un comentario")
            .assertExists()
    }

    @Test
    fun commentAlbumScreen_showsErrorWhenRatingIsInvalid() {
        composeTestRule.setContent {
            val navController = rememberNavController()

            CommentAlbumScreen(
                navController = navController,
                albumId = "1"
            )
        }

        composeTestRule
            .onNodeWithTag("comment_input")
            .performTextInput("Muy buen álbum")

        composeTestRule
            .onNodeWithTag("rating_input")
            .performTextInput("9")

        composeTestRule
            .onNodeWithTag("send_comment_button")
            .performClick()

        composeTestRule
            .onNodeWithText("La calificación debe estar entre 1 y 5")
            .assertExists()
    }
}