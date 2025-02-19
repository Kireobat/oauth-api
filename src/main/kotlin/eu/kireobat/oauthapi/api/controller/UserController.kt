package eu.kireobat.oauthapi.api.controller

import eu.kireobat.oauthapi.api.dto.OAuthApiPageDto
import eu.kireobat.oauthapi.api.dto.UserDto
import eu.kireobat.oauthapi.common.Constants.Companion.DEFAULT_PAGE_SIZE_INT
import eu.kireobat.oauthapi.common.Constants.Companion.DEFAULT_SORT_NO_DIRECTION
import eu.kireobat.oauthapi.service.UserService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
class UserController(private val userService: UserService) {
    @GetMapping("/user/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.getUserByUsername(username))
    }
    @GetMapping("/users")
    fun getUsers(
        @ParameterObject @PageableDefault(size = DEFAULT_PAGE_SIZE_INT, sort  = [DEFAULT_SORT_NO_DIRECTION]) pageable: Pageable,
        @RequestParam(name = "searchQuery", required = false) searchQuery: String?,
    ): ResponseEntity<OAuthApiPageDto<UserDto>>{
        return ResponseEntity.ok(userService.getUsers(pageable, searchQuery))
    }
}