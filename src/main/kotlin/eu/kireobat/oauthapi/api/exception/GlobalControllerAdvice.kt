package eu.kireobat.oauthapi.api.exception

import eu.kireobat.oauthapi.api.dto.OAuthResponseDto
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import java.time.ZonedDateTime
@RestControllerAdvice
class GlobalControllerAdvice {

    @ApiResponse(
        responseCode = "200-599",
        description = "Alternative response",
        content = [
            Content(
                schema = Schema(
                    implementation = OAuthResponseDto::class
                )
            )
        ])
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<OAuthResponseDto> {

        val message = if (HttpStatus.valueOf(e.body.status) == HttpStatus.UNAUTHORIZED) {
            "You must be logged in for this action"
        } else if (HttpStatus.valueOf(e.body.status) == HttpStatus.FORBIDDEN) {
            "Missing permission for this action"
        } else {
            e.message
        }

        val response = OAuthResponseDto(
            false, ZonedDateTime.now(), HttpStatus.valueOf(e.body.status), message
        )

        return ResponseEntity.status(HttpStatus.valueOf(e.body.status)).body(response)
    }
}