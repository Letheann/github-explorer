package com.desafio.githubexplorer.presentation.compose

import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.desafio.githubexplorer.core.presentation.ViewResource
import com.desafio.githubexplorer.core.theme.GithubAccent
import com.desafio.githubexplorer.core.theme.GithubDarkSecondary
import com.desafio.githubexplorer.core.theme.GithubTextSecondary
import com.desafio.githubexplorer.presentation.ViewIntent
import com.desafio.githubexplorer.presentation.WelcomeViewModel
import com.desafio.shared.data.dto.Repo

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

@OptIn(ExperimentalMaterial3Api::class)
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
        colors = CardDefaults.cardColors(containerColor = GithubDarkSecondary),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = repo.owner.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, GithubAccent.copy(alpha = 0.3f), CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = repo.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    color = GithubAccent,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!repo.description.isNullOrBlank()) {
                    Text(
                        text = repo.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = GithubTextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repo.language?.let { language ->
                        Text(
                            text = "📝 $language",
                            style = MaterialTheme.typography.labelSmall,
                            color = GithubTextSecondary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = "⭐ ${repo.stargazersCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GithubTextSecondary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "🍴 ${repo.forksCount}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GithubTextSecondary
                    )
                }
            }
        }
    }
}
