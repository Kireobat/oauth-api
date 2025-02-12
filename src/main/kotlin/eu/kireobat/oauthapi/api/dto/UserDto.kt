package eu.kireobat.oauthapi.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import eu.kireobat.oauthapi.persistence.entity.UserEntity
import java.time.ZonedDateTime

data class UserDto (
    val id: Int,
    val githubId: String,
    val username: String,
    val email: String,
    val avatarUrl: String,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val registrationDate: ZonedDateTime
) {
    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            githubId = this.githubId,
            username = this.username,
            email = this.email,
            avatarUrl = this.avatarUrl,
            registrationDate = this.registrationDate
        )
    }
}