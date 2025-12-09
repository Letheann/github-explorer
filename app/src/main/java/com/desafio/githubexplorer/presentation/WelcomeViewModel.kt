package com.desafio.githubexplorer.presentation

import androidx.lifecycle.viewModelScope
import com.desafio.githubexplorer.core.presentation.BaseMviViewModel
import com.desafio.githubexplorer.core.presentation.ViewResource
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.usecase.GithubReposUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WelcomeViewModel(private val useCase: GithubReposUseCase) :
    BaseMviViewModel<ViewIntent, ViewState, ViewEffect>() {
    override fun initialState(): ViewState = ViewState()

    override fun intent(intent: ViewIntent) {
        viewModelScope.launch {
            when (intent) {
                is ViewIntent.GetRepos -> {
                    useCase.getRepos().onStart {
                        setState {
                            copy(items = ViewResource.Loading())
                        }
                    }.catch {
                        setState {
                            copy(items = ViewResource.Error())
                        }
                    }.collect {
                        setState {
                            copy(items = ViewResource.Success(data = it))
                        }
                    }
                }

                is ViewIntent.OnClickCard -> {
                    setEffect { ViewEffect.EffectToView }
                }
            }
        }
    }
}


sealed class ViewIntent : BaseMviViewModel.BaseViewIntent {
    data object GetRepos : ViewIntent()
    data class OnClickCard(val id: Long?) : ViewIntent()
}

data class ViewState(
    val items: ViewResource<List<Repo>>? = null
) : BaseMviViewModel.BaseViewState

sealed class ViewEffect : BaseMviViewModel.BaseViewEffect {
    data object EffectToView : ViewEffect()
}

