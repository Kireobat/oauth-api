package eu.kireobat.oauthapi.api.controller

import eu.kireobat.oauthapi.api.dto.BlogDto
import eu.kireobat.oauthapi.api.dto.CreateBlogDto
import eu.kireobat.oauthapi.api.dto.OAuthApiPageDto
import eu.kireobat.oauthapi.common.Constants.Companion.DEFAULT_PAGE_SIZE_INT
import eu.kireobat.oauthapi.common.Constants.Companion.DEFAULT_SORT_NO_DIRECTION
import eu.kireobat.oauthapi.persistence.entity.BlogEntity
import eu.kireobat.oauthapi.service.BlogService
import eu.kireobat.oauthapi.service.ReactionService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/v1")
class BlogController(private val blogService: BlogService, private val reactionService: ReactionService) {
    @GetMapping("/blog")
    fun getBlogById(@RequestParam(name = "id", required = true) id: Int): ResponseEntity<BlogDto> {

        val reactionDtoList = reactionService.getReactionsByBlogId(id)
        val blogDto = blogService.getBlogById(id)

        blogDto.reactions = reactionDtoList

        return ResponseEntity.ok(blogDto)
    }

    @GetMapping("/blogs")
    fun getBlogs(@ParameterObject @PageableDefault(size = DEFAULT_PAGE_SIZE_INT, sort  = [DEFAULT_SORT_NO_DIRECTION]) pageable: Pageable): ResponseEntity<OAuthApiPageDto<BlogDto>> {
        return ResponseEntity.ok(blogService.getBlogs(pageable))
    }

    @PostMapping("/blog/create")
    fun createBlog(authentication: Authentication?,@RequestBody request: CreateBlogDto): ResponseEntity<BlogDto> {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }

        return ResponseEntity.ok(blogService.createBlog(authentication.principal as OAuth2User, request))
    }

    @PutMapping("/blog/update")
    fun updateBlogById(authentication: Authentication?, @RequestBody request: BlogEntity): ResponseEntity<BlogDto> {
        if (authentication == null) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        }
        return ResponseEntity.ok(blogService.editBlog(authentication.principal as OAuth2User, request))
    }
}