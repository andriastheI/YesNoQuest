/**
 * Filename: MailService.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Service responsible for sending email messages with HTML attachments
 * using Spring's JavaMailSender.
 *
 * This service:
 * - Creates MIME email messages
 * - Attaches dynamically generated HTML content
 * - Uses UTF-8 encoding
 * - Reads sender email from environment variable (MAIL_USER)
 *
 * Responsibilities:
 * - Construct MIME message
 * - Attach HTML file as downloadable attachment
 * - Send message through configured SMTP server
 *
 * Security Notes:
 * - MAIL_USER must be configured as an environment variable
 * - SMTP credentials must be properly secured
 * - This service does not validate email format (handled upstream)
 */

package com.dre.yesnoquest.service

import org.springframework.core.io.ByteArrayResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

/**
 * Service handling email transmission for YesNoQuest.
 *
 * @property mailSender Spring-provided JavaMailSender configured via application.properties
 */
@Service
class MailService(private val mailSender: JavaMailSender) {

    /**
     * Sends an email with an HTML file attachment.
     *
     * Flow:
     * 1) Create MIME message
     * 2) Set recipient, sender, subject, and plain-text body
     * 3) Convert HTML string into UTF-8 byte array
     * 4) Attach as text/html file
     * 5) Send via configured SMTP server
     *
     * @param to Recipient email address
     * @param subject Email subject line
     * @param bodyText Plain-text body message
     * @param filename Name of attached HTML file
     * @param html Full HTML document string
     */
    fun sendHtmlAttachment(
        to: String,
        subject: String,
        bodyText: String,
        filename: String,
        html: String
    ) {

        val message = mailSender.createMimeMessage()

        // true = multipart (needed for attachments)
        val helper = MimeMessageHelper(message, true, StandardCharsets.UTF_8.name())

        // Sender email pulled from environment variable
        val fromEmail = System.getenv("MAIL_USER")
            ?: error("MAIL_USER env var not set")

        helper.setTo(to)
        helper.setFrom(fromEmail)
        helper.setSubject(subject)

        // false = plain text body (not HTML body)
        helper.setText(bodyText, false)

        // Convert HTML string to bytes
        val bytes = html.toByteArray(StandardCharsets.UTF_8)

        // Attach file with content type text/html
        helper.addAttachment(filename, ByteArrayResource(bytes), "text/html")

        mailSender.send(message)
    }
}