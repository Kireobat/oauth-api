package eu.kireobat.oauthapi.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime

data class ReactionDto (
    val id: Int,
    val user: UserDto,
    val blogId: Int,
    val reaction: String,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val createdTime: ZonedDateTime,
)