package org.study.rotibow.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val name: String, val email: String)

@Serializable
data class UserList(val users: List<User>)

val users = mutableListOf(
    User(1, "Rosyid", "rosyid@gmail.com"),
    User(2, "Isma", "isma@gmail.com")
)

fun Application.userRoutes() {
    routing {
        get("/users") {
            call.respond(UserList(users))
        }

        get("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val user = users.find { it.id == id }
            if (user != null) {
                call.respond(user)
            } else {
                call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }

        post("/users") {
            val newUser = call.receive<User>()
            users.add(newUser)
            call.respond("User added successfully")
        }

        put("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val updateUser = call.receive<User>()
            val index = users.indexOfFirst { it.id == id }
            if (index != -1) {
                users[index] = updateUser
                call.respond("User updated successfully")
            } else {
                call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }

        delete("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val removed = users.removeIf { it.id == id }
            if (removed) {
                call.respondText("User deleted successfully")
            } else {
                call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}