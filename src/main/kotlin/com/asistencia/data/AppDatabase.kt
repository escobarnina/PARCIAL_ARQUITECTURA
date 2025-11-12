package com.asistencia.data

import com.asistencia.domain.model.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.text.SimpleDateFormat
import java.util.*

class AppDatabase(private val dbPath: String = "asistenciadb.db") {
    
    private var connection: Connection? = null

    init {
        // Cargar el driver JDBC de SQLite
        Class.forName("org.sqlite.JDBC")
        // Inicializar la conexión
        getConnection()
        // Crear las tablas si no existen
        onCreate()
    }

    private fun getConnection(): Connection {
        if (connection == null || connection?.isClosed == true) {
            connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
        }
        return connection!!
    }

    private fun onCreate() {
        val db = getConnection()
        
        // Usuarios
        db.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombres TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                username TEXT UNIQUE NOT NULL,
                contrasena TEXT NOT NULL,
                registro TEXT NOT NULL,
                rol TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Materias
        db.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS materias (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                sigla TEXT NOT NULL UNIQUE,
                nivel INTEGER NOT NULL
            )
            """
        )

        // Grupos
        db.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS grupos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                materia_id INTEGER NOT NULL,
                materia_nombre TEXT NOT NULL,
                docente_id INTEGER NOT NULL,
                docente_nombre TEXT NOT NULL,
                grupo TEXT NOT NULL,
                semestre INTEGER NOT NULL,
                gestion INTEGER NOT NULL,
                capacidad INTEGER NOT NULL,
                nro_inscritos INTEGER DEFAULT 0,
                FOREIGN KEY(materia_id) REFERENCES materias(id),
                FOREIGN KEY(docente_id) REFERENCES usuarios(id)
            )
            """
        )

        // Horarios
        db.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS horarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                grupo_id INTEGER NOT NULL,
                dia TEXT NOT NULL,
                hora_inicio TEXT NOT NULL,
                hora_fin TEXT NOT NULL,
                FOREIGN KEY(grupo_id) REFERENCES grupos(id)
            )
            """
        )

        // Boletas de Inscripción
        db.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS boletas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                alumno_id INTEGER NOT NULL,
                grupo_id INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                semestre INTEGER NOT NULL,
                gestion INTEGER NOT NULL,
                FOREIGN KEY(alumno_id) REFERENCES usuarios(id),
                FOREIGN KEY(grupo_id) REFERENCES grupos(id)
            )
            """
        )

        // Asistencias
        db.createStatement().execute(
            """
            CREATE TABLE IF NOT EXISTS asistencias (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                alumno_id INTEGER NOT NULL,
                grupo_id INTEGER NOT NULL,
                fecha TEXT NOT NULL,
                FOREIGN KEY(alumno_id) REFERENCES usuarios(id),
                FOREIGN KEY(grupo_id) REFERENCES grupos(id)
            )
            """
        )

        // Insertar datos de prueba solo si la tabla está vacía
        val countStmt = db.createStatement()
        val rs = countStmt.executeQuery("SELECT COUNT(*) as count FROM usuarios")
        if (rs.next() && rs.getInt("count") == 0) {
            db.createStatement().execute(
                """
                INSERT INTO usuarios(nombres, apellidos, username, contrasena, registro, rol)
                VALUES
                ('Ana', 'Alumno', 'alumno1', '1234', '211882', 'Alumno'),
                ('Juan', 'Alumno', 'alumno2', '1234', '212732', 'Alumno'),
                ('Carlos', 'Alumno', 'alumno3', '1234', '210882', 'Alumno'),
                ('Marcos', 'Docente', 'docente1', '1234', '342232', 'Docente'),
                ('Maria', 'Docente', 'docente2', '1234', '45532', 'Docente'),
                ('Julia', 'Docente', 'docente3', '1234', '56322', 'Docente'),
                ('Admin', 'Admin', 'admin1', '1234', '11111', 'Admin')
                """.trimIndent()
            )
            println("Datos de prueba insertados")
        }
    }

    // ===== USUARIOS =====

    fun validarUsuario(username: String, contrasena: String): Usuario? {
        val stmt = getConnection().prepareStatement(
            "SELECT * FROM usuarios WHERE username=? AND contrasena=?"
        )
        stmt.setString(1, username)
        stmt.setString(2, contrasena)
        
        stmt.executeQuery().use { cursor ->
            return if (cursor.next()) {
                Usuario(
                    id = cursor.getInt("id"),
                    nombres = cursor.getString("nombres"),
                    apellidos = cursor.getString("apellidos"),
                    registro = cursor.getString("registro"),
                    rol = cursor.getString("rol"),
                    username = cursor.getString("username")
                )
            } else {
                null
            }
        }
    }

    fun obtenerUsuarios(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val stmt = getConnection().createStatement()
        stmt.executeQuery(
            "SELECT id, nombres, apellidos, registro, rol, username FROM usuarios ORDER BY id DESC"
        ).use { c ->
            while (c.next()) {
                lista.add(
                    Usuario(
                        id = c.getInt("id"),
                        nombres = c.getString("nombres"),
                        apellidos = c.getString("apellidos"),
                        registro = c.getString("registro"),
                        rol = c.getString("rol"),
                        username = c.getString("username")
                    )
                )
            }
        }
        return lista
    }

    fun obtenerDocentes(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val stmt = getConnection().createStatement()
        stmt.executeQuery(
            "SELECT id, nombres, apellidos, registro, rol, username FROM usuarios WHERE rol LIKE '%docente%' ORDER BY id DESC"
        ).use { c ->
            while (c.next()) {
                lista.add(
                    Usuario(
                        id = c.getInt("id"),
                        nombres = c.getString("nombres"),
                        apellidos = c.getString("apellidos"),
                        registro = c.getString("registro"),
                        rol = c.getString("rol"),
                        username = c.getString("username")
                    )
                )
            }
        }
        return lista
    }

    fun agregarUsuario(
        nombres: String,
        apellidos: String,
        registro: String,
        rol: String,
        username: String,
        contrasena: String
    ) {
        val stmt = getConnection().prepareStatement(
            "INSERT INTO usuarios(nombres, apellidos, registro, rol, username, contrasena) VALUES (?,?,?,?,?,?)"
        )
        stmt.setString(1, nombres)
        stmt.setString(2, apellidos)
        stmt.setString(3, registro)
        stmt.setString(4, rol)
        stmt.setString(5, username)
        stmt.setString(6, contrasena)
        stmt.executeUpdate()
    }

    fun eliminarUsuario(id: Int) {
        val stmt = getConnection().prepareStatement("DELETE FROM usuarios WHERE id=?")
        stmt.setInt(1, id)
        stmt.executeUpdate()
    }

    fun actualizarUsuario(id: Int, nombres: String, apellidos: String, rol: String) {
        val stmt = getConnection().prepareStatement(
            "UPDATE usuarios SET nombres=?, apellidos=?, rol=? WHERE id=?"
        )
        stmt.setString(1, nombres)
        stmt.setString(2, apellidos)
        stmt.setString(3, rol)
        stmt.setInt(4, id)
        stmt.executeUpdate()
    }

    // ===== MATERIAS =====

    fun obtenerMaterias(): List<Materia> {
        val lista = mutableListOf<Materia>()
        val stmt = getConnection().createStatement()
        stmt.executeQuery(
            "SELECT id, nombre, sigla, nivel FROM materias ORDER BY id DESC"
        ).use { c ->
            while (c.next()) {
                lista.add(
                    Materia(
                        id = c.getInt("id"),
                        nombre = c.getString("nombre"),
                        sigla = c.getString("sigla"),
                        nivel = c.getInt("nivel")
                    )
                )
            }
        }
        return lista
    }

    fun agregarMateria(nombre: String, sigla: String, nivel: Int) {
        val stmt = getConnection().prepareStatement(
            "INSERT INTO materias(nombre, sigla, nivel) VALUES (?,?,?)"
        )
        stmt.setString(1, nombre)
        stmt.setString(2, sigla)
        stmt.setInt(3, nivel)
        stmt.executeUpdate()
    }

    fun eliminarMateria(id: Int) {
        val stmt = getConnection().prepareStatement("DELETE FROM materias WHERE id=?")
        stmt.setInt(1, id)
        stmt.executeUpdate()
    }

    // ===== GRUPOS =====

    fun obtenerGrupos(): List<Grupo> {
        val lista = mutableListOf<Grupo>()
        val stmt = getConnection().createStatement()
        stmt.executeQuery(
            "SELECT id, materia_id, materia_nombre, docente_id, docente_nombre, semestre, gestion, capacidad, nro_inscritos, grupo FROM grupos"
        ).use { c ->
            while (c.next()) {
                lista.add(
                    Grupo(
                        id = c.getInt("id"),
                        materiaId = c.getInt("materia_id"),
                        materiaNombre = c.getString("materia_nombre"),
                        docenteId = c.getInt("docente_id"),
                        docenteNombre = c.getString("docente_nombre"),
                        semestre = c.getInt("semestre"),
                        gestion = c.getInt("gestion"),
                        capacidad = c.getInt("capacidad"),
                        nroInscritos = c.getInt("nro_inscritos"),
                        grupo = c.getString("grupo")
                    )
                )
            }
        }
        return lista
    }

    fun agregarGrupo(
        materiaId: Int,
        materiaNombre: String,
        docenteId: Int,
        docenteNombre: String,
        semestre: Int,
        gestion: Int,
        capacidad: Int,
        grupo: String
    ) {
        val stmt = getConnection().prepareStatement(
            "INSERT INTO grupos(materia_id, materia_nombre, docente_id, docente_nombre, semestre, gestion, capacidad, grupo) VALUES (?,?,?,?,?,?,?,?)"
        )
        stmt.setInt(1, materiaId)
        stmt.setString(2, materiaNombre)
        stmt.setInt(3, docenteId)
        stmt.setString(4, docenteNombre)
        stmt.setInt(5, semestre)
        stmt.setInt(6, gestion)
        stmt.setInt(7, capacidad)
        stmt.setString(8, grupo)
        stmt.executeUpdate()
    }

    fun eliminarGrupo(id: Int) {
        val stmt = getConnection().prepareStatement("DELETE FROM grupos WHERE id=?")
        stmt.setInt(1, id)
        stmt.executeUpdate()
    }

    // ===== HORARIOS =====

    fun obtenerHorarios(): List<Horario> {
        val lista = mutableListOf<Horario>()
        val stmt = getConnection().createStatement()
        stmt.executeQuery(
            """
            SELECT 
                h.id, 
                h.grupo_id, 
                h.dia, 
                h.hora_inicio, 
                h.hora_fin, 
                g.materia_nombre,
                g.grupo
            FROM 
                horarios h JOIN grupos g ON h.grupo_id = g.id 
            ORDER BY g.id DESC
            """.trimIndent()
        ).use { c ->
            while (c.next()) {
                lista.add(
                    Horario(
                        id = c.getInt("id"),
                        grupoId = c.getInt("grupo_id"),
                        dia = c.getString("dia"),
                        horaInicio = c.getString("hora_inicio"),
                        horaFin = c.getString("hora_fin"),
                        materia = c.getString("materia_nombre"),
                        grupo = c.getString("grupo")
                    )
                )
            }
        }
        return lista
    }

    fun agregarHorario(grupoId: Int, dia: String, horaInicio: String, horaFin: String) {
        val stmt = getConnection().prepareStatement(
            "INSERT INTO horarios(grupo_id, dia, hora_inicio, hora_fin) VALUES (?,?,?,?)"
        )
        stmt.setInt(1, grupoId)
        stmt.setString(2, dia)
        stmt.setString(3, horaInicio)
        stmt.setString(4, horaFin)
        stmt.executeUpdate()
    }

    fun eliminarHorario(id: Int) {
        val stmt = getConnection().prepareStatement("DELETE FROM horarios WHERE id=?")
        stmt.setInt(1, id)
        stmt.executeUpdate()
    }

    // ===== BOLETAS =====

    fun obtenerBoletasPorAlumno(alumnoId: Int): List<Boleta> {
        val lista = mutableListOf<Boleta>()
        val stmt = getConnection().prepareStatement(
            """
            SELECT 
                b.id, 
                b.alumno_id, 
                b.grupo_id, 
                b.fecha, 
                b.semestre, 
                b.gestion,
                g.grupo,
                g.materia_nombre,
                h.dia,
                h.hora_inicio,
                h.hora_fin
            FROM boletas b JOIN grupos g ON b.grupo_id = g.id
            JOIN horarios h ON h.grupo_id = g.id
            WHERE b.alumno_id=?
            """.trimIndent()
        )
        stmt.setInt(1, alumnoId)
        
        stmt.executeQuery().use { c ->
            while (c.next()) {
                lista.add(
                    Boleta(
                        id = c.getInt("id"),
                        alumnoId = c.getInt("alumno_id"),
                        grupoId = c.getInt("grupo_id"),
                        fecha = c.getString("fecha"),
                        semestre = c.getInt("semestre"),
                        gestion = c.getInt("gestion"),
                        grupo = c.getString("grupo"),
                        materiaNombre = c.getString("materia_nombre"),
                        dia = c.getString("dia"),
                        horario = "${c.getString("dia")} - ${c.getString("hora_inicio")}"
                    )
                )
            }
        }
        return lista
    }

    fun registrarBoleta(alumnoId: Int, grupoId: Int, fecha: String, semestre: Int, gestion: Int) {
        val stmt = getConnection().prepareStatement(
            "INSERT INTO boletas(alumno_id, grupo_id, fecha, semestre, gestion) VALUES (?,?,?,?,?)"
        )
        stmt.setInt(1, alumnoId)
        stmt.setInt(2, grupoId)
        stmt.setString(3, fecha)
        stmt.setInt(4, semestre)
        stmt.setInt(5, gestion)
        stmt.executeUpdate()
    }

    fun tieneCruceDeHorario(alumnoId: Int, grupoId: Int): Boolean {
        // Horarios del grupo a inscribir
        val horariosNuevo = mutableListOf<Pair<String, StringRange>>()
        val stmt1 = getConnection().prepareStatement(
            "SELECT dia, hora_inicio, hora_fin FROM horarios WHERE grupo_id=?"
        )
        stmt1.setInt(1, grupoId)
        
        stmt1.executeQuery().use { c ->
            while (c.next()) {
                horariosNuevo.add(
                    Pair(
                        c.getString("dia"),
                        StringRange(c.getString("hora_inicio"), c.getString("hora_fin"))
                    )
                )
            }
        }

        // Horarios de grupos ya inscritos
        val horariosPrevios = mutableListOf<Pair<String, StringRange>>()
        val stmt2 = getConnection().prepareStatement(
            """
            SELECT h.dia, h.hora_inicio, h.hora_fin
            FROM horarios h
            INNER JOIN boletas b ON b.grupo_id = h.grupo_id
            WHERE b.alumno_id = ?
            """
        )
        stmt2.setInt(1, alumnoId)
        
        stmt2.executeQuery().use { c ->
            while (c.next()) {
                horariosPrevios.add(
                    Pair(
                        c.getString("dia"),
                        StringRange(c.getString("hora_inicio"), c.getString("hora_fin"))
                    )
                )
            }
        }

        // Comparar
        for (nuevo in horariosNuevo) {
            for (prev in horariosPrevios) {
                if (nuevo.first == prev.first && nuevo.second.overlaps(prev.second)) {
                    return true
                }
            }
        }
        return false
    }

    // ===== ASISTENCIAS =====

    fun registrarAsistencia(alumnoId: Int, grupoId: Int, fecha: String) {
        val stmt = getConnection().prepareStatement(
            "INSERT INTO asistencias(alumno_id, grupo_id, fecha) VALUES (?,?,?)"
        )
        stmt.setInt(1, alumnoId)
        stmt.setInt(2, grupoId)
        stmt.setString(3, fecha)
        stmt.executeUpdate()
    }

    fun obtenerAsistenciasPorAlumno(alumnoId: Int): List<Asistencia> {
        val lista = mutableListOf<Asistencia>()
        val stmt = getConnection().prepareStatement(
            """
            SELECT 
                a.id, 
                a.alumno_id, 
                a.grupo_id, 
                a.fecha,
                g.grupo,
                g.materia_nombre
            FROM 
                asistencias a JOIN grupos g ON a.grupo_id = g.id
            WHERE a.alumno_id=?
            """.trimIndent()
        )
        stmt.setInt(1, alumnoId)
        
        stmt.executeQuery().use { c ->
            while (c.next()) {
                lista.add(
                    Asistencia(
                        id = c.getInt("id"),
                        alumnoId = c.getInt("alumno_id"),
                        grupoId = c.getInt("grupo_id"),
                        fecha = c.getString("fecha"),
                        grupo = c.getString("grupo"),
                        materiaNombre = c.getString("materia_nombre")
                    )
                )
            }
        }
        return lista
    }

    fun puedeMarcarAsistencia(alumnoId: Int, grupoId: Int): Boolean {
        val cal = Calendar.getInstance()
        val diaSemana = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale("es", "ES"))
        val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(cal.time)

        val stmt = getConnection().prepareStatement(
            """
            SELECT 1
            FROM horarios h
            INNER JOIN boletas b ON b.grupo_id = h.grupo_id
            WHERE b.alumno_id = ? AND h.grupo_id = ?
              AND lower(h.dia) = lower(?)
              AND h.hora_inicio <= ? AND h.hora_fin >= ?
            """.trimIndent()
        )
        stmt.setInt(1, alumnoId)
        stmt.setInt(2, grupoId)
        stmt.setString(3, diaSemana ?: "")
        stmt.setString(4, horaActual)
        stmt.setString(5, horaActual)
        
        stmt.executeQuery().use { c ->
            return c.next()
        }
    }

    fun close() {
        connection?.close()
    }
}

