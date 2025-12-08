package com.desafio.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class OwnerResponse(
    val login: String,
    val avatar_url: String
)
