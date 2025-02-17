package eu.kireobat.oauthapi.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import eu.kireobat.oauthapi.persistence.entity.TopicEntity
import java.time.ZonedDateTime

data class TopicDto (
    val id : Number,
    val title : String,
    val description : String,
    val imageUrl : String,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val createdTime: ZonedDateTime,
) {
    fun toTopicEntity() : TopicEntity {
        return TopicEntity(
            id = id,
            title = title,
            description = description,
            imageUrl = imageUrl,
            createdTime = createdTime
        )
    }
}