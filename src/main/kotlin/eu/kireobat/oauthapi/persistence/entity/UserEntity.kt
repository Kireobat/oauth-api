package eu.kireobat.oauthapi.persistence.entity

import eu.kireobat.oauthapi.api.dto.UserDto
import jakarta.persistence.*
import java.math.BigInteger
import java.time.ZonedDateTime

@Entity
@Table(name = "users")
data class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersSeq")
    @SequenceGenerator(name = "usersSeq", sequenceName = "users_seq", allocationSize = 1)
    @Column(name="id")
    val id: Int = 0,
    @Column(name="github_id")
    val githubId: String = "",
    @Column(name="username")
    val username: String = "",
    @Column(name="email")
    val email: String = "",
    @Column(name="avatar_url")
    val avatarUrl: String = "",
    @Column(name="registration_date")
    val registrationDate: ZonedDateTime = ZonedDateTime.now(),
) {
    fun toUserDto(): UserDto {
        return UserDto(
            id = this.id,
            githubId = this.githubId,
            username = this.username,
            email = this.email,
            avatarUrl = this.avatarUrl,
            registrationDate = this.registrationDate
        )
    }
}
