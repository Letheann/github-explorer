package com.desafio.shared.data.dto

data class Repo(
    val id: Long?,
    val name: String?,
    val full_name: String?,
    val description: String?,
    val stargazers_count: Int?,
    val forks_count: Int?,
    val owner: Owner?,
    val updated_at: String?
)