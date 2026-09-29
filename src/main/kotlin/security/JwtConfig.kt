package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    private const val SECRET = "change-me-in-production-256-bit-secret-key"
    const val ISSUER = "http://localhost:8080"
    const val AUDIENCE = "http://localhost:8080"
    const val REALM = "Access to protected routes"
    const val EXPIRES_IN_MS = 3600_000L

    val algorithm: Algorithm = Algorithm.HMAC256(SECRET)

    fun generateToken(userId: Int, username: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRES_IN_MS))
            .sign(algorithm)
    }
}