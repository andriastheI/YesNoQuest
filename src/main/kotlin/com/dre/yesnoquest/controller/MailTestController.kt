package com.dre.yesnoquest.controller

import com.dre.yesnoquest.service.MailService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MailTestController(
    private val mailService: MailService
) {

    @GetMapping("/test-mail")
    fun testMail(@RequestParam to: String): String {
        val html = """
            <!doctype html>
            <html>
              <body style="font-family: system-ui; padding: 24px;">
                <h1>Mail test ✅</h1>
                <p>If you got this attachment, your JavaMailSender config works.</p>
              </body>
            </html>
        """.trimIndent()

        mailService.sendHtmlAttachment(
            to = to.trim(),
            subject = "YesNoQuest mail test ✅",
            bodyText = "Attached is a test HTML file. Open it locally.",
            filename = "test.html",
            html = html
        )

        return "Sent test email to $to"
    }
}