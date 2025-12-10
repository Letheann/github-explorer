package com.desafio.githubexplorer.presentation.screens.repositories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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
import com.desafio.githubexplorer.core.theme.GithubTextSecondary
import com.desafio.githubexplorer.core.theme.StarYellow
import com.desafio.githubexplorer.presentation.GithubIntent
import com.desafio.githubexplorer.presentation.GithubViewModel
import com.desafio.githubexplorer.presentation.SortOption
import com.desafio.githubexplorer.presentation.compose.Indicator
import com.desafio.shared.data.dto.Repo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoriesScreen(
    viewModel: GithubViewModel,
    onRepositoryClick: (owner: String, repo: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var showSortMenu by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        if (state.repositoriesResult == null) {
            viewModel.intent(GithubIntent.LoadRepositories())
        }
    }

    Scaffold(
        containerColor = GithubDark,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "{ }",
                                style = MaterialTheme.typography.headlineSmall,
                                color = GithubAccent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Github Repositories",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    actions = {
                        SortButton(
                            currentSort = state.sortOption,
                            expanded = showSortMenu,
                            onExpandChange = { showSortMenu = it },
                            onSortSelected = { option ->
                                viewModel.intent(GithubIntent.ChangeSortOption(option))
                                showSortMenu = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = GithubDark)
                )

                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        keyboardController?.hide()
                        if (searchQuery.isNotBlank()) {
                            viewModel.intent(GithubIntent.SearchByLanguage(searchQuery.trim()))
                        }
                    }
                )

                if (state.searchLanguage.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        LanguageChip(
                            language = state.searchLanguage,
                            onClear = {
                                searchQuery = ""
                                viewModel.intent(GithubIntent.SearchByLanguage(""))
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val result = state.repositoriesResult) {
                is ViewResource.Loading -> Indicator()
                is ViewResource.Error -> ErrorState(
                    onRetry = { viewModel.intent(GithubIntent.LoadRepositories()) }
                )
                is ViewResource.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        state = gridState,
                        contentPadding = PaddingValues(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items = result.data.items, key = { it.id }) { repo ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically { it / 2 }
                            ) {
                                RepositoryCard(
                                    repo = repo,
                                    onClick = { onRepositoryClick(repo.owner.login, repo.name) }
                                )
                            }
                        }
                    }
                }
                null -> {}
            }
        }
    }
}

@Composable
private fun SortButton(
    currentSort: SortOption,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    Box {
        val rotation by animateFloatAsState(
            targetValue = if (expanded) 180f else 0f,
            label = "sortRotation"
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onExpandChange(!expanded) }
                .border(1.dp, GithubBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currentSort.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = GithubTextSecondary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = GithubTextSecondary,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(rotation)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandChange(false) },
            modifier = Modifier.background(GithubDarkSecondary)
        ) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.displayName,
                            color = if (currentSort == option) GithubAccent
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = { onSortSelected(option) }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text(
                text = "Search by language (e.g., kotlin, rust, python)",
                color = GithubTextSecondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = GithubTextSecondary
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GithubAccent,
            unfocusedBorderColor = GithubBorder,
            cursorColor = GithubAccent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = GithubDarkSecondary,
            unfocusedContainerColor = GithubDarkSecondary
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() })
    )
}

@Composable
fun RepositoryCard(
    repo: Repo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GithubDarkSecondary),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(GithubBorder, GithubBorder.copy(alpha = 0.5f))
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = repo.owner.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(2.dp, GithubAccent.copy(alpha = 0.3f), CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = repo.owner.login,
                    style = MaterialTheme.typography.labelMedium,
                    color = GithubTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = repo.name,
                style = MaterialTheme.typography.titleMedium,
                color = GithubAccent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = repo.description ?: "No description",
                style = MaterialTheme.typography.bodySmall,
                color = GithubTextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                minLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = StarYellow
                        )
                    },
                    value = formatCount(repo.stargazersCount)
                )

                StatChip(
                    icon = {
                        Text(
                            text = "⑂",
                            style = MaterialTheme.typography.labelMedium,
                            color = ForkBlue
                        )
                    },
                    value = formatCount(repo.forksCount)
                )
            }

            repo.language?.let { language ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(getLanguageColor(language))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = language,
                        style = MaterialTheme.typography.labelSmall,
                        color = GithubTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun StatChip(
    icon: @Composable () -> Unit,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = GithubDark.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LanguageChip(
    language: String,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GithubGreen.copy(alpha = 0.2f))
            .border(1.dp, GithubGreen, RoundedCornerShape(20.dp))
            .clickable(onClick = onClear)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "language:$language",
            style = MaterialTheme.typography.labelMedium,
            color = GithubGreen
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "✕",
            style = MaterialTheme.typography.labelSmall,
            color = GithubGreen
        )
    }
}

@Composable
fun ErrorState(onRetry: () -> Unit) {
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
            text = "Oops! Something went wrong",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap to retry",
            style = MaterialTheme.typography.bodyMedium,
            color = GithubAccent,
            modifier = Modifier.clickable(onClick = onRetry)
        )
    }
}

fun formatCount(count: Int): String = when {
    count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000.0)
    count >= 1_000 -> String.format("%.1fK", count / 1_000.0)
    else -> count.toString()
}

fun getLanguageColor(language: String): Color = when (language.lowercase()) {
    "kotlin" -> Color(0xFFA97BFF)
    "java" -> Color(0xFFB07219)
    "python" -> Color(0xFF3572A5)
    "javascript" -> Color(0xFFF1E05A)
    "typescript" -> Color(0xFF2B7489)
    "rust" -> Color(0xFFDEA584)
    "go" -> Color(0xFF00ADD8)
    "swift" -> Color(0xFFFFAC45)
    "c++" -> Color(0xFFF34B7D)
    "c#" -> Color(0xFF178600)
    "ruby" -> Color(0xFF701516)
    "php" -> Color(0xFF4F5D95)
    else -> Color(0xFF8B949E)
}
