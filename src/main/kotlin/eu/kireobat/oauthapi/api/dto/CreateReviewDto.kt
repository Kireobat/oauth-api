package eu.kireobat.oauthapi.api.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CreateReviewDto (
    @Schema(example = "My title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    val title: String,
    @Schema(example = "My review's description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    val description: String,
    @Schema(example = "5", pattern = "^(0|1|2|3|4|5)$", requiredMode = Schema.RequiredMode.REQUIRED)
    val rating: Int
)