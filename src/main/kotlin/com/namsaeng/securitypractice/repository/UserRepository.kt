package com.namsaeng.securitypractice.repository

import com.namsaeng.securitypractice.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserEntity, Int> {
    fun findByUsername(username: String): UserEntity
}