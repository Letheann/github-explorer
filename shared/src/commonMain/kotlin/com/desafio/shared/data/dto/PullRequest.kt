package com.desafio.shared.data.dto

data class PullRequest(
    val id: Long,
    val number: Int,
    val state: String,
    val title: String,
    val body: String?,
    val htmlUrl: String,
    val user: Owner,
    val createdAt: String,
    val updatedAt: String,
    val closedAt: String?,
    val mergedAt: String?,
    val isDraft: Boolean
)
