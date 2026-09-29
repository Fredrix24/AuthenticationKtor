package com.example.repository

import com.example.model.User
import java.util.concurrent.atomic.AtomicInteger

object UserRepository {
    private val users = mutableMapOf<Int, User>()
    private val usernameIndex = mutableMapOf<String, Int>()
    private val idCounter = AtomicInteger(1)

    fun findByUsername(username: String): User? {
        val id = usernameIndex[username] ?: return null
        return users[id]
    }

    fun existsByUsername(username: String): Boolean = usernameIndex.containsKey(username)

    fun save(username: String, passwordHash: String): User {
        val id = idCounter.getAndIncrement()
        val user = User(id, username, passwordHash)
        users[id] = user
        usernameIndex[username] = id
        return user
    }
}