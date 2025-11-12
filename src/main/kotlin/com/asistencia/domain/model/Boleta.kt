package com.asistencia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Boleta(
    val id: Int,
    val alumnoId: Int,
    val grupoId: Int,
    val fecha: String,
    val semestre: Int,
    val gestion: Int,
    val grupo: String,
    val materiaNombre: String,
    val dia: String,
    val horario: String
)

