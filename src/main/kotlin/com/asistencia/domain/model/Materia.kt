package com.asistencia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Materia(
    val id: Int,
    val nombre: String,
    val sigla: String,
    val nivel: Int
)

