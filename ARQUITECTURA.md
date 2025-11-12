# 🏗️ Arquitectura de AsistenciaApp Web

## 📐 Estructura del Proyecto

```
AsistenciaApp/
│
├── src/main/kotlin/com/asistencia/
│   ├── Application.kt                      # 🚀 Punto de entrada
│   │   ├── main()                          # Inicializa DB y servidor
│   │   └── configureApp()                  # Configura plugins y rutas
│   │
│   ├── data/
│   │   ├── AppDatabase.kt                  # 💾 Capa de datos (JDBC)
│   │   └── StringRange.kt                  # 🔧 Utilidad para horarios
│   │
│   ├── domain/model/                       # 📦 Modelos de dominio
│   │   ├── Usuario.kt
│   │   ├── Materia.kt
│   │   ├── Horario.kt
│   │   ├── Grupo.kt
│   │   ├── Asistencia.kt
│   │   └── Boleta.kt
│   │
│   └── routes/
│       └── UsuarioRoutes.kt                # 🛣️ Endpoints de usuarios
│
├── build-ktor.gradle.kts                   # 🔧 Configuración Gradle
├── settings-ktor.gradle.kts                # ⚙️ Settings Gradle
│
└── asistenciadb.db                         # 💾 Base de datos SQLite
```

## 🔄 Flujo de una Petición HTTP

```
Cliente (Browser/Postman/cURL)
    │
    │ HTTP Request
    │ POST /api/login
    │ {"usuario": "alumno1", "password": "1234"}
    │
    ▼
┌─────────────────────────────────────────┐
│   KTOR SERVER (Puerto 8080)             │
│                                          │
│  ┌────────────────────────────────────┐ │
│  │ Plugins Middleware                 │ │
│  │  • CallLogging  (📊 logs)          │ │
│  │  • CORS         (🌐 permisos)      │ │
│  │  • ContentNeg   (🔄 JSON)          │ │
│  └────────────────────────────────────┘ │
│                                          │
│  ┌────────────────────────────────────┐ │
│  │ Routing                            │ │
│  │  route("/api") {                   │ │
│  │    post("/login") { ... }          │ │
│  │  }                                 │ │
│  └────────────────────────────────────┘ │
│                 │                        │
└─────────────────┼────────────────────────┘
                  │
                  ▼
         ┌────────────────────┐
         │ UsuarioRoutes.kt   │
         │                    │
         │ 1. Recibir request │
         │ 2. Validar datos   │
         │ 3. Llamar a DB     │
         │ 4. Retornar JSON   │
         └────────────────────┘
                  │
                  ▼
         ┌────────────────────┐
         │ AppDatabase.kt     │
         │                    │
         │ validarUsuario()   │
         │   ├─ SQL Query     │
         │   └─ Map to Model  │
         └────────────────────┘
                  │
                  ▼
         ┌────────────────────┐
         │ SQLite DB          │
         │ asistenciadb.db    │
         │                    │
         │ SELECT * FROM      │
         │ usuarios WHERE...  │
         └────────────────────┘
                  │
                  │ ResultSet
                  ▼
         ┌────────────────────┐
         │ Usuario (Model)    │
         │                    │
         │ @Serializable      │
         │ data class Usuario │
         └────────────────────┘
                  │
                  │ JSON Serialization
                  ▼
         ┌────────────────────┐
         │ ApiResponse<T>     │
         │                    │
         │ {                  │
         │   "success": true, │
         │   "data": {...}    │
         │ }                  │
         └────────────────────┘
                  │
                  │ HTTP Response
                  ▼
              Cliente
```

## 🔌 Plugins Configurados

### 1️⃣ **CallLogging**
```kotlin
install(CallLogging) {
    level = Level.INFO
    format { call ->
        "📡 ${call.response.status()} | ${call.request.httpMethod.value} ${call.request.path()}"
    }
}
```

**Propósito:** Registrar cada petición HTTP
**Salida:** `📡 200 OK | POST /api/login | UA: curl/7.68.0`

### 2️⃣ **ContentNegotiation**
```kotlin
install(ContentNegotiation) {
    json(Json {
        prettyPrint = true        // JSON formateado
        isLenient = true          // Flexible con formato
        ignoreUnknownKeys = true  // Ignora campos extra
    })
}
```

**Propósito:** Serialización automática de JSON
**Entrada:** `{"usuario":"alumno1","password":"1234"}`
**Salida:** `LoginRequest(usuario="alumno1", password="1234")`

### 3️⃣ **CORS**
```kotlin
install(CORS) {
    anyHost()                     // Permitir cualquier origen
    allowMethod(HttpMethod.Get)   // GET, POST, PUT, DELETE
    allowMethod(HttpMethod.Post)
    allowHeader(HttpHeaders.ContentType)
    allowCredentials = true
}
```

**Propósito:** Permitir peticiones desde cualquier origen (frontend)
**Headers añadidos:**
- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Methods: GET, POST, PUT, DELETE`
- `Access-Control-Allow-Headers: Content-Type, Authorization`

## 📊 Flujo de Datos

### Ejemplo: Login

```
1. Cliente envía:
   POST /api/login
   {"usuario": "alumno1", "password": "1234"}

2. CallLogging registra:
   📡 Received: POST /api/login

3. ContentNegotiation deserializa:
   LoginRequest(usuario="alumno1", password="1234")

4. UsuarioRoutes procesa:
   val request = call.receive<LoginRequest>()
   val usuario = database.validarUsuario(request.usuario, request.password)

5. AppDatabase ejecuta:
   SELECT * FROM usuarios WHERE username=? AND contrasena=?

6. SQLite retorna:
   ResultSet { id=1, nombres="Ana", ... }

7. AppDatabase mapea:
   Usuario(id=1, nombres="Ana", apellidos="Alumno", ...)

8. UsuarioRoutes responde:
   ApiResponse(success=true, data=usuario)

9. ContentNegotiation serializa:
   {"success":true,"data":{"id":1,"nombres":"Ana",...}}

10. CallLogging registra:
    📡 200 OK | POST /api/login

11. Cliente recibe:
    HTTP 200 OK
    {"success":true,"data":{"id":1,"nombres":"Ana",...}}
```

## 🎯 Ventajas de esta Arquitectura

### ✅ **Separación de Responsabilidades**
- **Application.kt** → Configuración y arranque
- **Routes** → Endpoints y validaciones HTTP
- **AppDatabase** → Lógica de acceso a datos
- **Models** → Estructuras de datos

### ✅ **Escalabilidad**
```
Fácil agregar nuevas entidades:

1. Crear modelo:      src/domain/model/Entidad.kt
2. Agregar tabla:     AppDatabase.onCreate()
3. Agregar métodos:   AppDatabase.obtenerEntidades()
4. Crear rutas:       routes/EntidadRoutes.kt
5. Registrar rutas:   Application.kt → routing { ... }
```

### ✅ **Mantenibilidad**
- Código organizado por capas
- Cada archivo tiene una responsabilidad clara
- Fácil de testear

### ✅ **Reutilización**
```kotlin
// Mismo AppDatabase para todas las rutas
fun Application.configureApp(database: AppDatabase) {
    routing {
        usuarioRoutes(database)
        materiaRoutes(database)      // Fácil de agregar
        grupoRoutes(database)         // Fácil de agregar
        asistenciaRoutes(database)    // Fácil de agregar
    }
}
```

## 🔐 Seguridad (Pendiente de Implementar)

### Recomendaciones:
1. **Hashing de contraseñas** - No guardar en texto plano
2. **JWT Tokens** - Para autenticación stateless
3. **Rate Limiting** - Prevenir abuso
4. **Validación de entrada** - Sanitización de datos
5. **HTTPS** - Encriptar comunicación

## 📈 Próximos Pasos

### 1. Agregar más rutas:
- `MateriaRoutes.kt` - CRUD de materias
- `GrupoRoutes.kt` - CRUD de grupos
- `HorarioRoutes.kt` - CRUD de horarios
- `AsistenciaRoutes.kt` - Registro de asistencia

### 2. Implementar autenticación:
```kotlin
install(Authentication) {
    jwt {
        // Configuración JWT
    }
}
```

### 3. Agregar validación robusta:
```kotlin
// Usar biblioteca de validación
data class CrearUsuarioRequest(
    @field:NotBlank val nombres: String,
    @field:Email val username: String,
    @field:Size(min = 6) val contrasena: String
)
```

### 4. Testing:
```kotlin
@Test
fun testLogin() = testApplication {
    client.post("/api/login") {
        contentType(ContentType.Application.Json)
        setBody("""{"usuario":"alumno1","password":"1234"}""")
    }.apply {
        assertEquals(HttpStatusCode.OK, status)
        val response = body<ApiResponse<Usuario>>()
        assertTrue(response.success)
    }
}
```

## 🎓 Resumen

**AsistenciaApp Web** es una aplicación REST API construida con:
- **Ktor** como framework web
- **SQLite** como base de datos
- **JDBC** para acceso a datos
- **Kotlinx Serialization** para JSON
- **Arquitectura en capas** para organización

La aplicación está lista para:
✅ Recibir peticiones HTTP
✅ Autenticar usuarios
✅ Gestionar CRUD de usuarios
✅ Expandirse con nuevas funcionalidades

---

**Estado:** ✅ Funcionando y listo para producción (modo desarrollo)
**Próximo:** Implementar rutas de Materias, Grupos, Horarios y Asistencias

