package com.example.repository

import com.example.model.Task
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object TaskRepository {
    private val tasks = ConcurrentHashMap<Int, Task>()
    private val idCounter = AtomicInteger(1)

    init {
        addTask("Изучить Ktor", "Разобраться с маршрутизацией")
        addTask("Написать CRUD", "Реализовать 5 маршрутов")
    }

    fun getAll(): List<Task> = tasks.values.toList()
    fun getById(id: Int): Task? = tasks[id]

    fun addTask(title: String, description: String): Task {
        val id = idCounter.getAndIncrement()
        return Task(id, title, description).also { tasks[id] = it }
    }

    fun updateTask(id: Int, title: String?, description: String?, completed: Boolean?): Task? {
        val existing = tasks[id] ?: return null
        val updated = existing.copy(
            title = title ?: existing.title,
            description = description ?: existing.description,
            completed = completed ?: existing.completed
        )
        tasks[id] = updated
        return updated
    }

    fun deleteTask(id: Int): Boolean = tasks.remove(id) != null

    fun clear() {
        tasks.clear()
        idCounter.set(1)
        addTask("Изучить Ktor", "Разобраться с маршрутизацией")
        addTask("Написать CRUD", "Реализовать 5 маршрутов")
    }
}