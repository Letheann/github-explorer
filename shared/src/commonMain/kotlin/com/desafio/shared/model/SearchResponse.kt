package com.desafio.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val total_count: Int,
    val items: List<RepoResponse>
)