/**
 * Filename: UserService.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Service responsible for user-related business logic in the YesNoQuest application.
 *
 * This service handles:
 * - User registration
 * - Username validation
 * - Password validation
 * - Password hashing using configured PasswordEncoder
 * - Persisting users via AppUserRepo
 *
 * Responsibilities:
 * - Enforce validation rules before persistence
 * - Prevent duplicate usernames
 * - Ensure passwords are securely hashed before storage
 *
 * Security Notes:
 * - Passwords are encoded using the configured PasswordEncoder (BCrypt).
 * - Plaintext passwords are never stored.
 * - Username uniqueness is enforced both at service and database levels.
 */

package com.dre.yesnoquest.service

import com.dre.yesnoquest.model.AppUser
import com.dre.yesnoquest.repo.AppUserRepo
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Service layer for AppUser-related operations.
 *
 * @property repo Repository for user persistence
 * @property encoder PasswordEncoder used to hash passwords
 */
@Service
class UserService(
    private val repo: AppUserRepo,
    private val encoder: PasswordEncoder
) {

    /**
     * Registers a new user.
     *
     * Validation Rules:
     * - Username must be 3–20 characters
     * - Password must be 6–50 characters
     * - Username must not already exist
     *
     * Flow:
     * 1) Trim username
     * 2) Validate constraints
     * 3) Encode raw password using BCrypt
     * 4) Create AppUser entity
     * 5) Save to database
     *
     * @param username Raw username input
     * @param rawPassword Plaintext password input
     * @return Saved AppUser entity
     * @throws IllegalArgumentException if validation fails
     */
    fun register(username: String, rawPassword: String): AppUser {

        val cleanUser = username.trim()

        require(cleanUser.length in 3..20) {
            "Username must be 3–20 characters."
        }

        require(rawPassword.length in 6..50) {
            "Password must be at least 6 characters."
        }

        require(!repo.existsByUsername(cleanUser)) {
            "Username already taken."
        }

        val user = AppUser(
            username = cleanUser,
            passwordHash = encoder.encode(rawPassword)
        )

        return repo.save(user)
    }

    /**
     * Retrieves a user by username.
     *
     * @param username Username to search for
     * @return AppUser if found, otherwise null
     */
    fun findByUsername(username: String): AppUser? =
        repo.findByUsername(username)
}