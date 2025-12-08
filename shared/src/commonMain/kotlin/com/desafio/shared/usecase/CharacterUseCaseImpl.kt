package com.desafio.shared.usecase

import com.desafio.shared.data.dto.Repo
import com.desafio.shared.repository.GithubReposRepository
import kotlinx.coroutines.flow.Flow

internal class GithubReposUseCaseImpl(
    private val repository: GithubReposRepository
) : GithubReposUseCase {

    override fun getRepos(): Flow<List<Repo>> {
        return repository.getRepos()
    }
}

interface GithubReposUseCase {
    fun getRepos(): Flow<List<Repo>>
}
