package com.example.routing

import com.example.dto.*
import com.example.service.AuthService
import com.example.security.JwtConfig
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    route("/auth") {

        post("/register") {
            val req = call.receive<RegisterRequest>()
            when (val result = AuthService.register(req.username, req.password)) {
                is AuthService.RegisterResult.Success -> call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(
                        success = true,
                        message = "Пользователь зарегистрирован",
                        data = RegisterResponse(result.user.id, result.user.username)
                    )
                )
                AuthService.RegisterResult.UsernameTaken -> call.respond(
                    HttpStatusCode.Conflict,
                    ApiResponse<Unit>(false, "Логин уже занят")
                )
                AuthService.RegisterResult.InvalidInput -> call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(false, "Логин пуст или пароль короче 6 символов")
                )
            }
        }

        post("/login") {
            val req = call.receive<LoginRequest>()
            val token = AuthService.login(req.username, req.password)
            if (token == null) {
                call.respond(HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(false, "Неверный логин или пароль"))
            } else {
                call.respond(HttpStatusCode.OK,
                    ApiResponse(true, "Вход выполнен",
                        AuthResponse(token, JwtConfig.EXPIRES_IN_MS / 1000)))
            }
        }
    }
}