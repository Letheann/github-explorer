package com.desafio.shared.usecase

import com.desafio.shared.TestFixtures
import com.desafio.shared.data.dto.PullRequest
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.data.dto.SearchResult
import com.desafio.shared.data.dto.User
import com.desafio.shared.repository.GithubReposRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GithubReposUseCaseTest {

    private val repository = FakeRepository()
    private val useCase = GithubReposUseCaseImpl(repository)

    @Test
    fun searchRepositories_delegatesToRepository() = runTest {
        val result = useCase.searchRepositories("kotlin", "stars", "desc", 30, 1).first()

        assertEquals(100, result.totalCount)
        assertEquals(3, result.items.size)
    }

    @Test
    fun getPopularRepositories_delegatesToRepository() = runTest {
        val result = useCase.getPopularRepositories("kotlin", 30, 1).first()

        assertEquals(100, result.totalCount)
    }

    @Test
    fun getRepository_delegatesToRepository() = runTest {
        val result = useCase.getRepository("octocat", "hello-world").first()

        assertEquals("hello-world", result.name)
        assertEquals("octocat", result.owner.login)
    }

    @Test
    fun getUserRepositories_delegatesToRepository() = runTest {
        val result = useCase.getUserRepositories("octocat", perPage = 30, page = 1).first()

        assertEquals(3, result.size)
    }

    @Test
    fun listPublicRepositories_delegatesToRepository() = runTest {
        val result = useCase.listPublicRepositories(null, 30).first()

        assertEquals(3, result.size)
    }
}

private class FakeRepository : GithubReposRepository {

    override fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow { emit(TestFixtures.createSearchResult()) }

    override fun getPopularRepositories(
        language: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow { emit(TestFixtures.createSearchResult()) }

    override fun getUserRepositories(
        username: String,
        type: String,
        sort: String,
        direction: String,
        perPage: Int,
        page: Int
    ): Flow<List<Repo>> = flow {
        emit(listOf(
            TestFixtures.createRepo(1L, "repo-1"),
            TestFixtures.createRepo(2L, "repo-2"),
            TestFixtures.createRepo(3L, "repo-3")
        ))
    }

    override fun getRepository(owner: String, repo: String): Flow<Repo> = flow {
        emit(TestFixtures.createRepo(name = repo))
    }

    override fun getPullRequests(
        owner: String,
        repo: String,
        state: String,
        perPage: Int,
        page: Int
    ): Flow<List<PullRequest>> = flow { emit(emptyList()) }

    override fun getUser(username: String): Flow<User> = flow {
        emit(User(
            id = 1L,
            login = username,
            avatarUrl = "https://github.com/images/$username.png",
            htmlUrl = "https://github.com/$username",
            name = username,
            company = null,
            blog = null,
            location = null,
            email = null,
            bio = null,
            publicRepos = 10,
            publicGists = 5,
            followers = 100,
            following = 50,
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-12-01T00:00:00Z",
            type = "User"
        ))
    }

    override fun listPublicRepositories(since: Long?, perPage: Int): Flow<List<Repo>> = flow {
        emit(listOf(
            TestFixtures.createRepo(1L, "public-1"),
            TestFixtures.createRepo(2L, "public-2"),
            TestFixtures.createRepo(3L, "public-3")
        ))
    }
}
