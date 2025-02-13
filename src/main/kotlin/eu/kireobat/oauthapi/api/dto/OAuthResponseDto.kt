package eu.kireobat.oauthapi.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.time.ZonedDateTime

data class OAuthResponseDto(
    var success: Boolean,
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSX")
    var timestamp: ZonedDateTime,
    var status: HttpStatus,
    var message: String
)
