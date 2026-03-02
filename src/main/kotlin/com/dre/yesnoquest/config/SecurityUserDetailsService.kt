package com.dre.yesnoquest.config

import com.dre.yesnoquest.repo.AppUserRepo
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class SecurityUserDetailsService(
    private val repo: AppUserRepo
) : UserDetailsService {

    override fun loadUserByUsername(username: String) =
        repo.findByUsername(username.trim())
            ?.let { u ->
                User.withUsername(u.username)
                    .password(u.passwordHash) // <-- this MUST match the DB column
                    .roles("USER")
                    .build()
            }
            ?: throw UsernameNotFoundException("User not found: $username")
}