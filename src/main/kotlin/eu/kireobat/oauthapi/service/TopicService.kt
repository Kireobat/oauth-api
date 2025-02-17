package eu.kireobat.oauthapi.service

import eu.kireobat.oauthapi.api.dto.BlogDto
import eu.kireobat.oauthapi.api.dto.CreateBlogDto
import eu.kireobat.oauthapi.api.dto.OAuthApiPageDto
import eu.kireobat.oauthapi.api.dto.TopicDto
import eu.kireobat.oauthapi.persistence.entity.BlogEntity
import eu.kireobat.oauthapi.persistence.repo.BlogRepo
import eu.kireobat.oauthapi.persistence.repo.TopicRepo
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.ZonedDateTime
import kotlin.jvm.optionals.getOrElse

@Service
class TopicService(
    private val topicRepo: TopicRepo
) {
    fun getTopics(pageable: Pageable): OAuthApiPageDto<TopicDto> {
        return OAuthApiPageDto(
            topicRepo.findAll(pageable).content.map{entity -> entity.toTopicDto()},
            topicRepo.count(),
            pageable.pageNumber,
            pageable.pageSize
        )

    }
}