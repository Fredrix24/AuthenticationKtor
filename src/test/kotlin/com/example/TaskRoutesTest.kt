package com.example

import com.example.repository.TaskRepository
import com.example.repository.UserRepository
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskRoutesTest {

    @BeforeTest
    fun setUp() {
        UserRepository.clear()
        TaskRepository.clear()
    }

    @Test
    fun `GET tasks returns 200`() = testApplication {
        application { module() }
        val response = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `GET task by id returns 404 for unknown`() = testApplication {
        application { module() }
        val response = client.get("/tasks/999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET task by id returns 400 for invalid id`() = testApplication {
        application { module() }
        val response = client.get("/tasks/abc")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST tasks without token returns 401`() = testApplication {
        application { module() }
        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Test","description":"Desc"}""")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}