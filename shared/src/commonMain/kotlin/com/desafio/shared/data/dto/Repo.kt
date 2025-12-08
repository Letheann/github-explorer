package com.desafio.shared.data.dto

import com.desafio.shared.model.OwnerResponse

data class Repo(
    val id: Long,
    val name: String,
    val full_name: String,
    val description: String?,
    val stargazers_count: Int,
    val forks_count: Int,
    val owner: OwnerResponse,
    val updated_at: String
)