package com.desafio.shared.data.mapper

import com.desafio.shared.model.OwnerResponse
import com.desafio.shared.model.RepoResponse
import com.desafio.shared.model.SearchResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchResultMapperTest {

    private val ownerResponse = OwnerResponse(
        id = 1L,
        login = "octocat",
        avatarUrl = "https://github.com/images/octocat.png",
        htmlUrl = "https://github.com/octocat",
        type = "User"
    )

    private fun createRepoResponse(id: Long, name: String) = RepoResponse(
        id = id,
        name = name,
        fullName = "octocat/$name",
        description = "Description for $name",
        htmlUrl = "https://github.com/octocat/$name",
        language = "Kotlin",
        stargazersCount = 100,
        watchersCount = 50,
        forksCount = 25,
        openIssuesCount = 5,
        owner = ownerResponse,
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = "2024-12-01T00:00:00Z",
        pushedAt = null,
        private = false,
        fork = false,
        defaultBranch = "main",
        topics = null
    )

    @Test
    fun transformTo_mapsSearchResult() {
        val response = SearchResponse(
            totalCount = 100,
            incompleteResults = false,
            items = listOf(
                createRepoResponse(1L, "repo-1"),
                createRepoResponse(2L, "repo-2"),
                createRepoResponse(3L, "repo-3")
            )
        )

        val result = SearchResultMapper.transformTo(response)

        assertEquals(100, result.totalCount)
        assertFalse(result.incompleteResults)
        assertEquals(3, result.items.size)
    }

    @Test
    fun transformTo_mapsAllItems() {
        val response = SearchResponse(
            totalCount = 2,
            incompleteResults = false,
            items = listOf(
                createRepoResponse(1L, "first-repo"),
                createRepoResponse(2L, "second-repo")
            )
        )

        val result = SearchResultMapper.transformTo(response)

        assertEquals("first-repo", result.items[0].name)
        assertEquals("second-repo", result.items[1].name)
        assertEquals("octocat/first-repo", result.items[0].fullName)
        assertEquals("octocat/second-repo", result.items[1].fullName)
    }

    @Test
    fun transformTo_emptyItems() {
        val response = SearchResponse(
            totalCount = 0,
            incompleteResults = false,
            items = emptyList()
        )

        val result = SearchResultMapper.transformTo(response)

        assertEquals(0, result.totalCount)
        assertTrue(result.items.isEmpty())
    }

    @Test
    fun transformTo_incompleteResults() {
        val response = SearchResponse(
            totalCount = 1000,
            incompleteResults = true,
            items = listOf(createRepoResponse(1L, "partial-repo"))
        )

        val result = SearchResultMapper.transformTo(response)

        assertTrue(result.incompleteResults)
    }

    @Test
    fun transformTo_preservesOrder() {
        val response = SearchResponse(
            totalCount = 3,
            incompleteResults = false,
            items = listOf(
                createRepoResponse(3L, "third"),
                createRepoResponse(1L, "first"),
                createRepoResponse(2L, "second")
            )
        )

        val result = SearchResultMapper.transformTo(response)

        assertEquals(3L, result.items[0].id)
        assertEquals(1L, result.items[1].id)
        assertEquals(2L, result.items[2].id)
    }
}
