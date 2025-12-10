package com.desafio.githubexplorer.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GithubDarkColorScheme = darkColorScheme(
    primary = GithubAccent,
    onPrimary = GithubDark,
    primaryContainer = GithubDarkSecondary,
    onPrimaryContainer = GithubText,
    secondary = GithubPurple,
    onSecondary = GithubDark,
    secondaryContainer = GithubDarkSecondary,
    onSecondaryContainer = GithubText,
    tertiary = GithubGreen,
    onTertiary = GithubDark,
    tertiaryContainer = GithubDarkSecondary,
    onTertiaryContainer = GithubText,
    error = GithubRed,
    onError = GithubDark,
    errorContainer = GithubDarkSecondary,
    onErrorContainer = GithubRed,
    background = GithubDark,
    onBackground = GithubText,
    surface = GithubDarkSecondary,
    onSurface = GithubText,
    surfaceVariant = GithubBorder,
    onSurfaceVariant = GithubTextSecondary,
    outline = GithubBorder,
    outlineVariant = GithubBorder,
    inverseSurface = GithubText,
    inverseOnSurface = GithubDark,
    inversePrimary = GithubAccent,
    surfaceTint = GithubAccent
)

@Composable
fun GithubExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = GithubDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = GithubDark.toArgb()
            window.navigationBarColor = GithubDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun SimpleloginTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    GithubExplorerTheme(darkTheme = darkTheme, content = content)
}
