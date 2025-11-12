package com.asistencia.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Horario(
    val id: Int,
    val grupoId: Int,
    val dia: String,
    val horaInicio: String,
    val horaFin: String,
    val materia: String,
    val grupo: String
)

