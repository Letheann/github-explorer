package com.desafio.githubexplorer.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.desafio.githubexplorer.core.presentation.ViewResource
import com.desafio.githubexplorer.presentation.ViewIntent
import com.desafio.githubexplorer.presentation.WelcomeViewModel
import com.desafio.shared.data.dto.Owner
import com.desafio.shared.data.dto.Repo

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecyclerCompose(
    viewModel: WelcomeViewModel,
    invokeClick: (owner: String, repo: String) -> Unit,
    disposable: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> if (uiState.searchResult == null) viewModel.intent(ViewIntent.GetPopularRepos())
            Lifecycle.State.CREATED -> viewModel.intent(ViewIntent.GetPopularRepos())
            else -> {}
        }
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(viewModel)
        onDispose {
            lifecycle.removeObserver(viewModel)
            disposable.invoke()
        }
    }

    when (val states = uiState.searchResult) {
        is ViewResource.Success -> {
            LazyColumn(Modifier.fillMaxSize()) {
                items(states.data.items) { repo ->
                    RepoCompose(repo = repo, invokeClick = invokeClick)
                }
            }
        }
        is ViewResource.Loading -> Indicator()
        is ViewResource.Error -> CharacterError()
        else -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepoCompose(
    repo: Repo,
    invokeClick: (owner: String, repo: String) -> Unit
) {
    Card(
        onClick = { invokeClick(repo.owner.login, repo.name) },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(repo.owner.avatarUrl),
                contentDescription = repo.owner.login,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = repo.fullName,
                    style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.primary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!repo.description.isNullOrBlank()) {
                    Text(
                        text = repo.description ?: "",
                        style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.onSurface),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    repo.language?.let { language ->
                        Text(
                            text = "📝 $language",
                            style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.secondary)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = "⭐ ${repo.stargazersCount}",
                        style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.secondary)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "🍴 ${repo.forksCount}",
                        style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.secondary)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RepoItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        RepoCompose(
            repo = Repo(
                id = 1,
                name = "kotlin-explorer",
                fullName = "user/kotlin-explorer",
                description = "A sample Kotlin repo",
                htmlUrl = "https://github.com/user/kotlin-explorer",
                language = "Kotlin",
                stargazersCount = 123,
                watchersCount = 100,
                forksCount = 45,
                openIssuesCount = 5,
                owner = Owner(
                    id = 1,
                    login = "user",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                    htmlUrl = "https://github.com/user",
                    type = "User"
                ),
                createdAt = "2025-01-01T12:00:00Z",
                updatedAt = "2025-12-08T12:00:00Z",
                pushedAt = "2025-12-08T12:00:00Z",
                isPrivate = false,
                isFork = false,
                defaultBranch = "main",
                topics = listOf("kotlin", "android")
            ),
            invokeClick = { _, _ -> }
        )
    }
}
