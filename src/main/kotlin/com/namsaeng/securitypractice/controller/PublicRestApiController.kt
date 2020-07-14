package com.namsaeng.securitypractice.controller

import com.namsaeng.securitypractice.entity.UserEntity
import com.namsaeng.securitypractice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping

import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/public")
class PublicRestApiController {
    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("test")
    fun test(): String {
        return "API Test 1"
    }

    @GetMapping("management/reports")
    fun reports(): String {
        return "management reports"
    }

    @GetMapping("admin/users")
    fun allUsers(): List<UserEntity> {
        return userRepository.findAll()
    }
}