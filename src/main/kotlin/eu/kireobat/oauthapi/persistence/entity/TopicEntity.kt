package eu.kireobat.oauthapi.persistence.entity

import eu.kireobat.oauthapi.api.dto.BlogDto
import eu.kireobat.oauthapi.api.dto.TopicDto
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "topics")
data class TopicEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "topicsSeq")
    @SequenceGenerator(name = "topicsSeq", sequenceName = "topics_seq", allocationSize = 1)
    @Column(name="id")
    val id: Int = 0,
    @Column(name="title")
    var title: String = "",
    @Column(name="description")
    var description: String = "",
    @Column(name="image_url")
    var imageUrl: String = "",
    @Column(name="created_time")
    val createdTime: ZonedDateTime = ZonedDateTime.now(),
) {
    fun toTopicDto(): TopicDto {
        return TopicDto(
            id = this.id,
            title = this.title,
            description = this.description,
            imageUrl = this.imageUrl,
            createdTime = this.createdTime
        )
    }
}
