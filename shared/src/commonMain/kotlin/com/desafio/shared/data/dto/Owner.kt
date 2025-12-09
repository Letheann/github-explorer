package com.desafio.shared.data.dto

data class Owner(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val type: String?
)
