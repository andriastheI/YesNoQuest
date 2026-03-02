package com.dre.yesnoquest.config

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class CsrfModelAdvice {

    @ModelAttribute("_csrf")
    fun csrfToken(token: CsrfToken?): CsrfToken? = token
}