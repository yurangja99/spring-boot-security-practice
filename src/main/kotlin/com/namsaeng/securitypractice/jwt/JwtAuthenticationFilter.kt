package com.namsaeng.securitypractice.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.namsaeng.securitypractice.entity.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationFilter(
        @set:JvmName("setAuthenticationManager_")
        @get:JvmName("getAuthenticationManager_")
        var authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {

    /* Trigger when we issue POST request to /login
    We also need to pass in {"username":"minho", "password":"minho123"} in the request body
    * */
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        println("attempt authentication")
        // Grab credentials and map then to LoginViewModel
        var credentials: LoginViewModel? = null
        try {
            credentials = ObjectMapper().readValue(request.inputStream, LoginViewModel::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Create login token
        val authenticationToken = UsernamePasswordAuthenticationToken(
                credentials!!.username,
                credentials.password,
                ArrayList()
        )

        // Authenticate user
        return authenticationManager!!.authenticate(authenticationToken)
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse, chain: FilterChain?, authResult: Authentication) {
        println("successful authentication")
        // Grab principal
        val principal: UserPrincipal = authResult.principal as UserPrincipal

        // Create JWT Token
        val token = JWT.create()
                .withSubject(principal.username)
                .withExpiresAt(Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.toByteArray()))

        // Add token in response
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX.toString() + token)
    }
}