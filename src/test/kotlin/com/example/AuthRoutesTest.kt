package com.example

import com.example.repository.TaskRepository
import com.example.repository.UserRepository
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class AuthRoutesTest {

    @BeforeTest
    fun setUp() {
        UserRepository.clear()
        TaskRepository.clear()
    }

    @Test
    fun `register returns 201 and does not expose password`() = testApplication {
        application { module() }
        val name = "testuser_${System.nanoTime()}"
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"$name","password":"secret123"}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertFalse(response.bodyAsText().contains("secret123"))
    }

    @Test
    fun `register duplicate returns 409`() = testApplication {
        application { module() }
        val name = "dupuser_${System.nanoTime()}"
        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"$name","password":"secret123"}""")
        }
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"$name","password":"secret123"}""")
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun `login returns token for valid credentials`() = testApplication {
        application { module() }
        val name = "loginuser_${System.nanoTime()}"
        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"$name","password":"secret123"}""")
        }
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"$name","password":"secret123"}""")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val body = Json.Default.parseToJsonElement(response.bodyAsText()).jsonObject
        assertNotNull(body["data"]?.jsonObject?.get("token"))
    }

    @Test
    fun `login with wrong password returns 401`() = testApplication {
        application { module() }
        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"nonexistent","password":"wrong"}""")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}