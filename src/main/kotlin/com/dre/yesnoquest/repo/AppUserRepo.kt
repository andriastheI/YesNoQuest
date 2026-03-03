/**
 * Filename: AppUserRepo.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Spring Data JPA repository for AppUser entity.
 *
 * Provides database access methods for user persistence and lookup.
 * Extends JpaRepository to inherit full CRUD capabilities.
 *
 * Responsibilities:
 * - Retrieve users by username
 * - Check if a username already exists
 * - Provide standard CRUD operations (save, delete, findAll, etc.)
 *
 * Design Notes:
 * - Spring automatically generates implementations at runtime.
 * - Method names follow Spring Data naming conventions.
 */

package com.dre.yesnoquest.repo

import com.dre.yesnoquest.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repository interface for AppUser persistence operations.
 *
 * @see AppUser
 */
interface AppUserRepo : JpaRepository<AppUser, Long> {

    /**
     * Finds a user by their unique username.
     *
     * @param username The username to search for
     * @return AppUser if found, otherwise null
     */
    fun findByUsername(username: String): AppUser?

    /**
     * Checks whether a user with the given username already exists.
     *
     * @param username The username to check
     * @return true if user exists, false otherwise
     */
    fun existsByUsername(username: String): Boolean
}