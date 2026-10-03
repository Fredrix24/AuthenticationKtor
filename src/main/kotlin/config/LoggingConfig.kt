package com.example.config

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.configureLogging() {
    install(CallLogging) {
        level = Level.INFO

        format { call ->
            val status = call.response.status()?.value ?: "-"
            val method = call.request.httpMethod.value
            val path = call.request.path()
            val userAgent = call.request.headers["User-Agent"] ?: "-"
            "[$method] $path -> $status | UA: $userAgent"
        }

        filter { call ->
            !call.request.path().startsWith("/swagger")
        }
    }
}