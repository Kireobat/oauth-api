package eu.kireobat.oauthapi.api.controller

import eu.kireobat.oauthapi.api.dto.UserDto
import eu.kireobat.oauthapi.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1/")
class AuthController(private val userService: UserService) {
    @GetMapping("/user")
    fun getUser(authentication: Authentication?): ResponseEntity<UserDto> {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
        return ResponseEntity.ok(userService.registerOrUpdateUser(authentication.principal as OAuth2User))
    }
}