package com.desafio.githubexplorer.di

import com.desafio.shared.AndroidDI
import org.koin.dsl.module


object DI {
    private val presentation = module {

    }

    val modules = listOf(presentation, AndroidDI.shared)
}