package eu.kireobat.oauthapi.service

import eu.kireobat.oauthapi.api.dto.BlogDto
import eu.kireobat.oauthapi.api.dto.CreateBlogDto
import eu.kireobat.oauthapi.api.dto.OAuthApiPageDto
import eu.kireobat.oauthapi.persistence.entity.BlogEntity
import eu.kireobat.oauthapi.persistence.repo.BlogRepo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.ZonedDateTime
import kotlin.jvm.optionals.getOrElse

@Service
class BlogService(
    private val blogRepo: BlogRepo,
    private val userService: UserService
) {

    fun createBlog(oAuth2User: OAuth2User, createBlogDto: CreateBlogDto): BlogDto {

        val user = userService.registerOrUpdateUser(oAuth2User)

        return blogRepo.save(BlogEntity(
            createdBy = user.toUserEntity(),
            title = createBlogDto.title,
            description = createBlogDto.description,
        )).toBlogDto()
    }

    fun editBlog(oAuth2User: OAuth2User, blogEntity: BlogEntity): BlogDto {
        val user = userService.registerOrUpdateUser(oAuth2User)

        val oldBlog = blogRepo.findById(blogEntity.id.toString()).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND,"Blog with id ${blogEntity.id} not found")
        }

        if (oldBlog.createdBy.id != user.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }

        if (blogEntity.title != oldBlog.title && blogEntity.title != "") {
            oldBlog.title = blogEntity.title
        }
        if (blogEntity.description != oldBlog.description && blogEntity.description != "") {
            oldBlog.description = blogEntity.description
        }

        oldBlog.editedTime = ZonedDateTime.now()

        blogRepo.save(oldBlog)

        return oldBlog.toBlogDto()
    }

    fun getBlogById(id: Int): BlogDto {

        val blogDto = blogRepo.findById(id.toString()).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Blog with id $id not found")
        }.toBlogDto()

        return blogDto
    }

    fun getBlogs(pageable: Pageable, topicId: Number?, userId: Number?, searchQuery: String?): OAuthApiPageDto<BlogDto> {

        val blogs: Page<BlogEntity>
        val count: Long


        if (topicId != null && userId != null && searchQuery != null) {
            blogs = blogRepo.findAllByTopicIdAndCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable, topicId, userId, searchQuery, searchQuery)
            count = blogRepo.countAllByTopicIdAndCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(topicId, userId, searchQuery, searchQuery)
        } else if (topicId != null && userId == null && searchQuery != null) {
            blogs = blogRepo.findAllByTopicIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable, topicId, searchQuery, searchQuery)
            count = blogRepo.countAllByTopicIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(topicId, searchQuery, searchQuery)
        } else if (topicId == null && userId != null && searchQuery != null) {
            blogs = blogRepo.findAllByCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable, userId, searchQuery, searchQuery)
            count = blogRepo.countAllByCreatedByIdAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(userId, searchQuery, searchQuery)
        } else if (topicId == null && userId == null && searchQuery != null) {
            blogs = blogRepo.findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(pageable, searchQuery, searchQuery)
            count = blogRepo.countAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchQuery, searchQuery)
        } else if (topicId != null && userId != null) {
            blogs = blogRepo.findAllByTopicIdAndCreatedById(pageable, topicId, userId)
            count = blogRepo.countAllByTopicIdAndCreatedById(topicId, userId)
        } else if (topicId != null) {
            blogs = blogRepo.findAllByTopicId(pageable, topicId)
            count = blogRepo.countAllByTopicId(topicId)
        } else if (userId != null) {
            blogs = blogRepo.findAllByCreatedById(pageable, userId)
            count = blogRepo.countAllByCreatedById(userId)
        } else {
            blogs = blogRepo.findAll(pageable)
            count = blogRepo.count()
        }

        return OAuthApiPageDto(
            blogs.content.map{entity -> entity.toBlogDto()},
            count,
            pageable.pageNumber,
            pageable.pageSize
        )

    }
}