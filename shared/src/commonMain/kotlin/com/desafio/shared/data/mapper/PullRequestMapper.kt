package com.desafio.shared.data.mapper

import com.desafio.shared.data.dto.PullRequest
import com.desafio.shared.model.PullRequestResponse

internal object PullRequestMapper : BaseMapper<PullRequestResponse, PullRequest>() {

    override fun transformTo(source: PullRequestResponse): PullRequest =
        PullRequest(
            id = source.id,
            number = source.number,
            state = source.state,
            title = source.title,
            body = source.body,
            htmlUrl = source.htmlUrl,
            user = OwnerMapper.transformTo(source.user),
            createdAt = source.createdAt,
            updatedAt = source.updatedAt,
            closedAt = source.closedAt,
            mergedAt = source.mergedAt,
            isDraft = source.draft
        )
}
