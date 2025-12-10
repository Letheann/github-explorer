package com.desafio.shared.data.mapper

import com.desafio.shared.model.OwnerResponse
import com.desafio.shared.model.RepoResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RepoMapperTest {

    private val ownerResponse = OwnerResponse(
        id = 1L,
        login = "octocat",
        avatarUrl = "https://github.com/images/octocat.png",
        htmlUrl = "https://github.com/octocat",
        type = "User"
    )

    @Test
    fun transformTo_mapsAllFields() {
        val response = RepoResponse(
            id = 100L,
            name = "hello-world",
            fullName = "octocat/hello-world",
            description = "A sample repository",
            htmlUrl = "https://github.com/octocat/hello-world",
            language = "Kotlin",
            stargazersCount = 1500,
            watchersCount = 1200,
            forksCount = 300,
            openIssuesCount = 15,
            owner = ownerResponse,
            createdAt = "2020-01-01T00:00:00Z",
            updatedAt = "2024-12-01T00:00:00Z",
            pushedAt = "2024-12-01T12:00:00Z",
            private = false,
            fork = false,
            defaultBranch = "main",
            topics = listOf("kotlin", "android")
        )

        val result = RepoMapper.transformTo(response)

        assertEquals(100L, result.id)
        assertEquals("hello-world", result.name)
        assertEquals("octocat/hello-world", result.fullName)
        assertEquals("A sample repository", result.description)
        assertEquals("https://github.com/octocat/hello-world", result.htmlUrl)
        assertEquals("Kotlin", result.language)
        assertEquals(1500, result.stargazersCount)
        assertEquals(1200, result.watchersCount)
        assertEquals(300, result.forksCount)
        assertEquals(15, result.openIssuesCount)
        assertEquals("octocat", result.owner.login)
        assertEquals("2020-01-01T00:00:00Z", result.createdAt)
        assertEquals("2024-12-01T00:00:00Z", result.updatedAt)
        assertEquals("2024-12-01T12:00:00Z", result.pushedAt)
        assertFalse(result.isPrivate)
        assertFalse(result.isFork)
        assertEquals("main", result.defaultBranch)
        assertEquals(listOf("kotlin", "android"), result.topics)
    }

    @Test
    fun transformTo_nullableFields() {
        val response = RepoResponse(
            id = 200L,
            name = "empty-repo",
            fullName = "octocat/empty-repo",
            description = null,
            htmlUrl = "https://github.com/octocat/empty-repo",
            language = null,
            stargazersCount = 0,
            watchersCount = 0,
            forksCount = 0,
            openIssuesCount = 0,
            owner = ownerResponse,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z",
            pushedAt = null,
            private = false,
            fork = false,
            defaultBranch = null,
            topics = null
        )

        val result = RepoMapper.transformTo(response)

        assertNull(result.description)
        assertNull(result.language)
        assertNull(result.pushedAt)
        assertNull(result.defaultBranch)
        assertNull(result.topics)
    }

    @Test
    fun transformTo_privateAndForkFlags() {
        val response = RepoResponse(
            id = 300L,
            name = "private-fork",
            fullName = "octocat/private-fork",
            description = null,
            htmlUrl = "https://github.com/octocat/private-fork",
            language = null,
            stargazersCount = 0,
            watchersCount = 0,
            forksCount = 0,
            openIssuesCount = 0,
            owner = ownerResponse,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z",
            pushedAt = null,
            private = true,
            fork = true,
            defaultBranch = null,
            topics = null
        )

        val result = RepoMapper.transformTo(response)

        assertTrue(result.isPrivate)
        assertTrue(result.isFork)
    }

    @Test
    fun transformTo_emptyTopics() {
        val response = RepoResponse(
            id = 400L,
            name = "no-topics",
            fullName = "octocat/no-topics",
            description = null,
            htmlUrl = "https://github.com/octocat/no-topics",
            language = null,
            stargazersCount = 0,
            watchersCount = 0,
            forksCount = 0,
            openIssuesCount = 0,
            owner = ownerResponse,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z",
            pushedAt = null,
            private = false,
            fork = false,
            defaultBranch = null,
            topics = emptyList()
        )

        val result = RepoMapper.transformTo(response)

        assertEquals(emptyList(), result.topics)
    }
}
