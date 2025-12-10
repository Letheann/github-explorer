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

enum class SortOption(val displayName: String, val apiValue: String) {
    STARS("Stars", "stars"),
    FORKS("Forks", "forks"),
    UPDATED("Last Updated", "updated")
}

class GithubViewModel(
    private val useCase: GithubReposUseCase
) : BaseMviViewModel<GithubIntent, GithubState, GithubEffect>() {

    override fun initialState(): GithubState = GithubState()

    override fun intent(intent: GithubIntent) {
        viewModelScope.launch {
            when (intent) {
                is GithubIntent.LoadRepositories -> loadRepositories(intent.page)
                is GithubIntent.SearchByLanguage -> searchByLanguage(intent.language)
                is GithubIntent.ChangeSortOption -> changeSortOption(intent.sortOption)
                is GithubIntent.LoadRepositoryDetail -> loadRepositoryDetail(intent.owner, intent.repo)
                is GithubIntent.ClearDetail -> setState { copy(selectedRepository = null) }
            }
        }
    }

    private suspend fun loadRepositories(page: Int) {
        val language = currentState.searchLanguage.ifBlank { "kotlin" }
        val query = "language:$language"

        useCase.searchRepositories(
            query = query,
            sort = currentState.sortOption.apiValue,
            order = "desc",
            perPage = 30,
            page = page
        ).onStart {
            setState { copy(repositoriesResult = ViewResource.Loading()) }
        }.catch {
            setState { copy(repositoriesResult = ViewResource.Error()) }
        }.collect { result ->
            setState { copy(repositoriesResult = ViewResource.Success(data = result)) }
        }
    }

    private fun searchByLanguage(language: String) {
        setState { copy(searchLanguage = language) }
        intent(GithubIntent.LoadRepositories())
    }

    private fun changeSortOption(sortOption: SortOption) {
        setState { copy(sortOption = sortOption) }
        intent(GithubIntent.LoadRepositories())
    }

    private suspend fun loadRepositoryDetail(owner: String, repo: String) {
        useCase.getRepository(owner, repo)
            .onStart {
                setState { copy(selectedRepository = ViewResource.Loading()) }
            }.catch {
                setState { copy(selectedRepository = ViewResource.Error()) }
            }.collect { repoData ->
                setState { copy(selectedRepository = ViewResource.Success(data = repoData)) }
            }
    }
}

sealed class GithubIntent : BaseMviViewModel.BaseViewIntent {
    data class LoadRepositories(val page: Int = 1) : GithubIntent()
    data class SearchByLanguage(val language: String) : GithubIntent()
    data class ChangeSortOption(val sortOption: SortOption) : GithubIntent()
    data class LoadRepositoryDetail(val owner: String, val repo: String) : GithubIntent()
    data object ClearDetail : GithubIntent()
}

data class GithubState(
    val repositoriesResult: ViewResource<SearchResult>? = null,
    val selectedRepository: ViewResource<Repo>? = null,
    val searchLanguage: String = "",
    val sortOption: SortOption = SortOption.STARS
) : BaseMviViewModel.BaseViewState

sealed class GithubEffect : BaseMviViewModel.BaseViewEffect
