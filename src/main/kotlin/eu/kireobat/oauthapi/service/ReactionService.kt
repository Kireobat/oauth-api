package eu.kireobat.oauthapi.service

import eu.kireobat.oauthapi.api.dto.CreateReactionDto
import eu.kireobat.oauthapi.api.dto.OAuthResponseDto
import eu.kireobat.oauthapi.api.dto.ReactionDto
import eu.kireobat.oauthapi.persistence.entity.ReactionEntity
import eu.kireobat.oauthapi.persistence.repo.ReactionRepo
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.ZonedDateTime
import kotlin.jvm.optionals.getOrElse

@Service
class ReactionService(
    private val reactionRepo: ReactionRepo,
    private val userService: UserService,
    private val blogService: BlogService
) {
    fun getReactionsByBlogId(id: Int): List<ReactionDto> {

        val reactionDtoList = mutableListOf<ReactionDto>()

        val reactionEntityList = reactionRepo.findAllByBlogId(id)

        for (reaction in reactionEntityList) {
            reactionDtoList += reaction.toReactionDto()
        }

        return reactionDtoList
    }

    fun addReaction(oAuth2User: OAuth2User, createReactionDto: CreateReactionDto): ReactionDto {

        val user = userService.registerOrUpdateUser(oAuth2User)

        if (reactionRepo.existsByUserIdAndBlogIdAndReaction(user.id, createReactionDto.blogId, createReactionDto.reaction)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Duplicate reaction")
        }

        val blog = blogService.getBlogById(createReactionDto.blogId)

        return reactionRepo.save(ReactionEntity(
            user = user.toUserEntity(),
            blog = blog.toBlogEntity(),
            reaction = createReactionDto.reaction
        )).toReactionDto()
    }

    fun removeReaction(oAuth2User: OAuth2User, reactionId: Number): OAuthResponseDto {

        val user = userService.registerOrUpdateUser(oAuth2User)

        val reaction = reactionRepo.findById(reactionId.toString()).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "This reaction does not exist")
        }

        if (reaction.user.id != user.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }

        reactionRepo.deleteById(reactionId.toString())

        return OAuthResponseDto(true, ZonedDateTime.now(), HttpStatus.OK, "Reaction removed successfully")
    }
}