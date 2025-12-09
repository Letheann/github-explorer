package com.desafio.shared.data.dto

data class SearchResult(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<Repo>
)
