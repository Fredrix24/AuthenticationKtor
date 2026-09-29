package com.example.service

import com.example.dto.CreateTaskRequest
import com.example.dto.UpdateTaskRequest
import com.example.model.Task
import com.example.repository.TaskRepository

object TaskService {
    fun getAll(completed: Boolean?): List<Task> =
        if (completed == null) TaskRepository.getAll()
        else TaskRepository.getAll().filter { it.completed == completed }

    fun getById(id: Int): Task? = TaskRepository.getById(id)

    fun create(request: CreateTaskRequest): Task? {
        if (request.title.isBlank()) return null
        return TaskRepository.addTask(request.title, request.description)
    }

    fun update(id: Int, request: UpdateTaskRequest): Task? =
        TaskRepository.updateTask(id, request.title, request.description, request.completed)

    fun delete(id: Int): Boolean = TaskRepository.deleteTask(id)
}