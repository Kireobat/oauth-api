package eu.kireobat.oauthapi.api.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CreateReactionDto (
    @Schema(description = "Id of the blog to react to", required = true)
    val blogId: Int = 0,
    @Schema(description = "The reaction", required = true)
    val reaction: String = ""
)