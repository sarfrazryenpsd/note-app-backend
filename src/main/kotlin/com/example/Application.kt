package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.DatabaseFactory
import com.example.repository.Repo
import com.example.routes.noteRoutes
import com.example.routes.userRoutes
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import kotlin.collections.set


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused")
@JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JwtService()
    val hashFunction = {s: String -> hash(s) }

    install(Sessions){
        cookie<MySession>("MY_SESSION"){
            cookie.extensions["SameSite"] = "lax"
        }
    }
    install(Authentication){
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "Note Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUser(email)
                user
            }
        }
    }

    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
        }
    }
    install(Resources)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        userRoutes(db, jwtService, hashFunction)
        noteRoutes(db, hashFunction)

        /*route("/notes"){
            route("/create"){
                post {
                    val body = call.receive<String>()
                    call.respond(body)
                }
            }
            delete {
                val body = call.receive<String>()
                call.respond(body)
            }
        }*/
    }
}

@Serializable
data class MySession(val count: Int = 0)
