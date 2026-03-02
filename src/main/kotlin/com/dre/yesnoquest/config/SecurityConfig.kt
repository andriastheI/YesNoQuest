package com.dre.yesnoquest.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // ✅ Allow these pages without being logged in
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/test-mail").permitAll()
                    .requestMatchers("/login", "/register", "/h2-console/**", "/css/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
            }

            // ✅ Use our own login page at /login
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
            }

            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }

            // ✅ Keep CSRF ON but allow H2 console to work
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/h2-console/**")
            }

            // ✅ H2 console needs iframes
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }

        return http.build()
    }
}