/**
 * Filename: AuthController.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Handles authentication-related web routes for the YesNoQuest application.
 *
 * This controller manages:
 * - Login page rendering
 * - Registration page rendering
 * - User registration processing
 *
 * Responsibilities:
 * - Expose login and registration endpoints
 * - Pass UI state flags to templates (error, logout, registered)
 * - Delegate user creation logic to UserService
 * - Handle registration errors gracefully
 *
 * Security Notes:
 * - Authentication itself is handled by Spring Security (SecurityConfig).
 * - This controller does NOT verify passwords manually.
 * - Registration logic relies on UserService for validation and hashing.
 */

package com.dre.yesnoquest.controller

import com.dre.yesnoquest.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * Controller responsible for login and registration views.
 *
 * @property userService Service layer handling user registration logic.
 */
@Controller
class AuthController(
    private val userService: UserService
) {

    /**
     * Displays the login page.
     *
     * Reads optional query parameters used to control UI state:
     * - error → login failure
     * - logout → successful logout
     * - registered → successful registration
     *
     * @param error Indicates login error
     * @param logout Indicates successful logout
     * @param registered Indicates successful registration
     * @param model Model used to pass attributes to the view
     * @return login template name
     */
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

    /**
     * Displays the registration page.
     *
     * @return register template name
     */
    @GetMapping("/register")
    fun registerPage(): String = "register"

    /**
     * Handles registration form submission.
     *
     * Delegates user creation to UserService.
     * On success → redirects to login page with registered flag.
     * On failure → returns registration page with error message.
     *
     * @param username Submitted username
     * @param password Submitted password (plaintext at this stage)
     * @param model Model used to pass error message to view
     * @return Redirect or view name
     */
    @PostMapping("/register")
    fun doRegister(
        @RequestParam username: String,
        @RequestParam password: String,
        model: Model
    ): String {

        return try {
            userService.register(username, password)

            // After successful registration, redirect to login page
            "redirect:/login?registered"

        } catch (e: Exception) {

            // If registration fails, show error message on registration page
            model.addAttribute("error", e.message ?: "Registration failed.")
            "register"
        }
    }
}