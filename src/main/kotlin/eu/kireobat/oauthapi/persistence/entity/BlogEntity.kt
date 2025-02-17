package eu.kireobat.oauthapi.persistence.entity

import eu.kireobat.oauthapi.api.dto.BlogDto
import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "blogs")
data class BlogEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogsSeq")
    @SequenceGenerator(name = "blogsSeq", sequenceName = "blogs_seq", allocationSize = 1)
    @Column(name="id")
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name="created_by")
    val createdBy: UserEntity = UserEntity(),
    @Column(name="title")
    var title: String? = "",
    @Column(name="description")
    var description: String? = "",
    @Column(name="created_time")
    val createdTime: ZonedDateTime = ZonedDateTime.now(),
    @Column(name="edited_time")
    var editedTime: ZonedDateTime? = null,
    @ManyToOne
    @JoinColumn(name="topic_id")
    val topic: TopicEntity? = null,
) {
    fun toBlogDto(): BlogDto {
        return BlogDto(
            id = this.id,
            createdBy = this.createdBy.toUserDto(),
            title = this.title,
            description = this.description,
            createdTime = this.createdTime,
            editedTime = this.editedTime,
            reactions = mutableListOf(),
            topic = if (this.topic != null) {
                        this.topic.toTopicDto()
                    } else {
                        null
                    }

        )
    }
}
