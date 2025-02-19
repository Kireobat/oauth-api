package eu.kireobat.oauthapi.persistence.repo

import eu.kireobat.oauthapi.persistence.entity.ReactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReactionRepo: JpaRepository<ReactionEntity, String> {
    fun findAllByBlogId(blogId: Int): List<ReactionEntity>
    fun existsByUserIdAndBlogIdAndReaction(userId: Int, blogId: Int, reaction: String): Boolean
}