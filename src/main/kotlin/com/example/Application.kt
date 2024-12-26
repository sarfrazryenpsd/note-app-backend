package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.data.model.User
import com.example.repository.DatabaseFactory
import com.example.repository.repo
import com.example.routes.userRoute
import io.ktor.resources.Resources
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
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
    val db = repo()
    val jwtService = JwtService()
    val hashFunction = {s: String -> hash(s) }

    install(Sessions){
        cookie<MySession>("MY_SESSION"){
            cookie.extensions["SameSite"] = "lax"
        }
    }
    install(Authentication){

    }

    install(ContentNegotiation){
        json()
    }
    install(Resources){

    }
    routing {
        /*get("/") {
            call.respondText("Hello World!")
        }*/

        userRoute(db, jwtService, hashFunction)

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
data class MySession(val id: String, val count: Int)
