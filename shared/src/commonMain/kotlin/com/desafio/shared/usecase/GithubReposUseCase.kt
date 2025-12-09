package com.desafio.shared.usecase

import com.desafio.shared.data.dto.PullRequest
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.data.dto.SearchResult
import com.desafio.shared.data.dto.User
import com.desafio.shared.repository.GithubReposRepository
import kotlinx.coroutines.flow.Flow

internal class GithubReposUseCaseImpl(
    private val repository: GithubReposRepository
) : GithubReposUseCase {

    override fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = repository.searchRepositories(query, sort, order, perPage, page)

    override fun getPopularRepositories(
        language: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = repository.getPopularRepositories(language, perPage, page)

    override fun getUserRepositories(
        username: String,
        type: String,
        sort: String,
        direction: String,
        perPage: Int,
        page: Int
    ): Flow<List<Repo>> = repository.getUserRepositories(username, type, sort, direction, perPage, page)

    override fun getRepository(owner: String, repo: String): Flow<Repo> =
        repository.getRepository(owner, repo)

    override fun getPullRequests(
        owner: String,
        repo: String,
        state: String,
        perPage: Int,
        page: Int
    ): Flow<List<PullRequest>> = repository.getPullRequests(owner, repo, state, perPage, page)

    override fun getUser(username: String): Flow<User> = repository.getUser(username)

    override fun listPublicRepositories(since: Long?, perPage: Int): Flow<List<Repo>> =
        repository.listPublicRepositories(since, perPage)
}

interface GithubReposUseCase {
    fun searchRepositories(
        query: String,
        sort: String = "stars",
        order: String = "desc",
        perPage: Int = 30,
        page: Int = 1
    ): Flow<SearchResult>

    fun getPopularRepositories(
        language: String,
        perPage: Int = 30,
        page: Int = 1
    ): Flow<SearchResult>

    fun getUserRepositories(
        username: String,
        type: String = "owner",
        sort: String = "updated",
        direction: String = "desc",
        perPage: Int = 30,
        page: Int = 1
    ): Flow<List<Repo>>

    fun getRepository(owner: String, repo: String): Flow<Repo>

    fun getPullRequests(
        owner: String,
        repo: String,
        state: String = "open",
        perPage: Int = 30,
        page: Int = 1
    ): Flow<List<PullRequest>>

    fun getUser(username: String): Flow<User>

    fun listPublicRepositories(since: Long? = null, perPage: Int = 30): Flow<List<Repo>>
}
