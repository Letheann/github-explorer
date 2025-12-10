package com.desafio.githubexplorer.di

import com.desafio.githubexplorer.presentation.GithubViewModel
import com.desafio.githubexplorer.presentation.WelcomeViewModel
import com.desafio.shared.AndroidDI
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DI {
    private val presentation = module {
        viewModel { WelcomeViewModel(useCase = get()) }
        viewModel { GithubViewModel(useCase = get()) }
    }

    val modules = listOf(presentation, AndroidDI.shared)
}
