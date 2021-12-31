package com.benkolera.electricbrain.fetchers

import com.benkolera.electricbrain.backend.generated.types.Tag
import com.benkolera.electricbrain.services.TagsService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery

@DgsComponent
class TagsDataFetcher(val tagsService: TagsService) {
    @DgsQuery
    fun tags(): List<Tag> {
        return tagsService.listTags()
    }
}