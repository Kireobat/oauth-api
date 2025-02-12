package eu.kireobat.oauthapi.api.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CreateBlogDto (
    @Schema(example = "My title", required = true)
    val title: String,
    @Schema(example = "My blog's description", required = true)
    val description: String
)