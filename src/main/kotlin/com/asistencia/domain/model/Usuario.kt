package com.asistencia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val nombres: String,
    val apellidos: String,
    val registro: String,
    val rol: String,
    val username: String
    // val contrasena: String  // Comentado por seguridad
)

