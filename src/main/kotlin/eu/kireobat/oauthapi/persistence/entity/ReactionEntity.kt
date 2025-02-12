package eu.kireobat.oauthapi.persistence.entity

import eu.kireobat.oauthapi.api.dto.ReactionDto
import jakarta.persistence.*
import java.math.BigInteger
import java.time.ZonedDateTime

@Entity
@Table(name = "reactions")
data class ReactionEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reactionsSeq")
    @SequenceGenerator(name = "reactionsSeq", sequenceName = "reactions_seq", allocationSize = 1)
    @Column(name="id")
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name="user_id")
    val user: UserEntity = UserEntity(),
    @ManyToOne
    @JoinColumn(name="blog_id")
    val blog: BlogEntity = BlogEntity(),
    @Column(name="reaction")
    val reaction: String = "❤",
    @Column(name="created_time")
    val createdTime: ZonedDateTime = ZonedDateTime.now()
) {
    fun toReactionDto(): ReactionDto {
        return ReactionDto(
            id = this.id,
            user = this.user.toUserDto(),
            blogId = this.blog.id,
            reaction = this.reaction,
            createdTime = this.createdTime,
        )
    }
}
