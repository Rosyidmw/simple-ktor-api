package org.study.rotibow.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.study.rotibow.models.Users

@Serializable
data class User(val id: Int? = null, val name: String, val email: String, val password: String)

@Serializable
data class UserList(val users: List<User>)

val users = mutableListOf(
    User(1, "Rosyid", "rosyid@gmail.com", "1234"),
    User(2, "Isma", "isma@gmail.com", "1234")
)

fun Application.userRoutes() {
    routing {

        get("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val user = users.find { it.id == id }
            if (user != null) {
                call.respond(user)
            } else {
                call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }

        get("/users") {
            val userList = transaction {
                Users.selectAll().map {
                    User(
                        id = it[Users.id].value,
                        name = it[Users.name],
                        email = it[Users.email],
                        password = it[Users.password]
                    )
                }
            }
            call.respond(UserList(userList))
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

        post("/register") {
            val request = call.receive<User>()
            transaction {
                Users.insert {
                    it[name] = request.name
                    it[email] = request.email
                    it[password] = request.password
                }
            }
            call.respondText("User registered successfully")
        }

        post("/login") {
            val request = call.receive<User>()
            val user = transaction {
                Users
                    .selectAll()
                    .where { (Users.email eq request.email) and (Users.password eq request.password) }
                    .singleOrNull()
            }
            if (user != null) {
                call.respondText("Login success! Welcome ${user[Users.name]}")
            } else {
                call.respondText("Invalid email or password", status = HttpStatusCode.Unauthorized)
            }
        }
    }
}