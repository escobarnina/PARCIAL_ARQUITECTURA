# 🚀 Servidor Web en Ejecución

## ✅ ESTADO ACTUAL

El servidor **está iniciando en segundo plano** ahora mismo.

**Tiempo estimado:** 1-2 minutos (primera vez)

---

## 🔍 VERIFICAR QUE ESTÁ FUNCIONANDO

### **Opción 1: Navegador (MÁS FÁCIL)** ⭐

Abre tu navegador y ve a:
```
http://localhost:8080
```

Si ves el mensaje de bienvenida, **¡está funcionando!** ✅

---

### **Opción 2: PowerShell (NUEVA VENTANA)**

Abre una **NUEVA** ventana de PowerShell y ejecuta:

```powershell
# Health check simple
Invoke-WebRequest http://localhost:8080/health
```

**Respuesta esperada:**
```
StatusCode: 200
Content: OK
```

---

### **Opción 3: Ver si el proceso está corriendo**

```powershell
# Ver procesos de Java
Get-Process | Where-Object { $_.ProcessName -like "*java*" }

# Ver si el puerto 8080 está en uso
netstat -ano | findstr :8080
```

Si ves resultados, el servidor está corriendo.

---

## 🧪 PROBAR LOS ENDPOINTS

Una vez que el servidor esté funcionando, prueba estos comandos en una **NUEVA ventana de PowerShell**:

### 1️⃣ **Health Check**
```powershell
Invoke-RestMethod http://localhost:8080/health
```
**Resultado esperado:** `OK`

### 2️⃣ **Información del Servidor**
```powershell
Invoke-RestMethod http://localhost:8080/
```
**Resultado esperado:** Mensaje de bienvenida

### 3️⃣ **Listar Usuarios**
```powershell
Invoke-RestMethod http://localhost:8080/api/usuarios
```
**Resultado esperado:** JSON con 7 usuarios

### 4️⃣ **Login**
```powershell
$body = @{
    usuario = "alumno1"
    password = "1234"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
```
**Resultado esperado:** Datos de Ana Alumno

---

## ⏱️ ¿CUÁNTO TIEMPO FALTA?

### **Primera Ejecución:**
- ⏳ 0-30 segundos: Descargando dependencias
- ⏳ 30-60 segundos: Compilando código
- ⏳ 60-90 segundos: Iniciando servidor
- ✅ 90-120 segundos: **¡Listo para usar!**

### **Siguientes Ejecuciones:**
- ✅ 10-20 segundos: **¡Listo para usar!**

---

## 🔔 SEÑALES DE QUE ESTÁ LISTO

Si el servidor está ejecutándose en otra ventana, busca este mensaje:

```
============================================================
🚀 Iniciando AsistenciaApp Web Server...
============================================================
📦 Inicializando base de datos...
✅ Base de datos inicializada correctamente
[main] INFO  Application - Responding at http://0.0.0.0:8080
============================================================
✅ AsistenciaApp Web Server iniciado correctamente
🌐 Servidor disponible en: http://localhost:8080
📡 API disponible en: http://localhost:8080/api
============================================================
```

---

## 🐛 SI NO FUNCIONA DESPUÉS DE 2 MINUTOS

### **1. Verificar si hay errores**

Si ejecutaste `ejecutar-servidor-web.bat`, deberías ver una ventana con logs.
Busca líneas con "ERROR" o "FAILED".

### **2. Reintentar con limpieza**

Abre una **nueva** PowerShell:

```powershell
cd C:\Users\Hp\parcialarqui\AsistenciaApp
.\activar-web.bat
.\gradlew.bat clean
.\gradlew.bat run
```

### **3. Verificar Java**

```powershell
java -version
```

Debe mostrar Java 17 o superior.

### **4. Puerto ocupado**

Si dice "Puerto 8080 ya en uso":

```powershell
# Encontrar proceso
netstat -ano | findstr :8080

# Terminar proceso (reemplaza <PID>)
taskkill /PID <PID> /F
```

---

## 📊 COMANDOS RÁPIDOS DE VERIFICACIÓN

```powershell
# ¿Está el servidor corriendo?
Invoke-WebRequest http://localhost:8080/health -UseBasicParsing

# ¿Está el proceso de Java activo?
Get-Process java -ErrorAction SilentlyContinue

# ¿Está el puerto 8080 en uso?
Test-NetConnection -ComputerName localhost -Port 8080
```

---

## 🎯 RESUMEN RÁPIDO

**1.** Espera 1-2 minutos

**2.** Abre tu navegador en: **http://localhost:8080**

**3.** Si ves el mensaje de bienvenida: **¡Funciona!** ✅

**4.** Si no funciona, ejecuta:
```powershell
Invoke-WebRequest http://localhost:8080/health
```

---

## 📱 ACCESO DESDE TU RED LOCAL

Si quieres acceder desde otro dispositivo en tu red:

1. Encuentra tu IP local:
```powershell
ipconfig | findstr IPv4
```

2. Usa esa IP en otros dispositivos:
```
http://192.168.x.x:8080
```

---

## 🛑 DETENER EL SERVIDOR

Si ejecutaste `ejecutar-servidor-web.bat`:
- Presiona **Ctrl + C** en esa ventana

Si ejecutaste en segundo plano:
```powershell
# Encontrar y terminar el proceso
Get-Process java | Stop-Process
```

---

## ✨ PRÓXIMOS PASOS

Una vez que el servidor esté funcionando:

1. 📚 Lee **PRUEBAS-RAPIDAS.md** para ver todos los ejemplos
2. 📮 Importa **AsistenciaApp-API.postman_collection.json** en Postman
3. 🧪 Prueba todos los endpoints de la API

---

**El servidor está iniciando... ¡en breve estará listo!** 🚀

Mientras esperas, puedes:
- ☕ Tomar un café
- 📖 Revisar PRUEBAS-RAPIDAS.md
- 🔍 Preparar Postman para las pruebas

---

**¿Necesitas ayuda?** Revisa los archivos de documentación:
- README.md
- COMANDOS-SERVIDOR.md
- PRUEBAS-RAPIDAS.md

