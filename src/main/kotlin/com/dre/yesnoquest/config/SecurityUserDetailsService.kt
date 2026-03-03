/**
 * Filename: SecurityUserDetailsService.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Custom Spring Security UserDetailsService for the YesNoQuest application.
 *
 * This service tells Spring Security how to load a user from the database
 * during authentication (login). It queries the AppUserRepo by username,
 * then maps the returned AppUser entity into Spring Security's UserDetails
 * object (username, password hash, and roles).
 *
 * Responsibilities:
 * - Trim and normalize the incoming username
 * - Fetch the matching user record from the database via AppUserRepo
 * - Convert the database user into a Spring Security UserDetails instance
 * - Throw UsernameNotFoundException when no user is found
 *
 * Security Notes:
 * - The password provided here must be the stored hash (NOT plaintext).
 * - The encoder configured in SecurityConfig (BCryptPasswordEncoder) must
 *   match the hashing used when saving passwords.
 * - Roles are currently hard-coded to "USER" for all authenticated accounts.
 */

package com.dre.yesnoquest.config

import com.dre.yesnoquest.repo.AppUserRepo
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Spring Security adapter that loads user credentials from the database.
 *
 * Spring Security calls loadUserByUsername(...) automatically during login.
 *
 * @property repo Repository used to fetch users from persistence by username.
 */
@Service
class SecurityUserDetailsService(
    private val repo: AppUserRepo
) : UserDetailsService {

    /**
     * Loads a user from the database and adapts them into a UserDetails object.
     *
     * Flow:
     * 1) Trim the incoming username (defensive against whitespace issues)
     * 2) Query the database repository for a matching user
     * 3) If found, build a Spring Security UserDetails instance:
     *    - username: u.username
     *    - password: u.passwordHash (stored hash from DB)
     *    - roles: USER
     * 4) If not found, throw UsernameNotFoundException (required by Spring Security)
     *
     * @param username Username entered during login
     * @return UserDetails object consumed by Spring Security authentication pipeline
     * @throws UsernameNotFoundException if no matching user exists
     */
    override fun loadUserByUsername(username: String) =
        repo.findByUsername(username.trim())
            ?.let { u ->
                User.withUsername(u.username)
                    // The password value MUST be the hashed password stored in the DB.
                    // This must align with the entity field/column used for password storage.
                    .password(u.passwordHash)
                    // Current authorization model: every authenticated user is a USER.
                    // Extension point: map roles from DB later if needed.
                    .roles("USER")
                    .build()
            }
            ?: throw UsernameNotFoundException("User not found: $username")
}