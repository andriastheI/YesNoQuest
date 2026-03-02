package com.dre.yesnoquest.controller

import com.dre.yesnoquest.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AuthController(
    private val userService: UserService
) {
    @GetMapping("/login")
    fun loginPage(
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) logout: String?,
        @RequestParam(required = false) registered: String?,
        model: Model
    ): String {
        model.addAttribute("error", error != null)
        model.addAttribute("logout", logout != null)
        model.addAttribute("registered", registered != null)
        return "login"
    }

    @GetMapping("/register")
    fun registerPage(): String = "register"

    @PostMapping("/register")
    fun doRegister(
        @RequestParam username: String,
        @RequestParam password: String,
        model: Model
    ): String {
        return try {
            userService.register(username, password)
            // after registering, send them to login
            "redirect:/login?registered"
        } catch (e: Exception) {
            model.addAttribute("error", e.message ?: "Registration failed.")
            "register"
        }
    }
}