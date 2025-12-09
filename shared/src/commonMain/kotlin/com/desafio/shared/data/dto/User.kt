package com.desafio.shared.data.dto

data class User(
    val id: Long,
    val login: String,
    val name: String?,
    val avatarUrl: String,
    val htmlUrl: String,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val createdAt: String?,
    val updatedAt: String?,
    val type: String?
)
