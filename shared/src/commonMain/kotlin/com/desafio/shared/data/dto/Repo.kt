package com.desafio.shared.data.dto

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val htmlUrl: String,
    val language: String?,
    val stargazersCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val openIssuesCount: Int,
    val owner: Owner,
    val createdAt: String,
    val updatedAt: String,
    val pushedAt: String?,
    val isPrivate: Boolean,
    val isFork: Boolean,
    val defaultBranch: String?,
    val topics: List<String>?
)
