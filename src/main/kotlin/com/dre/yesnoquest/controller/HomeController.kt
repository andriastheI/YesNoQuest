/**
 * Filename: HomeController.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Main application controller for authenticated users in YesNoQuest.
 *
 * This controller:
 * - Displays the home page after login
 * - Accepts question submissions
 * - Builds themed Yes/No HTML files
 * - Sends the generated HTML as an email attachment
 *
 * Responsibilities:
 * - Provide authenticated username to the view
 * - Expose available themes to the UI
 * - Delegate HTML generation to HtmlBuilder
 * - Delegate email sending to MailService
 * - Gracefully handle email errors
 *
 * Security Notes:
 * - Requires authenticated user (enforced in SecurityConfig)
 * - Uses Authentication object injected by Spring Security
 * - Does not manually verify login state
 */

package com.dre.yesnoquest.controller

import com.dre.yesnoquest.model.Theme
import com.dre.yesnoquest.service.HtmlBuilder
import com.dre.yesnoquest.service.MailService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * Controller responsible for the main YesNoQuest functionality.
 *
 * @property htmlBuilder Service that generates themed Yes/No HTML content
 * @property mailService Service responsible for sending email attachments
 */
@Controller
class HomeController(
    private val htmlBuilder: HtmlBuilder,
    private val mailService: MailService
) {

    /**
     * Displays the main dashboard page for authenticated users.
     *
     * @param auth Spring Security authentication object
     * @param model Model used to pass attributes to the view
     * @return index template name
     */
    @GetMapping("/")
    fun index(auth: Authentication, model: Model): String {

        // Provide username to template
        model.addAttribute("username", auth.name)

        // Provide all available themes (enum entries)
        model.addAttribute("themes", Theme.entries)

        return "index"
    }

    /**
     * Handles submission of a Yes/No question.
     *
     * Flow:
     * 1) Generate themed HTML content
     * 2) Generate a filename
     * 3) Send email with HTML attachment
     * 4) Return success message or error message
     *
     * @param auth Authenticated user context
     * @param recipientEmail Email address of recipient
     * @param question Question to embed in HTML
     * @param theme Selected theme (enum)
     * @param model Model for UI feedback
     * @return index template (success or error state)
     */
    @PostMapping("/send")
    fun send(
        auth: Authentication,
        @RequestParam recipientEmail: String,
        @RequestParam question: String,
        @RequestParam theme: Theme,
        model: Model
    ): String {

        // Re-add user and theme data for page re-render
        model.addAttribute("username", auth.name)
        model.addAttribute("themes", Theme.entries)

        return try {

            // Generate themed HTML file content
            val html = htmlBuilder.buildHtml(question, theme)

            // Generate filename based on theme
            val filename = htmlBuilder.buildFilename(theme)

            // Send email with generated HTML attachment
            mailService.sendHtmlAttachment(
                to = recipientEmail.trim(),
                subject = "You have a Yes/No question",
                bodyText = "Attached is an HTML file. Download it and open locally. Try clicking NO if you dare.",
                filename = filename,
                html = html
            )

            // Success feedback to UI
            model.addAttribute("sentTo", recipientEmail.trim())
            model.addAttribute("fileName", filename)

            "index"

        } catch (e: Exception) {

            // Prevents Whitelabel error page
            model.addAttribute(
                "mailError",
                (e.message ?: "Failed to send email. Check SMTP settings.")
            )

            "index"
        }
    }
}