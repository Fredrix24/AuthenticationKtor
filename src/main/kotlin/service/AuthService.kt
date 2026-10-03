package com.example.service

import com.example.model.User
import com.example.repository.UserRepository
import com.example.security.JwtConfig
import com.example.security.PasswordHasher

object AuthService {
    sealed class RegisterResult {
        data class Success(val user: User) : RegisterResult()
        object UsernameTaken : RegisterResult()
        object InvalidInput : RegisterResult()
    }

    fun register(username: String, password: String): RegisterResult {
        if (username.isBlank() || password.length < 6) return RegisterResult.InvalidInput

        val hash = PasswordHasher.hash(password)
        val user = UserRepository.register(username, hash)
            ?: return RegisterResult.UsernameTaken

        return RegisterResult.Success(user)
    }

    fun login(username: String, password: String): String? {
        val user = UserRepository.findByUsername(username) ?: return null
        if (!PasswordHasher.verify(password, user.passwordHash)) return null
        return JwtConfig.generateToken(user.id, user.username)
    }
}