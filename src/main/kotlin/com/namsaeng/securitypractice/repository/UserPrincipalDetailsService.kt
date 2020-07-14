package com.namsaeng.securitypractice.repository

import com.namsaeng.securitypractice.entity.UserEntity
import com.namsaeng.securitypractice.entity.UserPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserPrincipalDetailsService(
        var userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserEntity = userRepository.findByUsername(username)
        return UserPrincipal(user)
    }
}