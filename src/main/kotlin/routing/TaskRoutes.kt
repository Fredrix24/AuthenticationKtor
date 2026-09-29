package com.example.routing

import com.example.dto.*
import com.example.service.TaskService
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.taskRoutes() {
    route("/tasks") {
        //Публичные
        get {
            val completed = call.request.queryParameters["completed"]?.toBooleanStrictOrNull()
            val tasks = TaskService.getAll(completed)
            call.respond(HttpStatusCode.OK,
                ApiResponse(true, "Найдено: ${tasks.size}", tasks))
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Некорректный ID"))
            val task = TaskService.getById(id)
            if (task == null) call.respond(HttpStatusCode.NotFound,
                ApiResponse<Unit>(false, "Задача не найдена"))
            else call.respond(HttpStatusCode.OK,
                ApiResponse(true, "Задача найдена", task))
        }

        //Защищённые
        authenticate("auth-jwt") {
            post {
                val req = call.receive<CreateTaskRequest>()
                val task = TaskService.create(req)
                if (task == null) call.respond(HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Title не может быть пустым"))
                else call.respond(HttpStatusCode.Created,
                    ApiResponse(true, "Задача создана", task))
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(false, "Некорректный ID"))
                val req = call.receive<UpdateTaskRequest>()
                val updated = TaskService.update(id, req)
                if (updated == null) call.respond(HttpStatusCode.NotFound,
                    ApiResponse<Unit>(false, "Задача не найдена"))
                else call.respond(HttpStatusCode.OK,
                    ApiResponse(true, "Задача обновлена", updated))
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(false, "Некорректный ID"))
                if (TaskService.delete(id)) call.respond(HttpStatusCode.NoContent)
                else call.respond(HttpStatusCode.NotFound,
                    ApiResponse<Unit>(false, "Задача не найдена"))
            }
        }
    }
}