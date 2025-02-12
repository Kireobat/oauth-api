package eu.kireobat.oauthapi.api.controller

import eu.kireobat.oauthapi.api.dto.CreateReviewDto
import eu.kireobat.oauthapi.api.dto.UserDto
import eu.kireobat.oauthapi.persistence.entity.ReviewEntity
import eu.kireobat.oauthapi.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/authenticated")
class PrivateController(private val reviewService: ReviewService) {
    @PostMapping("/reviews/create")
    fun createReview(authentication: Authentication, @RequestBody review: CreateReviewDto): ResponseEntity<ReviewEntity> {
        val oAuthUser = authentication.principal as OAuth2User

        return ResponseEntity.ok(reviewService.createReview(oAuthUser,review))
    }
}