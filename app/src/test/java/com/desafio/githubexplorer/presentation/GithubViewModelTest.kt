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
class GithubViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeUseCase: FakeUseCase
    private lateinit var viewModel: GithubViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeUseCase()
        viewModel = GithubViewModel(fakeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_hasNullResults() {
        assertEquals(null, viewModel.currentState.repositoriesResult)
        assertEquals(null, viewModel.currentState.selectedRepository)
        assertEquals("", viewModel.currentState.searchLanguage)
        assertEquals(SortOption.STARS, viewModel.currentState.sortOption)
    }

    @Test
    fun loadRepositories_emitsSuccess() = runTest {
        viewModel.intent(GithubIntent.LoadRepositories())
        advanceUntilIdle()

        val result = viewModel.currentState.repositoriesResult
        assertTrue(result is ViewResource.Success)
        assertEquals(100, (result as ViewResource.Success).data.totalCount)
    }

    @Test
    fun loadRepositories_onError_emitsError() = runTest {
        fakeUseCase.shouldFail = true

        viewModel.intent(GithubIntent.LoadRepositories())
        advanceUntilIdle()

        assertTrue(viewModel.currentState.repositoriesResult is ViewResource.Error)
    }

    @Test
    fun searchByLanguage_updatesStateAndReloads() = runTest {
        viewModel.intent(GithubIntent.SearchByLanguage("rust"))
        advanceUntilIdle()

        assertEquals("rust", viewModel.currentState.searchLanguage)
        assertTrue(viewModel.currentState.repositoriesResult is ViewResource.Success)
    }

    @Test
    fun changeSortOption_updatesStateAndReloads() = runTest {
        viewModel.intent(GithubIntent.ChangeSortOption(SortOption.FORKS))
        advanceUntilIdle()

        assertEquals(SortOption.FORKS, viewModel.currentState.sortOption)
        assertTrue(viewModel.currentState.repositoriesResult is ViewResource.Success)
    }

    @Test
    fun loadRepositoryDetail_emitsSuccess() = runTest {
        viewModel.intent(GithubIntent.LoadRepositoryDetail("octocat", "hello-world"))
        advanceUntilIdle()

        val result = viewModel.currentState.selectedRepository
        assertTrue(result is ViewResource.Success)
        assertEquals("hello-world", (result as ViewResource.Success).data.name)
    }

    @Test
    fun clearDetail_clearsSelectedRepository() = runTest {
        viewModel.intent(GithubIntent.LoadRepositoryDetail("octocat", "hello-world"))
        advanceUntilIdle()

        viewModel.intent(GithubIntent.ClearDetail)
        advanceUntilIdle()

        assertEquals(null, viewModel.currentState.selectedRepository)
    }

    @Test
    fun emptyLanguage_defaultsToKotlin() = runTest {
        viewModel.intent(GithubIntent.LoadRepositories())
        advanceUntilIdle()

        assertEquals("language:kotlin", fakeUseCase.lastQuery)
    }

    @Test
    fun customLanguage_usedInQuery() = runTest {
        viewModel.intent(GithubIntent.SearchByLanguage("python"))
        advanceUntilIdle()

        assertEquals("language:python", fakeUseCase.lastQuery)
    }

    @Test
    fun sortOption_passedToUseCase() = runTest {
        viewModel.intent(GithubIntent.ChangeSortOption(SortOption.UPDATED))
        advanceUntilIdle()

        assertEquals("updated", fakeUseCase.lastSort)
    }
}

private class FakeUseCase : GithubReposUseCase {

    var shouldFail = false
    var lastQuery: String? = null
    var lastSort: String? = null

    override fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow {
        lastQuery = query
        lastSort = sort
        if (shouldFail) throw RuntimeException("error")
        emit(TestFixtures.createSearchResult())
    }

    override fun getPopularRepositories(
        language: String,
        perPage: Int,
        page: Int
    ): Flow<SearchResult> = flow {
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
    ): Flow<List<Repo>> = flow { emit(listOf(TestFixtures.createRepo())) }

    override fun getRepository(owner: String, repo: String): Flow<Repo> = flow {
        if (shouldFail) throw RuntimeException("error")
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
