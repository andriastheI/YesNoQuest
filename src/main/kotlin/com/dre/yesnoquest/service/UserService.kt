package com.dre.yesnoquest.service

import com.dre.yesnoquest.model.AppUser
import com.dre.yesnoquest.repo.AppUserRepo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repo: AppUserRepo,
    private val encoder: PasswordEncoder
) {
    fun register(username: String, rawPassword: String): AppUser {
        val cleanUser = username.trim()

        require(cleanUser.length in 3..20) { "Username must be 3–20 characters." }
        require(rawPassword.length in 6..50) { "Password must be at least 6 characters." }
        require(!repo.existsByUsername(cleanUser)) { "Username already taken." }

        val user = AppUser(
            username = cleanUser,
            passwordHash = encoder.encode(rawPassword)
        )
        return repo.save(user)
    }

    fun findByUsername(username: String): AppUser? = repo.findByUsername(username)
}