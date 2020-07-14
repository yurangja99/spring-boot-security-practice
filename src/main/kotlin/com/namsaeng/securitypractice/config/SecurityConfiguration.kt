package com.namsaeng.securitypractice.config

import com.namsaeng.securitypractice.jwt.JwtAuthenticationFilter
import com.namsaeng.securitypractice.jwt.JwtAuthorizationFilter
import com.namsaeng.securitypractice.repository.UserPrincipalDetailsService
import com.namsaeng.securitypractice.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfiguration(
        val userPrincipalDetailsService: UserPrincipalDetailsService,
        val userRepository: UserRepository
): WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
                /*.inMemoryAuthentication()   // in memory에 유저 정보 저장
                .withUser("admin").password(passwordEncoder().encode("admin"))
                .roles("ADMIN").authorities("ACCESS_TEST1", "ACCESS_TEST2")
                .and()
                .withUser("user").password(passwordEncoder().encode("user"))
                .roles("USER")
                .and()
                .withUser("manager").password(passwordEncoder().encode("manager"))
                .roles("MANAGER").authorities("ACCESS_TEST1")*/
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider: DaoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService)
        return daoAuthenticationProvider
    }

    // JWT에서 좀 달라짐.
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(JwtAuthenticationFilter(authenticationManager()))
                .addFilter(JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                //.anyRequest().authenticated() // 모든 요청에 대해 인증된 사람만!
                //.antMatchers("/index.html").permitAll() // antMatchers 작성할 때는 순서 고려
                //.antMatchers("/profile/**").authenticated()
                //.antMatchers("/admin/**").hasRole("ADMIN")
                //.antMatchers("/management/**").hasAnyRole("ADMIN", "MANAGER")
                //.antMatchers("/test").authenticated()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/public/management/*").hasRole("MANAGER")
                .antMatchers("/api/public/admin/*").hasRole("ADMIN")
                .anyRequest().authenticated()
                //.and()
                //.httpBasic()    // 인증방식: HTTP Basic Authentication
    }

    @Bean   // 프로젝트 어디에서나 사용할 수 있도록 함.
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}