/**
 * Filename: SecurityConfig.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Configures Spring Security for the YesNoQuest web application.
 *
 * This class defines authentication and authorization rules, custom login/logout
 * behavior, password encoding strategy, CSRF configuration, and security settings
 * required for H2 console access during development.
 *
 * Responsibilities:
 * - Defines BCrypt password encoder bean
 * - Configures public vs protected routes
 * - Sets up custom login page
 * - Handles logout behavior
 * - Adjusts CSRF rules for H2 console
 * - Enables iframe support for embedded H2 console
 *
 * Security Notes:
 * - All routes require authentication unless explicitly permitted
 * - CSRF protection remains enabled (except for H2 console)
 * - BCrypt hashing ensures secure password storage
 */

package com.dre.yesnoquest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * Security configuration class for YesNoQuest.
 *
 * Marked with @Configuration so Spring Boot registers it
 * as part of the application context during startup.
 */
@Configuration
class SecurityConfig {

    /**
     * Provides a BCrypt password encoder bean.
     *
     * This encoder is used by Spring Security when
     * hashing and verifying user passwords.
     *
     * @return PasswordEncoder configured with BCrypt algorithm
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * Defines the security filter chain used to configure
     * HTTP security rules for the application.
     *
     * @param http HttpSecurity builder used to configure web security
     * @return SecurityFilterChain applied to incoming requests
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            /**
             * Authorization configuration:
             * Defines which endpoints are publicly accessible
             * and which require authentication.
             */
            .authorizeHttpRequests { auth ->
                auth
                    // Allow GET requests to test mail endpoint without authentication
                    .requestMatchers(HttpMethod.GET, "/test-mail").permitAll()

                    // Publicly accessible pages and static resources
                    .requestMatchers(
                        "/login",
                        "/register",
                        "/h2-console/**",
                        "/css/**",
                        "/js/**"
                    ).permitAll()

                    // All other endpoints require authentication
                    .anyRequest().authenticated()
            }

            /**
             * Custom form login configuration.
             * Uses our own login page instead of Spring's default.
             */
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            }

            /**
             * Logout configuration.
             * Defines logout endpoint and redirect behavior.
             */
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }

            /**
             * CSRF configuration.
             * CSRF protection remains enabled globally,
             * but disabled for H2 console endpoints
             * to allow development usage.
             */
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/h2-console/**")
            }

            /**
             * Header configuration.
             * Allows frames from the same origin,
             * required for embedded H2 console.
             */
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }

        return http.build()
    }
}