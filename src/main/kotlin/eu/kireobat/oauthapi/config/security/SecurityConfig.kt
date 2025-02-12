package eu.kireobat.oauthapi.config.security

import eu.kireobat.oauthapi.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val customOAuth2UserService: CustomOAuth2UserService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity,
                            clientRegistrationRepository: ClientRegistrationRepository): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/swagger-ui/**", permitAll) // Allow access to all Swagger UI paths
                authorize("/v3/api-docs/**", permitAll) // Allow access to API docs
                authorize("/api/v1/**", permitAll)
                authorize("/api/v1/user", authenticated)
                authorize("/login", permitAll) // Allow access to the login endpoint
                authorize(anyRequest, authenticated)
            }
            oauth2Login {
                authorizationEndpoint {
                    authorizationRequestResolver = CustomAuthorizationRequestResolver(
                        clientRegistrationRepository = clientRegistrationRepository,
                        authorizationRequestBaseUri = "/oauth2/authorization"
                    )
                }
                userInfoEndpoint {
                    userService = customOAuth2UserService
                }
                authenticationSuccessHandler = svelteKitAuthSuccessHandler()
            }
            cors {
                configurationSource = corsConfigurationSource()
            }
            csrf {
                disable()
            }
            sessionManagement {
                sessionFixation { changeSessionId() }
                sessionCreationPolicy = SessionCreationPolicy.ALWAYS // Force session creation
            }
            logout {
                logoutSuccessUrl = "http://localhost:5173"
                deleteCookies("JSESSIONID")
                invalidateHttpSession = true
            }
            exceptionHandling {
                authenticationEntryPoint = CustomAuthenticationEntryPoint()
            }
        }
        return http.build()
    }
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:8080", "http://localhost:5173", "https://github.com")
            allowedMethods = listOf("*")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun svelteKitAuthSuccessHandler(): AuthenticationSuccessHandler {
        return CustomSvelteKitAuthSuccessHandler()
    }
}