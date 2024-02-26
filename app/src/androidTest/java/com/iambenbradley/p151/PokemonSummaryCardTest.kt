package com.iambenbradley.p151

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.iambenbradley.p151.ui.common.PokemonSummaryCard
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonSummaryCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK(relaxed = true)
    private lateinit var onClick: () -> Unit

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun summaryCardShowsBackButtonAndCallsBackIfTrue() {
        composeTestRule.setContent {
            PokemonSummaryCard(
                name = "test",
                id = 1,
                onClick = { _, _ -> },
                useBackButton = true,
                onBackButtonClick = onClick,
            )
        }

        composeTestRule.onNodeWithContentDescription("back to list").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("back to list").performClick()
        verify { onClick() }
    }

    @Test
    fun summaryCardShowsPokeballAndDoesNotCallBackIfFalse() {
        composeTestRule.setContent {
            PokemonSummaryCard(
                name = "test",
                id = 1,
                onClick = { _, _ -> },
                useBackButton = false,
                onBackButtonClick = onClick,
            )
        }

        composeTestRule.onNodeWithContentDescription("pokeball").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("pokeball").performClick()
        verify(exactly = 0) { onClick() }
    }
}
