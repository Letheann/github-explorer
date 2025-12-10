package com.desafio.shared

import com.desafio.shared.data.dto.Owner
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.data.dto.SearchResult
import com.desafio.shared.model.OwnerResponse
import com.desafio.shared.model.RepoResponse
import com.desafio.shared.model.SearchResponse

object TestFixtures {

    val ownerResponse = OwnerResponse(
        id = 1L,
        login = "octocat",
        avatarUrl = "https://github.com/images/octocat.png",
        htmlUrl = "https://github.com/octocat",
        type = "User"
    )

    val owner = Owner(
        id = 1L,
        login = "octocat",
        avatarUrl = "https://github.com/images/octocat.png",
        htmlUrl = "https://github.com/octocat",
        type = "User"
    )

    fun createRepoResponse(
        id: Long = 1L,
        name: String = "hello-world",
        stars: Int = 100
    ) = RepoResponse(
        id = id,
        name = name,
        fullName = "octocat/$name",
        description = "Description for $name",
        htmlUrl = "https://github.com/octocat/$name",
        language = "Kotlin",
        stargazersCount = stars,
        watchersCount = 50,
        forksCount = 25,
        openIssuesCount = 5,
        owner = ownerResponse,
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = "2024-12-01T00:00:00Z",
        pushedAt = "2024-12-01T12:00:00Z",
        private = false,
        fork = false,
        defaultBranch = "main",
        topics = listOf("kotlin", "android")
    )

    fun createRepo(
        id: Long = 1L,
        name: String = "hello-world",
        stars: Int = 100
    ) = Repo(
        id = id,
        name = name,
        fullName = "octocat/$name",
        description = "Description for $name",
        htmlUrl = "https://github.com/octocat/$name",
        language = "Kotlin",
        stargazersCount = stars,
        watchersCount = 50,
        forksCount = 25,
        openIssuesCount = 5,
        owner = owner,
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = "2024-12-01T00:00:00Z",
        pushedAt = "2024-12-01T12:00:00Z",
        isPrivate = false,
        isFork = false,
        defaultBranch = "main",
        topics = listOf("kotlin", "android")
    )

    fun createSearchResponse(totalCount: Int = 100, itemCount: Int = 3) = SearchResponse(
        totalCount = totalCount,
        incompleteResults = false,
        items = (1..itemCount).map { createRepoResponse(id = it.toLong(), name = "repo-$it") }
    )

    fun createSearchResult(totalCount: Int = 100, itemCount: Int = 3) = SearchResult(
        totalCount = totalCount,
        incompleteResults = false,
        items = (1..itemCount).map { createRepo(id = it.toLong(), name = "repo-$it") }
    )
}
