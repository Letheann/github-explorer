package com.desafio.shared.data.mapper

import com.desafio.shared.data.dto.Repo
import com.desafio.shared.model.RepoResponse

internal object RepoResponseMapper : BaseMapper<RepoResponse, Repo>() {
    override fun transformTo(source: RepoResponse): Repo =
        Repo(
            id = source.id,
            name = source.name,
            full_name = source.full_name,
            description = source.description,
            stargazers_count = source.stargazers_count,
            forks_count = source.forks_count,
            owner = source.owner,
            updated_at = source.updated_at
        )
}
