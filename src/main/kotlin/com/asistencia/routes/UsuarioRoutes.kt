package com.asistencia.routes

import com.asistencia.data.AppDatabase
import com.asistencia.domain.model.Usuario
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)

@Serializable
data class LoginRequest(
    val usuario: String,
    val password: String
)

@Serializable
data class CrearUsuarioRequest(
    val nombres: String,
    val apellidos: String,
    val registro: String,
    val rol: String,
    val username: String,
    val contrasena: String
)

fun Route.usuarioRoutes(database: AppDatabase) {
    
    route("/api") {
        
        // POST /api/login - Validar usuario
        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                
                val usuario = database.validarUsuario(request.usuario, request.password)
                
                if (usuario != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            data = usuario
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Usuario>(
                            success = false,
                            message = "Usuario o contraseña incorrectos"
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Usuario>(
                        success = false,
                        message = "Error en la solicitud: ${e.message}"
                    )
                )
            }
        }
        
        // GET /api/usuarios - Listar todos los usuarios
        get("/usuarios") {
            try {
                val usuarios = database.obtenerUsuarios()
                
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        data = usuarios
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<List<Usuario>>(
                        success = false,
                        message = "Error al obtener usuarios: ${e.message}"
                    )
                )
            }
        }
        
        // POST /api/usuarios - Crear nuevo usuario
        post("/usuarios") {
            try {
                val request = call.receive<CrearUsuarioRequest>()
                
                // Validaciones básicas
                if (request.nombres.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Usuario>(
                            success = false,
                            message = "El nombre es requerido"
                        )
                    )
                    return@post
                }
                
                if (request.apellidos.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Usuario>(
                            success = false,
                            message = "Los apellidos son requeridos"
                        )
                    )
                    return@post
                }
                
                if (request.username.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Usuario>(
                            success = false,
                            message = "El username es requerido"
                        )
                    )
                    return@post
                }
                
                if (request.contrasena.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Usuario>(
                            success = false,
                            message = "La contraseña es requerida"
                        )
                    )
                    return@post
                }
                
                // Agregar usuario
                database.agregarUsuario(
                    nombres = request.nombres,
                    apellidos = request.apellidos,
                    registro = request.registro,
                    rol = request.rol,
                    username = request.username,
                    contrasena = request.contrasena
                )
                
                // Obtener el usuario recién creado para devolverlo
                val usuarioCreado = database.validarUsuario(request.username, request.contrasena)
                
                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(
                        success = true,
                        data = usuarioCreado,
                        message = "Usuario creado exitosamente"
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ApiResponse<Usuario>(
                        success = false,
                        message = "Error al crear usuario: ${e.message}"
                    )
                )
            }
        }
    }
}

