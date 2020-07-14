package com.namsaeng.securitypractice.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


// UserEntity를 인자로 받아 UserDetails(spring security가 사용하는 폼) 반환
class UserPrincipal(
        var user: UserEntity
): UserDetails {
    // UserEntity의 authority와 role들을 spring security에 맞게 변환
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities: MutableList<GrantedAuthority> = mutableListOf()
        val permissions: List<String> = user.getPermissionList()
        for (p in permissions) {
            val authority: GrantedAuthority = SimpleGrantedAuthority(p)
            authorities.add(authority)
        }
        val roles: List<String> = user.getRoleList()
        for (p in roles) {
            val authority: GrantedAuthority = SimpleGrantedAuthority("ROLE_$p")
            authorities.add(authority)
        }
        return authorities
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return user.active == 1
    }
}