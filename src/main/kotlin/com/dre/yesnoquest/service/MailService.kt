package com.dre.yesnoquest.service

import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class MailService(private val mailSender: JavaMailSender) {

    fun sendHtmlAttachment(to: String, subject: String, bodyText: String, filename: String, html: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, StandardCharsets.UTF_8.name())
        val fromEmail = System.getenv("MAIL_USER") ?: error("MAIL_USER env var not set")

        helper.setTo(to)
        helper.setFrom(fromEmail)
        helper.setSubject(subject)
        helper.setText(bodyText, false)

        val bytes = html.toByteArray(StandardCharsets.UTF_8)
        helper.addAttachment(filename, ByteArrayResource(bytes), "text/html")

        println("MAIL DEBUG -> sending as: ${helper.mimeMessage.allRecipients?.joinToString()}")
        println("MAIL DEBUG -> from username configured: ${System.getenv("MAIL_USER")}")

        mailSender.send(message)
    }
}