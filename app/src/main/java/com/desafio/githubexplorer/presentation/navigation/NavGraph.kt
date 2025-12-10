package com.desafio.githubexplorer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.desafio.githubexplorer.presentation.GithubViewModel
import com.desafio.githubexplorer.presentation.screens.detail.RepositoryDetailScreen
import com.desafio.githubexplorer.presentation.screens.repositories.RepositoriesScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: GithubViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Repositories.route
    ) {
        composable(NavRoutes.Repositories.route) {
            RepositoriesScreen(
                viewModel = viewModel,
                onRepositoryClick = { owner, repo ->
                    navController.navigate(NavRoutes.RepositoryDetail.createRoute(owner, repo))
                }
            )
        }

        composable(
            route = NavRoutes.RepositoryDetail.route,
            arguments = listOf(
                navArgument("owner") { type = NavType.StringType },
                navArgument("repo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val owner = backStackEntry.arguments?.getString("owner") ?: ""
            val repo = backStackEntry.arguments?.getString("repo") ?: ""
            RepositoryDetailScreen(
                viewModel = viewModel,
                owner = owner,
                repoName = repo,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
