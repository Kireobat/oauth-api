package eu.kireobat.oauthapi.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import eu.kireobat.oauthapi.persistence.entity.BlogEntity
import java.time.ZonedDateTime

data class BlogDto (
    val id: Int,
    val createdBy: UserDto,
    val title: String?,
    val description: String?,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val createdTime: ZonedDateTime,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val editedTime: ZonedDateTime?,
    var reactions: List<ReactionDto>,
    val topic: TopicDto?
) {
    fun toBlogEntity(): BlogEntity {
        return BlogEntity(
            id = this.id,
            createdBy = this.createdBy.toUserEntity(),
            title = this.title,
            description = this.description,
            createdTime = this.createdTime,
            editedTime = this.editedTime,
            topic = if (this.topic != null) {
                        this.topic.toTopicEntity()
                    } else {
                        null
                    }
        )
    }
}