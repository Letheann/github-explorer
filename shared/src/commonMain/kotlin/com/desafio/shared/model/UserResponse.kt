package com.desafio.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long,
    val login: String,
    val name: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    val bio: String? = null,
    @SerialName("public_repos")
    val publicRepos: Int = 0,
    @SerialName("public_gists")
    val publicGists: Int = 0,
    val followers: Int = 0,
    val following: Int = 0,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    val type: String? = null
)
