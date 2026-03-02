package com.dre.yesnoquest.repo

import com.dre.yesnoquest.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository

interface AppUserRepo : JpaRepository<AppUser, Long> {
    fun findByUsername(username: String): AppUser?
    fun existsByUsername(username: String): Boolean
}