package com.desafio.githubexplorer.presentation

import androidx.lifecycle.viewModelScope
import com.desafio.githubexplorer.core.presentation.BaseMviViewModel
import com.desafio.githubexplorer.core.presentation.ViewResource
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.data.dto.SearchResult
import com.desafio.shared.usecase.GithubReposUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val useCase: GithubReposUseCase
) : BaseMviViewModel<ViewIntent, ViewState, ViewEffect>() {

    override fun initialState(): ViewState = ViewState()

    override fun intent(intent: ViewIntent) {
        viewModelScope.launch {
            when (intent) {
                is ViewIntent.GetPopularRepos -> {
                    useCase.getPopularRepositories(
                        language = intent.language,
                        perPage = intent.perPage,
                        page = intent.page
                    ).onStart {
                        setState { copy(searchResult = ViewResource.Loading()) }
                    }.catch {
                        setState { copy(searchResult = ViewResource.Error()) }
                    }.collect { result ->
                        setState { copy(searchResult = ViewResource.Success(data = result)) }
                    }
                }

                is ViewIntent.SearchRepos -> {
                    useCase.searchRepositories(
                        query = intent.query,
                        sort = intent.sort,
                        order = intent.order,
                        perPage = intent.perPage,
                        page = intent.page
                    ).onStart {
                        setState { copy(searchResult = ViewResource.Loading()) }
                    }.catch {
                        setState { copy(searchResult = ViewResource.Error()) }
                    }.collect { result ->
                        setState { copy(searchResult = ViewResource.Success(data = result)) }
                    }
                }

                is ViewIntent.GetUserRepos -> {
                    useCase.getUserRepositories(
                        username = intent.username,
                        perPage = intent.perPage,
                        page = intent.page
                    ).onStart {
                        setState { copy(userRepos = ViewResource.Loading()) }
                    }.catch {
                        setState { copy(userRepos = ViewResource.Error()) }
                    }.collect { repos ->
                        setState { copy(userRepos = ViewResource.Success(data = repos)) }
                    }
                }

                is ViewIntent.OnClickCard -> {
                    setEffect { ViewEffect.NavigateToDetail(intent.owner, intent.repo) }
                }
            }
        }
    }
}

sealed class ViewIntent : BaseMviViewModel.BaseViewIntent {
    data class GetPopularRepos(
        val language: String = "kotlin",
        val perPage: Int = 30,
        val page: Int = 1
    ) : ViewIntent()

    data class SearchRepos(
        val query: String,
        val sort: String = "stars",
        val order: String = "desc",
        val perPage: Int = 30,
        val page: Int = 1
    ) : ViewIntent()

    data class GetUserRepos(
        val username: String,
        val perPage: Int = 30,
        val page: Int = 1
    ) : ViewIntent()

    data class OnClickCard(val owner: String, val repo: String) : ViewIntent()
}

data class ViewState(
    val searchResult: ViewResource<SearchResult>? = null,
    val userRepos: ViewResource<List<Repo>>? = null
) : BaseMviViewModel.BaseViewState

sealed class ViewEffect : BaseMviViewModel.BaseViewEffect {
    data class NavigateToDetail(val owner: String, val repo: String) : ViewEffect()
}
