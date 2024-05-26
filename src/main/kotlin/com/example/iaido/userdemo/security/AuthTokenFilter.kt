package com.example.iaido.userdemo.security

import com.example.iaido.userdemo.service.impl.JwtServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthTokenFilter(val jwtUtils: JwtServiceImpl, val personService: UserDetailsService) : OncePerRequestFilter() {

    val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = parseJwt(request);
            if (jwt.isNotBlank() && jwtUtils.validateJwtToken(jwt)) {
                val username = jwtUtils.getUserNameFromJwtToken(jwt);
                val authPerson = personService.loadUserByUsername(username);
                val authToken = UsernamePasswordAuthenticationToken(authPerson, null, authPerson.authorities)
                SecurityContextHolder.getContext().authentication = authToken;
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String {
        val headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.isNotEmpty() && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return "";
    }
}
