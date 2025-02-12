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
    @JoinColumn(name="user_id")
    val user: UserEntity = UserEntity(),
    @Column(name="title")
    var title: String? = "",
    @Column(name="description")
    var body: String? = "",
    @Column(name="created_time")
    val createdTime: ZonedDateTime = ZonedDateTime.now(),
    @Column(name="latest_edit_time")
    var latestEditTime: ZonedDateTime? = null,
) {
    fun toBlogDto(): BlogDto {
        return BlogDto(
            id = this.id,
            user = this.user.toUserDto(),
            title = this.title,
            body = this.body,
            createdTime = this.createdTime,
            latestEditTime = this.latestEditTime,
            reactions = mutableListOf()
        )
    }
}
