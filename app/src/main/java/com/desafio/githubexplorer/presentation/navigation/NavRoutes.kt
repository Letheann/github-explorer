package com.desafio.githubexplorer.presentation.navigation

sealed class NavRoutes(val route: String) {
    data object Repositories : NavRoutes("repositories")
    data object RepositoryDetail : NavRoutes("repository/{owner}/{repo}") {
        fun createRoute(owner: String, repo: String) = "repository/$owner/$repo"
    }
}
