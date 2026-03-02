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

@Controller
class HomeController(
    private val htmlBuilder: HtmlBuilder,
    private val mailService: MailService
) {

    @GetMapping("/")
    fun index(auth: Authentication, model: Model): String {
        model.addAttribute("username", auth.name)
        model.addAttribute("themes", Theme.entries)
        return "index"
    }

    @PostMapping("/send")
    fun send(
        auth: Authentication,
        @RequestParam recipientEmail: String,
        @RequestParam question: String,
        @RequestParam theme: Theme,
        model: Model
    ): String {

        model.addAttribute("username", auth.name)
        model.addAttribute("themes", Theme.entries)

        return try {
            val html = htmlBuilder.buildHtml(question, theme)
            val filename = htmlBuilder.buildFilename(theme)

            mailService.sendHtmlAttachment(
                to = recipientEmail.trim(),
                subject = "You have a Yes/No question 😈",
                bodyText = "Attached is an HTML file. Download it and open locally. Try clicking NO if you dare.",
                filename = filename,
                html = html
            )

            model.addAttribute("sentTo", recipientEmail.trim())
            model.addAttribute("fileName", filename)
            "index"
        } catch (e: Exception) {
            // This prevents the Whitelabel 500 page
            model.addAttribute("mailError", (e.message ?: "Failed to send email. Check SMTP settings."))
            "index"
        }
    }
}