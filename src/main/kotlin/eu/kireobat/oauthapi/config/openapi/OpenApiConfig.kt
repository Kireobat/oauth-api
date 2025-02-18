package eu.kireobat.oauthapi.config.openapi

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.Scopes
import org.springframework.beans.factory.annotation.Value

@Configuration
@EnableWebMvc
class OpenApiConfig {

    @Value("\${environment.api.path}")
    lateinit var apiPath: String

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(Info().title("OAuth API").version("1.0"))
            .addSecurityItem(SecurityRequirement().addList("github"))
            .components(
                Components()
                    .addSecuritySchemes("github", SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .flows(
                        OAuthFlows()
                            .authorizationCode(
                                OAuthFlow()
                                    .authorizationUrl("/oauth-api/oauth2/authorization/github?preferredRedirect=${apiPath}/oauth-api/swagger-ui/index.html")
                                    .tokenUrl("https://github.com/login/oauth/access_token")
                                    .scopes(
                                        Scopes()
                                            .addString("user:email", "Access your email")
                                            .addString("read:user", "Access profile information")
                                    )
                            )
                    )
                )
            )
    }
}
