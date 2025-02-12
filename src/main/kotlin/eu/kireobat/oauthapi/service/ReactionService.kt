package eu.kireobat.oauthapi.service

import eu.kireobat.oauthapi.api.dto.CreateReactionDto
import eu.kireobat.oauthapi.api.dto.ReactionDto
import eu.kireobat.oauthapi.persistence.entity.ReactionEntity
import eu.kireobat.oauthapi.persistence.repo.ReactionRepo
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

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

        val blog = blogService.getBlogById(createReactionDto.blogId)

        return reactionRepo.save(ReactionEntity(
            user = user.toUserEntity(),
            blog = blog.toBlogEntity(),
            reaction = createReactionDto.reaction
        )).toReactionDto()
    }
}