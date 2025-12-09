package com.desafio.shared.repository

import com.desafio.shared.data.NetworkResponse
import com.desafio.shared.data.api.GithubApi
import com.desafio.shared.data.dto.PullRequest
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.data.dto.SearchResult
import com.desafio.shared.data.dto.User
import com.desafio.shared.data.mapper.PullRequestMapper
import com.desafio.shared.data.mapper.RepoMapper
import com.desafio.shared.data.mapper.SearchResultMapper
import com.desafio.shared.data.mapper.UserMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GithubReposRepositoryImpl(
    private val api: GithubApi
) : GithubReposRepository {

    override fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow {
        when (val response = safeApiCall {
            api.searchRepositories(query, sort, order, perPage, page)
        }) {
            is NetworkResponse.Success -> emit(SearchResultMapper.transformTo(response.data))
            is NetworkResponse.Error -> throw response.throwable
        }
    }

    override fun getPopularRepositories(
        language: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow {
        when (val response = safeApiCall {
            api.getPopularRepositories(language, perPage, page)
        }) {
            is NetworkResponse.Success -> emit(SearchResultMapper.transformTo(response.data))
            is NetworkResponse.Error -> throw response.throwable
        }
    }

    override fun getUserRepositories(
        username: String,
        type: String,
        sort: String,
        direction: String,
        perPage: Int,
        page: Int
    ): Flow<List<Repo>> = flow {
        when (val response = safeApiCall {
            api.getUserRepositories(username, type, sort, direction, perPage, page)
        }) {
            is NetworkResponse.Success -> emit(response.data.map { RepoMapper.transformTo(it) })
            is NetworkResponse.Error -> throw response.throwable
        }
    }

    override fun getRepository(owner: String, repo: String): Flow<Repo> = flow {
        when (val response = safeApiCall { api.getRepository(owner, repo) }) {
            is NetworkResponse.Success -> emit(RepoMapper.transformTo(response.data))
            is NetworkResponse.Error -> throw response.throwable
        }
    }

    override fun getPullRequests(
        owner: String,
        repo: String,
        state: String,
        perPage: Int,
        page: Int
    ): Flow<List<PullRequest>> = flow {
        when (val response = safeApiCall {
            api.getPullRequests(owner, repo, state, perPage = perPage, page = page)
        }) {
            is NetworkResponse.Success -> emit(response.data.map { PullRequestMapper.transformTo(it) })
            is NetworkResponse.Error -> throw response.throwable
        }
    }

    override fun getUser(username: String): Flow<User> = flow {
        when (val response = safeApiCall { api.getUser(username) }) {
            is NetworkResponse.Success -> emit(UserMapper.transformTo(response.data))
            is NetworkResponse.Error -> throw response.throwable
        }
    }

    override fun listPublicRepositories(since: Long?, perPage: Int): Flow<List<Repo>> = flow {
        when (val response = safeApiCall { api.listPublicRepositories(since, perPage) }) {
            is NetworkResponse.Success -> emit(response.data.map { RepoMapper.transformTo(it) })
            is NetworkResponse.Error -> throw response.throwable
        }
    }

    private suspend fun <T> safeApiCall(call: suspend () -> T): NetworkResponse<T> {
        return try {
            NetworkResponse.Success(call())
        } catch (e: Exception) {
            NetworkResponse.Error(e)
        }
    }
}

interface GithubReposRepository {
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
