package com.asistencia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Asistencia(
    val id: Int,
    val alumnoId: Int,
    val grupoId: Int,
    val fecha: String,
    val grupo: String,
    val materiaNombre: String
)

