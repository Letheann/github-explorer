package com.desafio.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class RepoResponse(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val owner: OwnerResponse,
    val updated_at: String
)