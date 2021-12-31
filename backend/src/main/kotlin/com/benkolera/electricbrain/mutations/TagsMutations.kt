package com.benkolera.electricbrain.mutations

import com.benkolera.electricbrain.backend.generated.types.Tag
import com.benkolera.electricbrain.backend.generated.types.TagInput
import com.benkolera.electricbrain.services.TagsService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class TagsMutations(val tagsService: TagsService) {
    @DgsMutation
    fun addTag(@InputArgument("input") input: TagInput): Tag {
        return tagsService.addTag(input)
    }
}