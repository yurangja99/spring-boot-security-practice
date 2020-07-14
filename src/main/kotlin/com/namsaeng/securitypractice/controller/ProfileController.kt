package com.namsaeng.securitypractice.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("profile")
class ProfileController {
    @GetMapping("index")
    fun index(): String {
        return "profile/index"
    }
}