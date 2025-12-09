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
import com.desafio.shared.data.dto.Repo

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecyclerCompose(
    viewModel: WelcomeViewModel,
    invokeClick: (id: Long?) -> Unit,
    disposable: () -> Unit = {}
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> if (uiState.items == null) viewModel.intent(ViewIntent.GetRepos)
            Lifecycle.State.CREATED -> viewModel.intent(ViewIntent.GetRepos)
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

    when (val states = uiState.items) {
        is ViewResource.Success -> {
            LazyColumn(Modifier.fillMaxSize()) {
                items(states.data) { repo ->
                    RepoCompose(repo = repo, invokeClick = invokeClick)
                }
            }
        }

        is ViewResource.Loading -> {
            Indicator()
        }

        is ViewResource.Error -> {
            CharacterError()
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RepoCompose(
    repo: Repo,
    invokeClick: (id: Long?) -> Unit
) {
    Card(
        onClick = { invokeClick(repo.id) },
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
                painter = rememberAsyncImagePainter(repo.owner?.avatar_url ?: ""),
                contentDescription = repo.owner?.login,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = repo.full_name ?: "Unknown Repo",
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
                    Text(
                        text = "⭐ ${repo.stargazers_count ?: 0}",
                        style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.secondary)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "🍴 ${repo.forks_count ?: 0}",
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
                full_name = "user/kotlin-explorer",
                description = "A sample Kotlin repo",
                stargazers_count = 123,
                forks_count = 45,
                owner = com.desafio.shared.data.dto.Owner(
                    login = "user",
                    avatar_url = "https://avatars.githubusercontent.com/u/1?v=4"
                ),
                updated_at = "2025-12-08T12:00:00Z"
            ),
            invokeClick = { /* click action */ }
        )
    }
}
