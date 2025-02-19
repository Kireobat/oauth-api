package eu.kireobat.oauthapi.persistence.repo

import eu.kireobat.oauthapi.persistence.entity.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepo: JpaRepository<UserEntity, String> {
    fun findByGithubId(githubId: String): Optional<UserEntity>
    fun findByUsername(userName: String): Optional<UserEntity>
    fun findByUsernameContainingIgnoreCase(pageable: Pageable, username: String): Page<UserEntity>
    fun countAllByUsernameContainingIgnoreCase(username: String): Long
}