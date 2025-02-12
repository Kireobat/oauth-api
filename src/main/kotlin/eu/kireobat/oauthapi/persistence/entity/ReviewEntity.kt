package eu.kireobat.oauthapi.persistence.entity

import jakarta.persistence.*
import java.math.BigInteger
import java.time.ZonedDateTime

@Entity
@Table(name = "reviews")
data class ReviewEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviewsSeq")
    @SequenceGenerator(name = "reviewsSeq", sequenceName = "reviews_seq", allocationSize = 1)
    @Column(name="id")
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name="user_id")
    val user: UserEntity? = null,
    @Column(name="title")
    val title: String? = null,
    @Column(name="description")
    val description: String? = null,
    @Column(name="rating")
    val rating: Int = 0,
    @Column(name="created_time")
    val createdTime: ZonedDateTime = ZonedDateTime.now(),
)
