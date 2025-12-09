package com.desafio.shared.data.mapper

import com.desafio.shared.data.dto.User
import com.desafio.shared.model.UserResponse

internal object UserMapper : BaseMapper<UserResponse, User>() {

    override fun transformTo(source: UserResponse): User =
        User(
            id = source.id,
            login = source.login,
            name = source.name,
            avatarUrl = source.avatarUrl,
            htmlUrl = source.htmlUrl,
            company = source.company,
            blog = source.blog,
            location = source.location,
            email = source.email,
            bio = source.bio,
            publicRepos = source.publicRepos,
            publicGists = source.publicGists,
            followers = source.followers,
            following = source.following,
            createdAt = source.createdAt,
            updatedAt = source.updatedAt,
            type = source.type
        )
}
