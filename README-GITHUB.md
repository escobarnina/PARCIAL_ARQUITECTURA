# 🎓 AsistenciaApp Web - API REST con Ktor

Sistema de gestión de asistencia universitaria construido con **Ktor**, **Kotlin** y **SQLite**.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org/)
[![Ktor](https://img.shields.io/badge/Ktor-3.0.1-orange.svg)](https://ktor.io/)
[![License](https://img.shields.io/badge/License-Academic-green.svg)]()

## 📋 Descripción

AsistenciaApp Web es un servidor REST API desarrollado como proyecto académico de Arquitectura de Software. Permite gestionar:

- 👥 **Usuarios** (Alumnos, Docentes, Administradores)
- 📚 **Materias** y niveles académicos
- 👨‍🏫 **Grupos** y asignaciones de docentes
- 🕐 **Horarios** de clases
- ✅ **Asistencias** de estudiantes
- 📄 **Boletas** de inscripción

## ✨ Características

- ✅ API REST completa con Ktor 3.0.1
- ✅ Base de datos SQLite embebida
- ✅ Serialización JSON automática
- ✅ CORS configurado para desarrollo
- ✅ Logging detallado de requests
- ✅ Arquitectura en capas (Domain, Data, Routes)
- ✅ Validación de datos
- ✅ Gestión de errores centralizada

## 🛠️ Tecnologías

- **[Ktor 3.0.1](https://ktor.io/)** - Framework web para Kotlin
- **[Kotlin 2.0.21](https://kotlinlang.org/)** - Lenguaje de programación
- **[SQLite JDBC](https://github.com/xerial/sqlite-jdbc)** - Base de datos embebida
- **[Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)** - JSON
- **[Netty](https://netty.io/)** - Motor de servidor
- **[Logback](https://logback.qos.ch/)** - Sistema de logging

## 📁 Estructura del Proyecto

```
AsistenciaApp/
├── src/main/kotlin/com/asistencia/
│   ├── Application.kt              # Punto de entrada del servidor
│   ├── data/
│   │   ├── AppDatabase.kt          # Acceso a datos con JDBC
│   │   └── StringRange.kt          # Utilidades
│   ├── domain/model/               # Modelos de dominio
│   │   ├── Usuario.kt
│   │   ├── Materia.kt
│   │   ├── Horario.kt
│   │   ├── Grupo.kt
│   │   ├── Asistencia.kt
│   │   └── Boleta.kt
│   └── routes/
│       └── UsuarioRoutes.kt        # Endpoints REST
├── build-ktor.gradle.kts           # Configuración Gradle
├── settings-ktor.gradle.kts        # Settings Gradle
└── README.md                       # Este archivo
```

## 🚀 Inicio Rápido

### Requisitos Previos

- **Java JDK 17+** ([Descargar](https://adoptium.net/))
- **Git** (para clonar el repositorio)

### Instalación

1. **Clonar el repositorio:**
```bash
git clone https://github.com/escobarnina/PARCIAL_ARQUITECTURA.git
cd PARCIAL_ARQUITECTURA
```

2. **Ejecutar el servidor:**

**Windows:**
```powershell
.\ejecutar-servidor-web.bat
```

**Linux/Mac:**
```bash
chmod +x run-web.sh
./run-web.sh
```

3. **Verificar que funciona:**

Abre tu navegador en: **http://localhost:8080**

## 🌐 Endpoints Disponibles

### Información del Servidor

| Método | Endpoint    | Descripción              |
|--------|-------------|--------------------------|
| GET    | `/`         | Información del servidor |
| GET    | `/health`   | Health check             |

### API de Usuarios

| Método | Endpoint          | Descripción               |
|--------|-------------------|---------------------------|
| POST   | `/api/login`      | Iniciar sesión            |
| GET    | `/api/usuarios`   | Listar todos los usuarios |
| POST   | `/api/usuarios`   | Crear nuevo usuario       |

### Formato de Respuesta

```json
{
  "success": true,
  "data": { /* datos */ },
  "message": "mensaje opcional"
}
```

## 🧪 Ejemplos de Uso

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

## 👥 Usuarios de Prueba

| Username  | Password | Rol     |
|-----------|----------|---------|
| alumno1   | 1234     | Alumno  |
| alumno2   | 1234     | Alumno  |
| alumno3   | 1234     | Alumno  |
| docente1  | 1234     | Docente |
| docente2  | 1234     | Docente |
| docente3  | 1234     | Docente |
| admin1    | 1234     | Admin   |

## 📚 Documentación

- **[README.md](README.md)** - Documentación completa del proyecto
- **[RESUMEN-FINAL.md](RESUMEN-FINAL.md)** - Estado final y arquitectura
- **[AsistenciaApp-API.postman_collection.json](AsistenciaApp-API.postman_collection.json)** - Colección Postman

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas:

```
Presentación (API REST)
    ↓
Rutas (UsuarioRoutes)
    ↓
Servicios/Lógica de Negocio
    ↓
Repositorio/Datos (AppDatabase)
    ↓
Base de Datos (SQLite)
```

### Plugins Configurados

- **CallLogging** - Logs detallados de cada request
- **ContentNegotiation** - Serialización JSON automática
- **CORS** - Configurado para permitir cualquier origen (desarrollo)

## 🔧 Comandos Gradle

```bash
# Limpiar proyecto
./gradlew clean

# Compilar
./gradlew build

# Ejecutar
./gradlew run

# Ejecutar tests
./gradlew test
```

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

## 🤝 Contribución

Este es un proyecto académico. Si deseas contribuir:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Licencia

Proyecto académico - Universidad [Nombre de la Universidad]

## 👨‍💻 Autor

- **Nina Escobar** - [escobarnina](https://github.com/escobarnina)

## 🙏 Agradecimientos

- Profesor de Arquitectura de Software
- Comunidad de Ktor
- Documentación oficial de Kotlin

---

**⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub!**

**🐛 Encontraste un bug? [Reporta un issue](https://github.com/escobarnina/PARCIAL_ARQUITECTURA/issues)**

