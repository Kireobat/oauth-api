package eu.kireobat.oauthapi.service

import eu.kireobat.oauthapi.api.dto.BlogDto
import eu.kireobat.oauthapi.api.dto.CreateBlogDto
import eu.kireobat.oauthapi.api.dto.OAuthApiPageDto
import eu.kireobat.oauthapi.persistence.entity.BlogEntity
import eu.kireobat.oauthapi.persistence.repo.BlogRepo
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
            user = user.toUserEntity(),
            title = createBlogDto.title,
            body = createBlogDto.description,
        )).toBlogDto()
    }

    fun editBlog(oAuth2User: OAuth2User, blogEntity: BlogEntity): BlogDto {
        val user = userService.registerOrUpdateUser(oAuth2User)

        val oldBlog = blogRepo.findById(blogEntity.id.toString()).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND,"Blog with id ${blogEntity.id} not found")
        }

        if (oldBlog.user.id != user.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }

        if (blogEntity.title != oldBlog.title && blogEntity.title != "") {
            oldBlog.title = blogEntity.title
        }
        if (blogEntity.body != oldBlog.body && blogEntity.body != "") {
            oldBlog.body = blogEntity.body
        }

        oldBlog.latestEditTime = ZonedDateTime.now()

        blogRepo.save(oldBlog)

        return oldBlog.toBlogDto()
    }

    fun getBlogById(id: Int): BlogDto {

        val blogDto = blogRepo.findById(id.toString()).getOrElse {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Blog with id $id not found")
        }.toBlogDto()

        return blogDto
    }

    fun getBlogs(pageable: Pageable): OAuthApiPageDto<BlogDto> {
        return OAuthApiPageDto(
            blogRepo.findAll(pageable).content.map{entity -> entity.toBlogDto()},
            blogRepo.count(),
            pageable.pageNumber,
            pageable.pageSize
        )

    }
}