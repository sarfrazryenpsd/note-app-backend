package com.example

import com.example.repository.DatabaseFactory
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
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

    install(Sessions){
        cookie<MySession>("MY_SESSION"){
            cookie.extensions["SameSite"] = "lax"
        }
    }
    install(Authentication){

    }

    install(ContentNegotiation){
        gson {

        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/note/{id}"){
            val id = call.parameters["id"]
            call.respond("$id")
        }
        get("/note"){
            val id = call.request.queryParameters["id"]
            call.respond("$id")
        }
        route("/notes"){
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
        }
    }
}

@Serializable
data class MySession(val id: String, val count: Int)
