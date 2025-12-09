package com.desafio.shared.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PullRequestResponse(
    val id: Long,
    val number: Int,
    val state: String,
    val title: String,
    val body: String? = null,
    @SerialName("html_url")
    val htmlUrl: String,
    val user: OwnerResponse,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("closed_at")
    val closedAt: String? = null,
    @SerialName("merged_at")
    val mergedAt: String? = null,
    val draft: Boolean = false,
    val head: BranchResponse? = null,
    val base: BranchResponse? = null
)

@Serializable
data class BranchResponse(
    val ref: String,
    val sha: String,
    val label: String? = null
)
