# 🚛 Logistics Fleet — Guía de inicio con Docker

Sistema de gestión de flotas de transporte desarrollado en **Java 25** con **MySQL 8.0**.  
Con Docker no necesitas instalar Java, Maven ni MySQL en tu máquina.

---

## ✅ Requisitos previos

Solo necesitas tener instalado:

| Herramienta | Versión mínima | Descargar |
|-------------|---------------|-----------|
| Docker Desktop | 4.x | https://www.docker.com/products/docker-desktop |
| Git | cualquiera | https://git-scm.com |

> **Windows**: asegúrate de que Docker Desktop esté corriendo antes de continuar.

---

## 🚀 Inicio rápido (3 pasos)

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/logistic_fleet.git
cd logistic_fleet
```

### 2. Configurar credenciales (opcional)

El proyecto trae valores por defecto que funcionan sin cambiar nada.  
Si quieres usar tus propias contraseñas:

```bash
# Windows (PowerShell)
Copy-Item .env.example .env

# Linux / Mac
cp .env.example .env
```

Luego abre `.env` con cualquier editor y cambia los valores. Si no copias el archivo, Docker usa los valores por defecto del `.env.example` automáticamente.

### 3. Levantar todo

```bash
docker compose up --build
```

Esto hará automáticamente:
1. 📦 Descargar las imágenes de MySQL 8.0 y Eclipse Temurin 25 (solo la primera vez)
2. ⚙️  Compilar el proyecto Java con Maven dentro del contenedor
3. 🗄️  Iniciar MySQL y crear la base de datos con todas las tablas y datos
4. ✅ Lanzar la aplicación cuando MySQL esté lista

---

## 🖥️ Interactuar con la aplicación

La aplicación es **interactiva por consola**. Después de que aparezca:

```
╔══════════════════════════════════════════════╗
║          LOGISTICS FLEET — Docker            ║
╚══════════════════════════════════════════════╝

✅ MySQL disponible. Iniciando aplicación...

==================================================
  LOGISTICS SYSTEM - BASE MODULE TESTING
==================================================

--- MAIN MENU ---
1. Vehicle Operations
2. Driver Operations
3. Route Operations
0. Exit Application
Select an option:
```

...ya puedes escribir números y presionar Enter para navegar.

> ⚠️ Si el terminal no responde al teclado, usa `docker attach logistics_app` en otra ventana.

---

## 📋 Comandos útiles

| Acción | Comando |
|--------|---------|
| Levantar por primera vez | `docker compose up --build` |
| Levantar (sin recompilar) | `docker compose up` |
| Detener sin borrar datos | `docker compose down` |
| **Borrar todo** (incluida la BD) | `docker compose down -v` |
| Ver logs del contenedor app | `docker logs logistics_app` |
| Ver logs de MySQL | `docker logs logistics_mysql` |
| Conectarse a MySQL directamente | `docker exec -it logistics_mysql mysql -u logistics_user -p logistics_fleet_db` |
| Reiniciar solo la app | `docker compose restart app` |

---

## 🔌 Conectar MySQL Workbench (opcional)

Si quieres inspeccionar la base de datos con MySQL Workbench:

| Campo | Valor |
|-------|-------|
| Host | `127.0.0.1` |
| Port | `3307` |
| Username | `logistics_user` (o `root`) |
| Password | `logistics123` (o el valor de `MYSQL_PASSWORD` en tu `.env`) |
| Schema | `logistics_fleet_db` |

> El puerto es **3307** (no 3306) para no chocar con una instalación local de MySQL.

---

## 📁 Estructura relevante de Docker

```
logistic_fleet/
├── Dockerfile                          ← Build multietapa Java 25
├── docker-compose.yml                  ← Orquesta MySQL + App
├── docker-entrypoint.sh                ← Espera MySQL, lanza la app
├── .env.example                        ← Plantilla de credenciales ✅ (en GitHub)
├── .env                                ← Credenciales reales ❌ (NO en GitHub)
└── src/main/java/org/example/db/
    └── logistics_fleet_db.sql          ← Schema cargado automáticamente
```

---

## 🐛 Solución de problemas comunes

**"Port 3307 is already in use"**  
→ Tienes algo corriendo en el puerto 3307. Cambia en `docker-compose.yml`:  
`"3308:3306"` (o cualquier otro puerto libre).

**La app arranca pero no puede conectar a la BD**  
→ MySQL tardó más de lo esperado. Ejecuta: `docker compose restart app`

**"Error: no main manifest attribute" al arrancar la app**  
→ Borra el target y recompila: `docker compose up --build`

**La ventana del grafo (Java Swing) no aparece**  
→ La visualización gráfica requiere un entorno de escritorio (X11/display).  
Ejecuta la opción del grafo directamente desde IntelliJ en tu máquina local,  
o usa un servidor X11 como VcXsrv (Windows) o XQuartz (Mac).
