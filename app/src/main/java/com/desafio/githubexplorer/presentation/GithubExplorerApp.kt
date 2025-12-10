package com.desafio.githubexplorer.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.desafio.githubexplorer.core.theme.GithubDark
import com.desafio.githubexplorer.presentation.navigation.NavGraph

@Composable
fun GithubExplorerApp(viewModel: GithubViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GithubDark
    ) {
        val navController = rememberNavController()
        NavGraph(
            navController = navController,
            viewModel = viewModel
        )
    }
}

