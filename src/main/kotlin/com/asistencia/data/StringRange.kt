package com.asistencia.data

data class StringRange(val start: String, val end: String) {
    fun overlaps(other: StringRange): Boolean {
        return start < other.end && end > other.start
    }
}

