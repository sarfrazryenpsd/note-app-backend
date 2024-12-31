package com.example.routes

import com.example.data.model.Note
import com.example.data.model.SimpleResponse
import com.example.data.model.User
import com.example.repository.Repo
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import io.ktor.server.response.*

const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTE = "$NOTES/create"
const val UPDATE_NOTE = "$NOTES/update"
const val DELETE_NOTE = "$NOTES/delete"

@Resource(NOTES)
class NoteGetRoute

@Resource(DELETE_NOTE)
class NoteDeleteRoute

@Resource(UPDATE_NOTE)
class NoteUpdateRoute

@Resource(CREATE_NOTE)
class NoteCreateRoute


fun Route.noteRoutes(
    db: Repo,
    hashFunction: (String) -> String
){
    authenticate("jwt") {

        post<NoteCreateRoute> {

            val note = try {
                call.receive<Note>()
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }

            try{
                val email = call.principal<User>()!!.email
                db.addNote(note, email)

                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note added successfully"))
            } catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message?: "Some Problem Occur"))
            }

        }

        get<NoteGetRoute> {

            try {
                val email = call.principal<User>()!!.email
                val notes = db.getAllNotes(email)
                call.respond(HttpStatusCode.OK, notes)
            } catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, emptyList<Note>())
            }

        }

        post<NoteUpdateRoute> {

            val note = try {
                call.receive<Note>()
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }

            try{
                val email = call.principal<User>()!!.email
                db.updateNote(note, email)

                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Updated Successfully"))
            } catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message?: "Some Problem Occur"))
            }

        }

        delete<NoteDeleteRoute> {

            val noteId = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "QueryParameter:id is not present"))
                return@delete
            }

            try {
                val email = call.principal<User>()!!.email
                db.deleteNote(noteId, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully"))
            } catch (e: Exception){
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message?: "Some Problem Occur"))
            }

        }

    }
}