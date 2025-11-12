# 🎓 AsistenciaApp Web - API REST con Ktor

Sistema de gestión de asistencia universitaria migrado de Android a servidor web usando Ktor, Kotlin y SQLite.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Requisitos Previos](#-requisitos-previos)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación](#-instalación)
- [Compilación](#-compilación)
- [Ejecución](#-ejecución)
- [Endpoints Disponibles](#-endpoints-disponibles)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Testing con Postman](#-testing-con-postman)
- [Testing con cURL](#-testing-con-curl)
- [Documentación Adicional](#-documentación-adicional)

---

## ✨ Características

- ✅ **API REST** con Ktor Framework
- ✅ **Base de datos SQLite** persistente
- ✅ **Serialización JSON** automática
- ✅ **CORS** configurado para frontend
- ✅ **Logging** detallado de requests
- ✅ **Autenticación** de usuarios (login)
- ✅ **CRUD completo** de usuarios
- ✅ **Arquitectura en capas** (Domain, Data, Routes)

---

## 🔧 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK 17+** ([Descargar](https://adoptium.net/))
- **Git** (opcional, para clonar el repositorio)

Verifica la instalación:

```bash
java -version
# Debe mostrar: openjdk version "17" o superior
```

---

## 📁 Estructura del Proyecto

```
AsistenciaApp/
│
├── src/main/kotlin/com/asistencia/
│   ├── Application.kt                    # 🚀 Punto de entrada del servidor
│   ├── data/
│   │   ├── AppDatabase.kt                # 💾 Acceso a datos (JDBC + SQLite)
│   │   └── StringRange.kt                # 🔧 Utilidad para horarios
│   ├── domain/model/                     # 📦 Modelos de dominio
│   │   ├── Usuario.kt
│   │   ├── Materia.kt
│   │   ├── Horario.kt
│   │   ├── Grupo.kt
│   │   ├── Asistencia.kt
│   │   └── Boleta.kt
│   └── routes/
│       └── UsuarioRoutes.kt              # 🛣️ Endpoints de usuarios
│
├── build-ktor.gradle.kts                 # 🔧 Configuración Gradle para Ktor
├── settings-ktor.gradle.kts              # ⚙️ Settings del proyecto Ktor
├── build.gradle.kts                      # (Configuración Android original)
├── settings.gradle.kts                   # (Settings Android original)
│
├── asistenciadb.db                       # 💾 Base de datos (se crea al ejecutar)
│
├── README.md                             # 📖 Este archivo
├── EJEMPLOS-API.md                       # 📚 Ejemplos detallados de la API
├── ARQUITECTURA.md                       # 🏗️ Documentación de arquitectura
├── EJECUTAR-SERVIDOR.md                  # 🚀 Guía de ejecución
└── MIGRACION-DATABASE.md                 # 📋 Detalles de migración
```

---

## 📥 Instalación

### 1. Clonar el repositorio (si aplica)

```bash
git clone <url-del-repositorio>
cd AsistenciaApp
```

### 2. El proyecto ya incluye Gradle Wrapper

No necesitas instalar Gradle. Los scripts `gradlew` y `gradlew.bat` están incluidos.

---

## 🔨 Compilación

### Opción 1: Compilar con configuración Ktor directamente

**Windows (PowerShell):**
```powershell
.\gradlew.bat -b build-ktor.gradle.kts -c settings-ktor.gradle.kts build
```

**Linux / Mac / Git Bash:**
```bash
./gradlew -b build-ktor.gradle.kts -c settings-ktor.gradle.kts build
```

### Opción 2: Renombrar archivos y compilar (Método permanente)

**Windows:**
```powershell
# Respaldar configuración Android
Rename-Item build.gradle.kts build-android.gradle.kts -ErrorAction SilentlyContinue
Rename-Item settings.gradle.kts settings-android.gradle.kts -ErrorAction SilentlyContinue

# Activar configuración Ktor
Rename-Item build-ktor.gradle.kts build.gradle.kts
Rename-Item settings-ktor.gradle.kts settings.gradle.kts

# Compilar
.\gradlew.bat build
```

**Linux / Mac:**
```bash
# Respaldar configuración Android
mv build.gradle.kts build-android.gradle.kts 2>/dev/null || true
mv settings.gradle.kts settings-android.gradle.kts 2>/dev/null || true

# Activar configuración Ktor
mv build-ktor.gradle.kts build.gradle.kts
mv settings-ktor.gradle.kts settings.gradle.kts

# Compilar
./gradlew build
```

### ✅ Salida Esperada

```
BUILD SUCCESSFUL in 15s
10 actionable tasks: 10 executed
```

---

## 🚀 Ejecución

### Opción 1: Ejecutar directamente (Recomendado)

**Windows:**
```powershell
.\gradlew.bat -b build-ktor.gradle.kts -c settings-ktor.gradle.kts run
```

**Linux / Mac:**
```bash
./gradlew -b build-ktor.gradle.kts -c settings-ktor.gradle.kts run
```

### Opción 2: Ejecutar después de renombrar

```bash
# Si ya renombraste los archivos en el paso de compilación
./gradlew run          # Linux/Mac
.\gradlew.bat run      # Windows
```

### ✅ Salida Esperada

```
============================================================
🚀 Iniciando AsistenciaApp Web Server...
============================================================
📦 Inicializando base de datos...
Datos de prueba insertados
✅ Base de datos inicializada correctamente

[main] INFO  Application - Responding at http://0.0.0.0:8080
============================================================
✅ AsistenciaApp Web Server iniciado correctamente
🌐 Servidor disponible en: http://localhost:8080
📡 API disponible en: http://localhost:8080/api
💾 Base de datos: asistenciadb.db
============================================================
```

### 🔍 Verificar que el Servidor Funciona

Abre tu navegador en: **http://localhost:8080**

Deberías ver:
```
🎓 AsistenciaApp Web API

✅ Estado: Funcionando
📦 Base de datos: SQLite
🌐 Puerto: 8080

📚 Endpoints disponibles:
  - GET  /health          - Estado del servidor
  - POST /api/login       - Iniciar sesión
  - GET  /api/usuarios    - Listar usuarios
  - POST /api/usuarios    - Crear usuario
```

---

## 🌐 Endpoints Disponibles

### Información del Servidor

| Método | Endpoint    | Descripción              |
|--------|-------------|--------------------------|
| GET    | `/`         | Información del servidor |
| GET    | `/health`   | Health check             |

### API de Usuarios

| Método | Endpoint          | Descripción               | Auth |
|--------|-------------------|---------------------------|------|
| POST   | `/api/login`      | Iniciar sesión            | No   |
| GET    | `/api/usuarios`   | Listar todos los usuarios | No   |
| POST   | `/api/usuarios`   | Crear nuevo usuario       | No   |

### Formato de Respuesta

Todas las respuestas siguen este formato:

```json
{
  "success": boolean,
  "data": T | null,
  "message": string | null
}
```

**Ejemplo de éxito:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "nombres": "Ana",
    "apellidos": "Alumno",
    "registro": "211882",
    "rol": "Alumno",
    "username": "alumno1"
  }
}
```

**Ejemplo de error:**
```json
{
  "success": false,
  "data": null,
  "message": "Usuario o contraseña incorrectos"
}
```

---

## 💡 Ejemplos de Uso

### 1️⃣ Login (Iniciar Sesión)

**Request:**
```http
POST http://localhost:8080/api/login
Content-Type: application/json

{
  "usuario": "alumno1",
  "password": "1234"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "nombres": "Ana",
    "apellidos": "Alumno",
    "registro": "211882",
    "rol": "Alumno",
    "username": "alumno1"
  }
}
```

### 2️⃣ Listar Usuarios

**Request:**
```http
GET http://localhost:8080/api/usuarios
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 7,
      "nombres": "Admin",
      "apellidos": "Admin",
      "registro": "11111",
      "rol": "Admin",
      "username": "admin1"
    },
    {
      "id": 6,
      "nombres": "Julia",
      "apellidos": "Docente",
      "registro": "56322",
      "rol": "Docente",
      "username": "docente3"
    }
    // ... más usuarios
  ]
}
```

### 3️⃣ Crear Usuario

**Request:**
```http
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombres": "Pedro",
  "apellidos": "García",
  "registro": "213456",
  "rol": "Alumno",
  "username": "pgarcia",
  "contrasena": "password123"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "id": 8,
    "nombres": "Pedro",
    "apellidos": "García",
    "registro": "213456",
    "rol": "Alumno",
    "username": "pgarcia"
  },
  "message": "Usuario creado exitosamente"
}
```

---

## 📮 Testing con Postman

### 1. Importar Colección

Crea una nueva colección en Postman llamada "AsistenciaApp API"

### 2. Configurar Requests

#### Login
```
Method: POST
URL: http://localhost:8080/api/login
Headers:
  Content-Type: application/json
Body (raw, JSON):
{
  "usuario": "alumno1",
  "password": "1234"
}
```

#### Listar Usuarios
```
Method: GET
URL: http://localhost:8080/api/usuarios
```

#### Crear Usuario
```
Method: POST
URL: http://localhost:8080/api/usuarios
Headers:
  Content-Type: application/json
Body (raw, JSON):
{
  "nombres": "Pedro",
  "apellidos": "García",
  "registro": "213456",
  "rol": "Alumno",
  "username": "pgarcia",
  "contrasena": "password123"
}
```

### 3. Variables de Entorno (Opcional)

Crea una variable `baseUrl` con valor `http://localhost:8080` y usa:
- `{{baseUrl}}/api/login`
- `{{baseUrl}}/api/usuarios`

---

## 🔧 Testing con cURL

### Login
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"usuario":"alumno1","password":"1234"}'
```

### Listar Usuarios
```bash
curl http://localhost:8080/api/usuarios
```

### Crear Usuario
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombres": "Pedro",
    "apellidos": "García",
    "registro": "213456",
    "rol": "Alumno",
    "username": "pgarcia",
    "contrasena": "password123"
  }'
```

### Con PowerShell (Windows)

```powershell
# Login
$body = @{
    usuario = "alumno1"
    password = "1234"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body

# Listar Usuarios
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios"

# Crear Usuario
$body = @{
    nombres = "Pedro"
    apellidos = "García"
    registro = "213456"
    rol = "Alumno"
    username = "pgarcia"
    contrasena = "password123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```

---

## 👥 Usuarios de Prueba

La base de datos se inicializa automáticamente con estos usuarios:

| Username  | Password | Rol     | Nombres | Apellidos | Registro |
|-----------|----------|---------|---------|-----------|----------|
| alumno1   | 1234     | Alumno  | Ana     | Alumno    | 211882   |
| alumno2   | 1234     | Alumno  | Juan    | Alumno    | 212732   |
| alumno3   | 1234     | Alumno  | Carlos  | Alumno    | 210882   |
| docente1  | 1234     | Docente | Marcos  | Docente   | 342232   |
| docente2  | 1234     | Docente | Maria   | Docente   | 45532    |
| docente3  | 1234     | Docente | Julia   | Docente   | 56322    |
| admin1    | 1234     | Admin   | Admin   | Admin     | 11111    |

---

## 🛑 Detener el Servidor

Presiona `Ctrl + C` en la terminal donde está ejecutándose el servidor.

---

## 🔄 Volver a la Configuración Android

Si renombraste los archivos y quieres volver al proyecto Android:

**Windows:**
```powershell
Rename-Item build.gradle.kts build-ktor.gradle.kts
Rename-Item settings.gradle.kts settings-ktor.gradle.kts
Rename-Item build-android.gradle.kts build.gradle.kts
Rename-Item settings-android.gradle.kts settings.gradle.kts
```

**Linux / Mac:**
```bash
mv build.gradle.kts build-ktor.gradle.kts
mv settings.gradle.kts settings-ktor.gradle.kts
mv build-android.gradle.kts build.gradle.kts
mv settings-android.gradle.kts settings.gradle.kts
```

---

## 🐛 Solución de Problemas

### Error: "Puerto 8080 ya en uso"

**Windows:**
```powershell
# Encontrar el proceso
netstat -ano | findstr :8080

# Terminar el proceso (reemplaza <PID> con el número encontrado)
taskkill /PID <PID> /F
```

**Linux / Mac:**
```bash
lsof -ti:8080 | xargs kill -9
```

### Error: "Base de datos bloqueada"

Detén el servidor y elimina el archivo de base de datos:

```bash
# Windows
Remove-Item asistenciadb.db

# Linux / Mac
rm asistenciadb.db
```

Vuelve a ejecutar el servidor.

### Error: "Cannot find JDK"

Verifica que tienes JDK 17+ instalado:

```bash
java -version
```

Si no lo tienes, descárgalo de: https://adoptium.net/

---

## 📚 Documentación Adicional

- **[EJEMPLOS-API.md](EJEMPLOS-API.md)** - Ejemplos detallados de todos los endpoints
- **[ARQUITECTURA.md](ARQUITECTURA.md)** - Documentación de la arquitectura del proyecto
- **[EJECUTAR-SERVIDOR.md](EJECUTAR-SERVIDOR.md)** - Guía detallada de ejecución
- **[MIGRACION-DATABASE.md](MIGRACION-DATABASE.md)** - Detalles de la migración de Android a Web

---

## 🛠️ Tecnologías Utilizadas

- **[Ktor 3.0.1](https://ktor.io/)** - Framework web para Kotlin
- **[Kotlin 2.0.21](https://kotlinlang.org/)** - Lenguaje de programación
- **[SQLite JDBC 3.44.1.0](https://github.com/xerial/sqlite-jdbc)** - Driver de base de datos
- **[Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)** - Serialización JSON
- **[Logback 1.4.14](https://logback.qos.ch/)** - Sistema de logging
- **[Netty](https://netty.io/)** - Motor de servidor

---

## 📊 Estado del Proyecto

| Componente              | Estado      |
|-------------------------|-------------|
| ✅ Configuración Base   | Completo    |
| ✅ Base de Datos SQLite | Completo    |
| ✅ Modelos de Dominio   | Completo    |
| ✅ API de Usuarios      | Completo    |
| ✅ Logging              | Completo    |
| ✅ CORS                 | Completo    |
| 🚧 API de Materias      | Pendiente   |
| 🚧 API de Grupos        | Pendiente   |
| 🚧 API de Horarios      | Pendiente   |
| 🚧 API de Asistencias   | Pendiente   |
| 🚧 Autenticación JWT    | Pendiente   |

---

## 🎯 Próximos Pasos

1. Implementar APIs REST para:
   - Materias
   - Grupos
   - Horarios
   - Asistencias
   - Boletas

2. Agregar autenticación con JWT

3. Implementar validaciones robustas

4. Agregar tests unitarios

5. Crear frontend (React, Vue, o Angular)

---

## 📝 Licencia

Este proyecto es parte de un ejercicio académico.

---

## 👨‍💻 Autor

Desarrollado como migración de aplicación Android a servidor web con Ktor.

---

## 🆘 Soporte

Para reportar problemas o hacer preguntas:
1. Revisa la documentación en la carpeta del proyecto
2. Verifica los ejemplos en `EJEMPLOS-API.md`
3. Consulta la arquitectura en `ARQUITECTURA.md`

---

**¡Gracias por usar AsistenciaApp Web! 🎓**

