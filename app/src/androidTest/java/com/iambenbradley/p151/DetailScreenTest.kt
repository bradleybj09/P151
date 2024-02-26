package com.iambenbradley.p151

import androidx.annotation.UiThread
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.iambenbradley.p151.model.domain.PokeColor
import com.iambenbradley.p151.model.domain.PokemonDetailImpl
import com.iambenbradley.p151.model.result.PokemonDetailResult
import com.iambenbradley.p151.ui.detail.DetailScreen
import com.iambenbradley.p151.ui.detail.DetailViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK
    private lateinit var viewModel: DetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val pokemon = PokemonDetailImpl(
        0, "", "", PokeColor.Red, emptyList(), null, emptyMap(), null, false, emptySet()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        coEvery { viewModel.pokemonName } returns(
            MutableStateFlow("Pokemon!")
        )
        coEvery { viewModel.pokemonId } answers {
            MutableStateFlow(0L)
        }
    }

    @Test
    fun viewModelLoadingStateShowsLoadingScreen() = runTest {
        coEvery { viewModel.currentPokemon } returns(
            MutableStateFlow(PokemonDetailResult.Loading)
        )

        composeTestRule.setContent {
            DetailScreen(
                onRelatedPokemonClick = { _, _ -> },
                onBackButtonClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("Loading").assertIsDisplayed()
    }

    @Test
    fun viewModelFailedStateShowsFailureScreen() = runTest {
        coEvery { viewModel.currentPokemon } returns(
            MutableStateFlow(PokemonDetailResult.Failure)
        )

        composeTestRule.setContent {
            DetailScreen(
                onRelatedPokemonClick = { _, _ -> },
                onBackButtonClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("Failure").assertIsDisplayed()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun viewModelSuccessStateShowsDetailScreen() = runTest {
        Dispatchers.resetMain() // COIL!!
        coEvery { viewModel.currentPokemon } returns(
            MutableStateFlow(PokemonDetailResult.Success(pokemon))
        )

        composeTestRule.setContent {
            DetailScreen(
                onRelatedPokemonClick = { _, _ -> },
                onBackButtonClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("PokeDetail").assertIsDisplayed()
    }
}