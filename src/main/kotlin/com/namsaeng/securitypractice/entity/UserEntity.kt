package com.namsaeng.securitypractice.entity

import javax.persistence.*

@Table(name="user")
@Entity
class UserEntity(
        @Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id: Int = 0,
        @Column(name="username") var username: String,
        @Column(name="password") var password: String,
        @Column(name="roles") var roles: String,
        @Column(name="permissions") var permissions: String,
        @Column(name="active") var active: Int
) {
    fun getRoleList(): List<String> {
        return if (roles.isNotEmpty()) {
            roles.split(",")
        } else listOf()
    }

    fun getPermissionList(): List<String> {
        return if (permissions.length > 0) {
            permissions.split(",")
        } else listOf()
    }
}
