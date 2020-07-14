package com.namsaeng.securitypractice.repository

import com.namsaeng.securitypractice.entity.UserEntity
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class DBInit(
        val userRepository: UserRepository,
        val passwordEncoder: PasswordEncoder
): CommandLineRunner {
    @Throws(Exception::class)
    override fun run(vararg args: String?) {
        // Delete all
        userRepository.deleteAll()

        // create users
        val minho = UserEntity(0, "minho", passwordEncoder.encode("minho123"), "USER", "", 1)
        val admin = UserEntity(0, "admin", passwordEncoder.encode("admin123"), "ADMIN", "ACCESS_TEST1,ACCESS_TEST2", 1)
        val manager = UserEntity(0, "manager", passwordEncoder.encode("manager123"), "MANAGER", "ACCESS_TEST1", 1)
        val users: List<UserEntity> = listOf(minho, admin, manager)

        // save to db
        userRepository.saveAll(users)
    }
}