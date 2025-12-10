package com.desafio.githubexplorer.presentation.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.desafio.githubexplorer.core.presentation.ViewResource
import com.desafio.githubexplorer.core.theme.ForkBlue
import com.desafio.githubexplorer.core.theme.GithubAccent
import com.desafio.githubexplorer.core.theme.GithubBorder
import com.desafio.githubexplorer.core.theme.GithubDark
import com.desafio.githubexplorer.core.theme.GithubDarkSecondary
import com.desafio.githubexplorer.core.theme.GithubGreen
import com.desafio.githubexplorer.core.theme.GithubOrange
import com.desafio.githubexplorer.core.theme.GithubPurple
import com.desafio.githubexplorer.core.theme.GithubTextSecondary
import com.desafio.githubexplorer.core.theme.StarYellow
import com.desafio.githubexplorer.presentation.GithubIntent
import com.desafio.githubexplorer.presentation.GithubViewModel
import com.desafio.githubexplorer.presentation.compose.Indicator
import com.desafio.githubexplorer.presentation.screens.repositories.formatCount
import com.desafio.githubexplorer.presentation.screens.repositories.getLanguageColor
import com.desafio.shared.data.dto.Repo
import kotlinx.coroutines.delay
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    viewModel: GithubViewModel,
    owner: String,
    repoName: String,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(owner, repoName) {
        viewModel.intent(GithubIntent.LoadRepositoryDetail(owner, repoName))
        delay(100)
        isVisible = true
    }

    Scaffold(
        containerColor = GithubDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = repoName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GithubDark)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = state.selectedRepository) {
                is ViewResource.Loading -> Indicator()
                is ViewResource.Error -> ErrorContent()
                is ViewResource.Success -> {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn() + slideInVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) { it / 3 }
                    ) {
                        RepositoryDetailContent(repo = result.data)
                    }
                }
                null -> {}
            }
        }
    }
}

@Composable
private fun ErrorContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "⚠️", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Failed to load repository details",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RepositoryDetailContent(repo: Repo) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HeaderCard(repo)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "📊 Statistics",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        StatisticsSection(repo)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "📋 Details",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        DetailsCard(repo)

        if (!repo.topics.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "🏷️ Topics",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repo.topics?.forEach { topic ->
                    TopicChip(topic = topic)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun HeaderCard(repo: Repo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = GithubDarkSecondary),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(GithubAccent.copy(alpha = 0.3f), GithubPurple.copy(alpha = 0.3f))
            )
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = repo.owner.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(3.dp, GithubAccent, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = repo.owner.login,
                        style = MaterialTheme.typography.titleMedium,
                        color = GithubAccent
                    )
                    Text(
                        text = repo.owner.type ?: "User",
                        style = MaterialTheme.typography.labelMedium,
                        color = GithubTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = repo.fullName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (!repo.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = repo.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = GithubTextSecondary
                )
            }

            repo.language?.let { language ->
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(getLanguageColor(language).copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(getLanguageColor(language))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = language,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticsSection(repo: Repo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Star,
            iconTint = StarYellow,
            label = "Stars",
            value = formatCount(repo.stargazersCount),
            backgroundColor = StarYellow.copy(alpha = 0.1f)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = null,
            iconContent = {
                Text(
                    text = "⑂",
                    style = MaterialTheme.typography.titleLarge,
                    color = ForkBlue
                )
            },
            label = "Forks",
            value = formatCount(repo.forksCount),
            backgroundColor = ForkBlue.copy(alpha = 0.1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Info,
            iconTint = GithubOrange,
            label = "Open Issues",
            value = repo.openIssuesCount.toString(),
            backgroundColor = GithubOrange.copy(alpha = 0.1f)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = null,
            iconContent = {
                Text(text = "👁", style = MaterialTheme.typography.titleMedium)
            },
            label = "Watchers",
            value = formatCount(repo.watchersCount),
            backgroundColor = GithubPurple.copy(alpha = 0.1f)
        )
    }
}

@Composable
private fun DetailsCard(repo: Repo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GithubDarkSecondary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DetailRow(
                icon = Icons.Default.DateRange,
                label = "Created",
                value = formatDate(repo.createdAt)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GithubBorder
            )

            DetailRow(
                icon = Icons.Default.DateRange,
                label = "Last Updated",
                value = formatDate(repo.updatedAt)
            )

            repo.pushedAt?.let { pushedAt ->
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = GithubBorder
                )
                DetailRow(
                    icon = Icons.Default.DateRange,
                    label = "Last Push",
                    value = formatDate(pushedAt)
                )
            }

            repo.defaultBranch?.let { branch ->
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = GithubBorder
                )
                DetailRow(
                    icon = null,
                    iconContent = {
                        Text(
                            text = "⎇",
                            style = MaterialTheme.typography.titleMedium,
                            color = GithubGreen
                        )
                    },
                    label = "Default Branch",
                    value = branch
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GithubBorder
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailChip(
                    text = if (repo.isPrivate) "Private" else "Public",
                    color = if (repo.isPrivate) GithubOrange else GithubGreen
                )
                if (repo.isFork) {
                    DetailChip(text = "Fork", color = ForkBlue)
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconContent: (@Composable () -> Unit)? = null,
    iconTint: Color = GithubAccent,
    label: String,
    value: String,
    backgroundColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = iconTint
                )
            } else {
                iconContent?.invoke()
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = GithubTextSecondary
            )
        }
    }
}

@Composable
fun DetailRow(
    icon: ImageVector? = null,
    iconContent: (@Composable () -> Unit)? = null,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = GithubTextSecondary
                )
            } else {
                iconContent?.invoke()
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = GithubTextSecondary
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun DetailChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

@Composable
fun TopicChip(topic: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GithubAccent.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = topic,
            style = MaterialTheme.typography.labelMedium,
            color = GithubAccent
        )
    }
}

fun formatDate(dateString: String): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        zonedDateTime.format(formatter)
    } catch (e: DateTimeParseException) {
        dateString.take(10)
    }
}
