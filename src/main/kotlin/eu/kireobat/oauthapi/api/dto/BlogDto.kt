package eu.kireobat.oauthapi.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import eu.kireobat.oauthapi.persistence.entity.BlogEntity
import java.time.ZonedDateTime

data class BlogDto (
    val id: Int,
    val user: UserDto,
    val title: String?,
    val body: String?,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val createdTime: ZonedDateTime,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val latestEditTime: ZonedDateTime?,
    var reactions: List<ReactionDto>
) {
    fun toBlogEntity(): BlogEntity {
        return BlogEntity(
            id = this.id,
            user = this.user.toUserEntity(),
            title = this.title,
            body = this.body,
            createdTime = this.createdTime,
            latestEditTime = this.latestEditTime
        )
    }
}