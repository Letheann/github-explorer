package com.desafio.shared.repository

import com.desafio.shared.data.NetworkResponse
import com.desafio.shared.data.dto.Repo
import com.desafio.shared.data.mapper.RepoResponseMapper
import com.desafio.shared.model.RepoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class GithubReposRepositoryImpl(
    private val client: HttpClient
) : GithubReposRepository {

    override fun getRepos(): Flow<List<Repo>> = flow {
        when (val response = fetchRepos()) {
            is NetworkResponse.Success -> {
                emit(response.data.map { RepoResponseMapper.transformTo(it) })
            }

            is NetworkResponse.Error -> {
                throw response.throwable
            }
        }
    }


    private suspend fun fetchRepos(): NetworkResponse<List<RepoResponse>> {
        return try {
            val result: List<RepoResponse> =
                client.get("https://api.github.com/repositories").body()
            NetworkResponse.Success(result)
        } catch (e: Exception) {
            NetworkResponse.Error(e)
        }
    }
}


internal interface GithubReposRepository {
    fun getRepos(): Flow<List<Repo>>
}
