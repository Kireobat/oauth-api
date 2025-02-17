package eu.kireobat.oauthapi.api.controller

import eu.kireobat.oauthapi.api.dto.OAuthApiPageDto
import eu.kireobat.oauthapi.api.dto.TopicDto
import eu.kireobat.oauthapi.common.Constants.Companion.DEFAULT_PAGE_SIZE_INT
import eu.kireobat.oauthapi.common.Constants.Companion.DEFAULT_SORT_NO_DIRECTION
import eu.kireobat.oauthapi.service.TopicService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class TopicController( private val topicService: TopicService) {
    @GetMapping("/topics")
    fun getTopics(@ParameterObject @PageableDefault(size = DEFAULT_PAGE_SIZE_INT, sort  = [DEFAULT_SORT_NO_DIRECTION]) pageable: Pageable): ResponseEntity<OAuthApiPageDto<TopicDto>> {
        return ResponseEntity.ok(topicService.getTopics(pageable))
    }
}