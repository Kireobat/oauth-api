package eu.kireobat.oauthapi.persistence.repo

import eu.kireobat.oauthapi.persistence.entity.BlogEntity
import eu.kireobat.oauthapi.persistence.entity.ReviewEntity
import eu.kireobat.oauthapi.persistence.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BlogRepo: JpaRepository<BlogEntity, String>, JpaSpecificationExecutor<BlogEntity> {
    fun findAllByTopicIdAndCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable: Pageable, topicId: Number?, userId: Number?, title: String?, description: String?): Page<BlogEntity>
    fun countAllByTopicIdAndCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(topicId: Number?, userId: Number?, title: String?, description: String?): Long

    fun findAllByTopicIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable: Pageable, userId: Number?, title: String?, description: String?): Page<BlogEntity>
    fun countAllByTopicIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(topicId: Number?, title: String?, description: String?): Long

    fun findAllByCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable: Pageable, userId: Number?, title: String?, description: String?): Page<BlogEntity>
    fun countAllByCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(userId: Number?, title: String?, description: String?): Long

    fun findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable: Pageable, title: String?, description: String?): Page<BlogEntity>
    fun countAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title: String?, description: String?): Long

    fun findAllByTopicIdAndCreatedById(pageable: Pageable, topicId: Number?, userId: Number?): Page<BlogEntity>
    fun countAllByTopicIdAndCreatedById(topicId: Number?, userId: Number?): Long

    fun findAllByTopicId(pageable: Pageable, topicId: Number?): Page<BlogEntity>
    fun countAllByTopicId(topicId: Number?): Long

    fun findAllByCreatedById(pageable: Pageable, userId: Number?): Page<BlogEntity>
    fun countAllByCreatedById(userId: Number?): Long
}