package eu.kireobat.oauthapi.service

import eu.kireobat.oauthapi.api.dto.UserDto
import eu.kireobat.oauthapi.persistence.entity.UserEntity
import eu.kireobat.oauthapi.persistence.repo.UserRepo
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import kotlin.jvm.optionals.getOrElse

@Service
class UserService(
    private val userRepo: UserRepo
) {
    fun registerOrUpdateUser(oauthUser: OAuth2User): UserDto {
        val githubId = oauthUser.attributes["id"].toString()
        val email = oauthUser.attributes["email"].toString()
        val username = oauthUser.attributes["login"].toString()
        val avatarUrl = oauthUser.attributes["avatar_url"].toString()

        return userRepo.findByGithubId(githubId).orElseGet {
            userRepo.save(UserEntity(
                githubId = githubId,
                username = username,
                email = email,
                avatarUrl = avatarUrl
            ))
        }.toUserDto()
    }

    fun getUserByUsername(username: String): UserDto {
        return userRepo.findByUsername(username).getOrElse{throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")}.toUserDto()
    }
}