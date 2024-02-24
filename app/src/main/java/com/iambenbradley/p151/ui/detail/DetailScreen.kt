package com.iambenbradley.p151.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.iambenbradley.p151.main.LocalUserTypePreference

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val preferredType = LocalUserTypePreference.current

    val pokemon by viewModel.currentPokemon.collectAsState()
}