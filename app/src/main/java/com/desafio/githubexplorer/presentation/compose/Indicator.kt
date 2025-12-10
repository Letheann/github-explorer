package com.desafio.githubexplorer.presentation.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.desafio.githubexplorer.core.theme.GithubAccent
import com.desafio.githubexplorer.core.theme.GithubBorder

@Composable
fun Indicator(
    size: Dp = 48.dp,
    sweepAngle: Float = 90f,
    color: Color = GithubAccent,
    strokeWidth: Dp = 4.dp
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val transition = rememberInfiniteTransition(label = "loading")

        val currentArcStartAngle by transition.animateValue(
            initialValue = 0,
            targetValue = 360,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1100, easing = LinearEasing)
            ),
            label = "arc"
        )

        val stroke = with(LocalDensity.current) {
            Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        }

        Canvas(
            Modifier
                .progressSemantics()
                .size(size)
                .padding(strokeWidth / 2)
        ) {
            drawCircle(GithubBorder, style = stroke)
            drawArc(
                color,
                startAngle = currentArcStartAngle.toFloat() - 90,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = stroke
            )
        }
    }
}
