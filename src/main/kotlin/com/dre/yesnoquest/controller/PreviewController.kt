package com.dre.yesnoquest.controller

import com.dre.yesnoquest.model.Theme
import com.dre.yesnoquest.service.HtmlBuilder
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Filename: PreviewController.kt
 * Author: Andrias Zelele
 * Date: 2026-03-03
 *
 * Description:
 * Provides a live HTML preview endpoint for the YesNoQuest application.
 *
 * This controller dynamically generates the themed Yes/No HTML trap
 * without sending an email. It is used by the front-end iframe to allow
 * users to preview their selected theme and question before sending.
 *
 * The endpoint returns raw HTML content rendered directly in the browser.
 *
 * AI Assistance Disclosure:
 * Portions of this project were developed with the assistance of OpenAI's ChatGPT
 * (https://openai.com/chatgpt) for architectural guidance and refinement.
 * All final implementation and integration decisions were completed by the author.
 */
@RestController
class PreviewController(
    private val htmlBuilder: HtmlBuilder
) {

    /**
     * Generates a themed preview of the Yes/No trap.
     *
     * @param question Optional question text entered by the user.
     * @param theme Selected theme enum (NEON, DARK, MINIMAL, RETRO).
     * @return A fully generated HTML page as a String.
     *
     * Produces:
     * - text/html content type so the browser renders it directly.
     */
    @GetMapping("/preview", produces = [MediaType.TEXT_HTML_VALUE])
    fun preview(
        @RequestParam(required = false) question: String?,
        @RequestParam theme: Theme
    ): String {
        return htmlBuilder.buildHtml(question ?: "", theme)
    }
}