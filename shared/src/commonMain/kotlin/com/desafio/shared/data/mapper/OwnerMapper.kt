package com.desafio.shared.data.mapper

import com.desafio.shared.data.dto.Owner
import com.desafio.shared.model.OwnerResponse

internal object OwnerMapper : BaseMapper<OwnerResponse, Owner>() {

    override fun transformTo(source: OwnerResponse): Owner =
        Owner(
            id = source.id,
            login = source.login,
            avatarUrl = source.avatarUrl,
            htmlUrl = source.htmlUrl,
            type = source.type
        )
}
