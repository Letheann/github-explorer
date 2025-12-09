package com.desafio.githubexplorer.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import com.desafio.githubexplorer.presentation.compose.RecyclerCompose
import com.desafio.githubexplorer.core.theme.SimpleloginTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeActivity : AppCompatActivity() {

    private val viewModel: WelcomeViewModel by viewModel<WelcomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleloginTheme {
                RecyclerCompose(
                    viewModel = viewModel,
                    invokeClick = { owner, repo ->
                        viewModel.intent(ViewIntent.OnClickCard(owner, repo))
                    }
                )
            }
        }
        LifecycleRegistry(this).currentState = Lifecycle.State.CREATED
        handleViewEffect()
    }

    private fun handleViewEffect() = lifecycleScope.launch {
        viewModel.viewEffect.collect { effect ->
            when (effect) {
                is ViewEffect.NavigateToDetail -> {
                }
            }
        }
    }
}
