package eu.kireobat.oauthapi.persistence.repo

import eu.kireobat.oauthapi.persistence.entity.ReviewEntity
import eu.kireobat.oauthapi.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ReviewRepo: JpaRepository<ReviewEntity, String> {
}