package com.example

import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import kotlin.collections.set

@Suppress("unused")
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(testing: Boolean = false) {
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
    configureRouting()
}

@Serializable
data class MySession(val id: String, val count: Int)
