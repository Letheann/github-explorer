package com.desafio.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoResponse(
    val id: Long,
    val name: String,
    @SerialName("full_name")
    val fullName: String,
    val description: String? = null,
    @SerialName("html_url")
    val htmlUrl: String,
    val language: String? = null,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    val owner: OwnerResponse,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("pushed_at")
    val pushedAt: String? = null,
    val private: Boolean = false,
    val fork: Boolean = false,
    @SerialName("default_branch")
    val defaultBranch: String? = null,
    val topics: List<String>? = null
)
