package eu.kireobat.oauthapi.config.security

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.json.JSONObject
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import java.util.*

class CustomSvelteKitAuthSuccessHandler: SimpleUrlAuthenticationSuccessHandler() {

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        try {
            val originalState = request.session.getAttribute("preferredRedirect") ?: return "http://localhost:5173/"

            originalState as String

            if (originalState == "http://localhost:8080/oauth-api/swagger-ui/index.html") {
                return originalState
            }

            val paddedState = originalState.padEnd(originalState.length + (4 - originalState.length % 4) % 4, '=')
            val standardBase64 = paddedState
                .replace("-", "+")
                .replace("_", "/")

            val decodedBytes = Base64.getDecoder().decode(standardBase64)
            val decodedState = JSONObject(String(decodedBytes))
            return validateRedirectUri(decodedState.getString("redirectUri"))
        } catch (e: IllegalArgumentException) {
            logger.error("Unable to decode stateParam: ${request.getParameter("state")} Error message: ${e.message}")
            return "http://localhost:5173?error=invalid_state"
        }
    }

    private fun validateRedirectUri(uri: String): String {
        return if (uri.startsWith("http://localhost:5173") || uri.startsWith("https://kireobat.eu")) {
            uri
        } else {
            logger.warn("Unauthorized redirect uri: $uri")
            "http://localhost:5173?error=invalid_redirect"
        }
    }
}
