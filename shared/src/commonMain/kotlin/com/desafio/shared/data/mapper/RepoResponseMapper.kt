package com.desafio.shared.data.mapper

import com.desafio.shared.data.dto.Repo
import com.desafio.shared.model.RepoResponse

internal object RepoMapper : BaseMapper<RepoResponse, Repo>() {

    override fun transformTo(source: RepoResponse): Repo =
        Repo(
            id = source.id,
            name = source.name,
            fullName = source.fullName,
            description = source.description,
            htmlUrl = source.htmlUrl,
            language = source.language,
            stargazersCount = source.stargazersCount,
            watchersCount = source.watchersCount,
            forksCount = source.forksCount,
            openIssuesCount = source.openIssuesCount,
            owner = OwnerMapper.transformTo(source.owner),
            createdAt = source.createdAt,
            updatedAt = source.updatedAt,
            pushedAt = source.pushedAt,
            isPrivate = source.private,
            isFork = source.fork,
            defaultBranch = source.defaultBranch,
            topics = source.topics
        )
}
