package com.dre.yesnoquest.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class AppUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var username: String = "",

    @Column(nullable = false)
    var passwordHash: String? = "",

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    var lastLoginAt: LocalDateTime? = null,

    @Column(nullable = false)
    var loginCount: Long = 0
)