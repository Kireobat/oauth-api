package eu.kireobat.oauthapi.api.controller

import eu.kireobat.oauthapi.api.dto.CreateReactionDto
import eu.kireobat.oauthapi.api.dto.ReactionDto
import eu.kireobat.oauthapi.service.ReactionService
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1")
class ReactionController(private val reactionService: ReactionService) {
    @PostMapping("/reaction/add")
    fun addReaction(authentication: Authentication?, @RequestBody request: CreateReactionDto) {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

    reactionService.addReaction(authentication.principal as OAuth2User, request)
    }
    @PostMapping("/reaction/remove")
    fun removeReaction(authentication: Authentication?, @RequestParam reactionId: Number) {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        reactionService.removeReaction(authentication.principal as OAuth2User, reactionId)
    }
}