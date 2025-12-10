package com.desafio.githubexplorer.presentation

import com.desafio.githubexplorer.TestFixtures
import com.desafio.githubexplorer.core.presentation.ViewResource
import com.desafio.shared.data.dto.PullRequest
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.data.dto.SearchResult
import com.desafio.shared.data.dto.User
import com.desafio.shared.usecase.GithubReposUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeUseCase: FakeWelcomeUseCase
    private lateinit var viewModel: WelcomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeWelcomeUseCase()
        viewModel = WelcomeViewModel(fakeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_hasNullResults() {
        assertEquals(null, viewModel.currentState.searchResult)
        assertEquals(null, viewModel.currentState.userRepos)
    }

    @Test
    fun getPopularRepos_emitsSuccess() = runTest {
        viewModel.intent(ViewIntent.GetPopularRepos())
        advanceUntilIdle()

        val result = viewModel.currentState.searchResult
        assertTrue(result is ViewResource.Success)
        assertEquals(100, (result as ViewResource.Success).data.totalCount)
    }

    @Test
    fun getPopularRepos_withLanguage() = runTest {
        viewModel.intent(ViewIntent.GetPopularRepos(language = "rust"))
        advanceUntilIdle()

        assertEquals("rust", fakeUseCase.lastLanguage)
    }

    @Test
    fun searchRepos_emitsSuccess() = runTest {
        viewModel.intent(ViewIntent.SearchRepos(query = "android"))
        advanceUntilIdle()

        assertTrue(viewModel.currentState.searchResult is ViewResource.Success)
    }

    @Test
    fun getUserRepos_emitsSuccess() = runTest {
        viewModel.intent(ViewIntent.GetUserRepos(username = "octocat"))
        advanceUntilIdle()

        val result = viewModel.currentState.userRepos
        assertTrue(result is ViewResource.Success)
        assertEquals(3, (result as ViewResource.Success).data.size)
    }

    @Test
    fun getPopularRepos_onError_emitsError() = runTest {
        fakeUseCase.shouldFail = true

        viewModel.intent(ViewIntent.GetPopularRepos())
        advanceUntilIdle()

        assertTrue(viewModel.currentState.searchResult is ViewResource.Error)
    }
}

private class FakeWelcomeUseCase : GithubReposUseCase {

    var shouldFail = false
    var lastLanguage: String? = null

    override fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow {
        if (shouldFail) throw RuntimeException("error")
        emit(TestFixtures.createSearchResult())
    }

    override fun getPopularRepositories(
        language: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow {
        lastLanguage = language
        if (shouldFail) throw RuntimeException("error")
        emit(TestFixtures.createSearchResult())
    }

    override fun getUserRepositories(
        username: String,
        type: String,
        sort: String,
        direction: String,
        perPage: Int,
        page: Int
    ): Flow<List<Repo>> = flow {
        if (shouldFail) throw RuntimeException("error")
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
        emit(User(1L, username, "https://github.com/$username.png", "https://github.com/$username",
            username, null, null, null, null, null, 10, 5, 100, 50, null, null, "User"))
    }

    override fun listPublicRepositories(since: Long?, perPage: Int): Flow<List<Repo>> = flow {
        emit(listOf(TestFixtures.createRepo()))
    }
}
