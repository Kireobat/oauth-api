package eu.kireobat.oauthapi.service

import eu.kireobat.oauthapi.api.dto.CreateReviewDto
import eu.kireobat.oauthapi.persistence.entity.ReviewEntity
import eu.kireobat.oauthapi.persistence.repo.ReviewRepo
import org.apache.coyote.BadRequestException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val userService: UserService,
    private val reviewRepo: ReviewRepo
) {

    fun createReview(oAuth2User: OAuth2User, createReviewDto: CreateReviewDto): ReviewEntity {

        validateReview(createReviewDto)

        val user = userService.registerOrUpdateUser(oAuth2User)

        val reviewEntity = reviewRepo.save(ReviewEntity(
            user = user.toUserEntity(),
            title = createReviewDto.title,
            description = createReviewDto.description,
            rating = createReviewDto.rating
        ))

        return reviewEntity
    }

    fun getAllReviews(): List<ReviewEntity> {
        return reviewRepo.findAll()
    }

    private fun validateReview(createReviewDto: CreateReviewDto) {
        val errorList: MutableList<String> = mutableListOf()

        if (createReviewDto.rating > 5) {
            errorList += "Rating can not be above 5"
        } else if (createReviewDto.rating < 0) {
            errorList  += "Rating can not be below 0"
        }

        if (createReviewDto.title.isNotEmpty() && createReviewDto.description.isEmpty()) {
            errorList  += "Review is missing a description"
        } else if (createReviewDto.title.isEmpty() && createReviewDto.description.isNotEmpty()){
            errorList  += "Review is missing a title"
        }

        if (errorList.isNotEmpty()) {
            throw BadRequestException(errorList.toString())
        }
    }
}