package eu.kireobat.oauthapi.api.controller

import eu.kireobat.oauthapi.persistence.entity.ReviewEntity
import eu.kireobat.oauthapi.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/public")
class PublicController(private val reviewService: ReviewService) {
    @GetMapping("/hello")
    fun getHelloWorld() = "Hello World"

    @GetMapping("/reviews")
    fun getReviews(): ResponseEntity<List<ReviewEntity>> {
        return ResponseEntity.ok(reviewService.getAllReviews())
    }


}