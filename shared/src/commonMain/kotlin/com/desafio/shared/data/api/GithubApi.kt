package com.desafio.shared.data.api

import com.desafio.shared.model.PullRequestResponse
import com.desafio.shared.model.RepoResponse
import com.desafio.shared.model.SearchResponse
import com.desafio.shared.model.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class GithubApi(
    private val client: HttpClient
) {
    companion object {
        private const val BASE_URL = "https://api.github.com"
    }

    suspend fun searchRepositories(
        query: String,
        sort: String = "stars",
        order: String = "desc",
        perPage: Int = 30,
        page: Int = 1
    ): SearchResponse {
        return client.get("$BASE_URL/search/repositories") {
            parameter("q", query)
            parameter("sort", sort)
            parameter("order", order)
            parameter("per_page", perPage)
            parameter("page", page)
        }.body()
    }

    suspend fun getPopularRepositories(
        language: String,
        perPage: Int = 30,
        page: Int = 1
    ): SearchResponse {
        return searchRepositories(
            query = "language:$language",
            sort = "stars",
            order = "desc",
            perPage = perPage,
            page = page
        )
    }

    suspend fun getUserRepositories(
        username: String,
        type: String = "owner",
        sort: String = "updated",
        direction: String = "desc",
        perPage: Int = 30,
        page: Int = 1
    ): List<RepoResponse> {
        return client.get("$BASE_URL/users/$username/repos") {
            parameter("type", type)
            parameter("sort", sort)
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("page", page)
        }.body()
    }

    suspend fun getRepository(owner: String, repo: String): RepoResponse {
        return client.get("$BASE_URL/repos/$owner/$repo").body()
    }

    suspend fun getPullRequests(
        owner: String,
        repo: String,
        state: String = "open",
        sort: String = "created",
        direction: String = "desc",
        perPage: Int = 30,
        page: Int = 1
    ): List<PullRequestResponse> {
        return client.get("$BASE_URL/repos/$owner/$repo/pulls") {
            parameter("state", state)
            parameter("sort", sort)
            parameter("direction", direction)
            parameter("per_page", perPage)
            parameter("page", page)
        }.body()
    }

    suspend fun getUser(username: String): UserResponse {
        return client.get("$BASE_URL/users/$username").body()
    }

    suspend fun listPublicRepositories(
        since: Long? = null,
        perPage: Int = 30
    ): List<RepoResponse> {
        return client.get("$BASE_URL/repositories") {
            since?.let { parameter("since", it) }
            parameter("per_page", perPage)
        }.body()
    }
}
