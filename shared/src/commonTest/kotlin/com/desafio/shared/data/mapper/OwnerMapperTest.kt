package com.desafio.shared.data.mapper

import com.desafio.shared.model.OwnerResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class OwnerMapperTest {

    @Test
    fun transformTo_mapsAllFields() {
        val response = OwnerResponse(
            id = 123L,
            login = "octocat",
            avatarUrl = "https://github.com/images/octocat.png",
            htmlUrl = "https://github.com/octocat",
            type = "User"
        )

        val result = OwnerMapper.transformTo(response)

        assertEquals(123L, result.id)
        assertEquals("octocat", result.login)
        assertEquals("https://github.com/images/octocat.png", result.avatarUrl)
        assertEquals("https://github.com/octocat", result.htmlUrl)
        assertEquals("User", result.type)
    }

    @Test
    fun transformTo_nullType() {
        val response = OwnerResponse(
            id = 456L,
            login = "testuser",
            avatarUrl = "https://github.com/images/test.png",
            htmlUrl = "https://github.com/testuser",
            type = null
        )

        val result = OwnerMapper.transformTo(response)

        assertEquals(null, result.type)
    }

    @Test
    fun transformTo_organizationType() {
        val response = OwnerResponse(
            id = 789L,
            login = "github",
            avatarUrl = "https://github.com/images/github.png",
            htmlUrl = "https://github.com/github",
            type = "Organization"
        )

        val result = OwnerMapper.transformTo(response)

        assertEquals("Organization", result.type)
    }
}
