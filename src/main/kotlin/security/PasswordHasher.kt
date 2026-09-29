package com.example.security

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {
    private const val COST = 12

    fun hash(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt(COST))

    fun verify(password: String, hash: String): Boolean =
        try { BCrypt.checkpw(password, hash) } catch (e: Exception) { false }
}