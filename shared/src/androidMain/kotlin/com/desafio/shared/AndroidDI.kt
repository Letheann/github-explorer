package com.desafio.shared

import com.desafio.shared.repository.GithubReposRepository
import com.desafio.shared.repository.GithubReposRepositoryImpl
import com.desafio.shared.usecase.GithubReposUseCase
import com.desafio.shared.usecase.GithubReposUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

object AndroidDI {
    val shared = module {
        factory {
            HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
        factory<GithubReposRepository> { GithubReposRepositoryImpl(get()) }
        factory<GithubReposUseCase> { GithubReposUseCaseImpl(get()) }
    }
}