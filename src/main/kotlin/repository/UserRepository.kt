package com.example.repository

import com.example.model.User
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object UserRepository {
    private val users = ConcurrentHashMap<Int, User>()
    private val usernameIndex = ConcurrentHashMap<String, Int>()
    private val idCounter = AtomicInteger(1)

    fun findByUsername(username: String): User? {
        val id = usernameIndex[username] ?: return null
        return users[id]
    }

    fun existsByUsername(username: String): Boolean = usernameIndex.containsKey(username)

    @Synchronized
    fun register(username: String, passwordHash: String): User? {
        if (existsByUsername(username)) return null
        val id = idCounter.getAndIncrement()
        val user = User(id, username, passwordHash)
        users[id] = user
        usernameIndex[username] = id
        return user
    }

    fun clear() {
        users.clear()
        usernameIndex.clear()
        idCounter.set(1)
    }
}