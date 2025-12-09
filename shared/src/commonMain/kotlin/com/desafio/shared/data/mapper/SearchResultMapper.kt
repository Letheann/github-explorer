package com.desafio.shared.data.mapper

import com.desafio.shared.data.dto.SearchResult
import com.desafio.shared.model.SearchResponse

internal object SearchResultMapper : BaseMapper<SearchResponse, SearchResult>() {

    override fun transformTo(source: SearchResponse): SearchResult =
        SearchResult(
            totalCount = source.totalCount,
            incompleteResults = source.incompleteResults,
            items = source.items.map { RepoMapper.transformTo(it) }
        )
}
