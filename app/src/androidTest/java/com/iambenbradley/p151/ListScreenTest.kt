package com.iambenbradley.p151

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.iambenbradley.p151.model.domain.PokemonSummaryImpl
import com.iambenbradley.p151.model.result.PokemonSummaryResult
import com.iambenbradley.p151.ui.list.ListScreen
import com.iambenbradley.p151.ui.list.ListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK
    private lateinit var viewModel: ListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun viewModelLoadingStateShowsLoadingScreen() = runTest {
        coEvery { viewModel.pokemon } returns(
            MutableStateFlow(PokemonSummaryResult.Loading)
        )

        composeTestRule.setContent {
            ListScreen(
                onClickPokemon = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("Loading").assertIsDisplayed()
    }

    @Test
    fun viewModelFailedStateShowsFailureScreen() = runTest {
        coEvery { viewModel.pokemon } returns(
            MutableStateFlow(PokemonSummaryResult.Failure)
        )

        composeTestRule.setContent {
            ListScreen(
                onClickPokemon = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("Failure").assertIsDisplayed()
    }


    @Test
    fun viewModelSuccessStateShowsListScreen() = runTest {
        coEvery { viewModel.pokemon } returns(
            MutableStateFlow(
                PokemonSummaryResult.Success(listOf(PokemonSummaryImpl(0, "Pokemon!")))
            )
        )

        composeTestRule.setContent {
            ListScreen(
                onClickPokemon = { _, _ -> },
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("PokeList").assertIsDisplayed()
    }

}