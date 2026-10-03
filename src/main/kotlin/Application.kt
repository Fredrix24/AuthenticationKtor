package com.example

import com.example.config.*
import com.example.routing.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    configureLogging()
    configureSerialization()
    configureStatusPages()
    configureSecurity()
    configureSwagger()
    configureRouting()
}