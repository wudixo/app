package com.example.iaido.userdemo.service.impl

import com.example.iaido.userdemo.config.JwtConfig
import com.example.iaido.userdemo.service.JwtService
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtServiceImpl(val jwtConfig: JwtConfig) : JwtService {

    private val logger = LoggerFactory.getLogger(JwtServiceImpl::class.java);

    override fun generateJwtToken(username: String): String {
        return Jwts.builder().setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtConfig.getExirationInMs()))
            .signWith(SignatureAlgorithm.HS512, jwtConfig.secret)
            .compact();
    }

    override fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser()
            .setSigningKey(jwtConfig.secret)
            .parseClaimsJws(token).body.subject;
    }

    override fun validateJwtToken(authToken: String): Boolean {
        try {
            Jwts.parser()
                .setSigningKey(jwtConfig.secret)
                .parseClaimsJws(authToken)
            return true
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        }
        return false
    }
}
