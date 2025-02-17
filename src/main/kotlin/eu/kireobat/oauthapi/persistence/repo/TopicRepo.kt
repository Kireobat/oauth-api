package eu.kireobat.oauthapi.persistence.repo

import eu.kireobat.oauthapi.persistence.entity.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TopicRepo: JpaRepository<TopicEntity, String> {
}