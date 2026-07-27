# 🚛 Logistics Fleet

Sistema de gestión de flotas de transporte desarrollado en **Java 25** con **MySQL 8.0** para el curso de **Matemáticas Discretas I** de la Universidad Nacional de Colombia.

El sistema modela la red vial colombiana como un **grafo ponderado**, implementa el **algoritmo de Dijkstra** para encontrar la ruta más corta entre ciudades, y ofrece visualización interactiva del grafo con **Java Swing**.

---

## 📸 Características

- 🗺️ **Grafo interactivo** — Visualización de la red de ciudades con algoritmo de fuerzas (Java Swing)
- 🛣️ **Ruta más corta (Dijkstra)** — Calcula el camino óptimo entre cualquier par de ciudades
- 🚚 **Gestión de vehículos** — CRUD, reportes de riesgo, costos operativos, ranking por consumo
- 👤 **Gestión de conductores** — Control de fatiga, licencias, conductores inactivos
- 📊 **Reportes analíticos** — Eficiencia, versatilidad, marcas más derrochadoras
- 🗄️ **Base de datos MySQL** — 6 tablas con datos reales de ciudades colombianas

---

## ✅ Requisitos previos

Necesitas instalar estas **2 herramientas** (todo lo demás viene incluido):

| Herramienta | Para qué | Descargar |
|---|---|---|
| **Docker Desktop** | Corre la base de datos MySQL sin que instales MySQL | [docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop) |
| **IntelliJ IDEA Community** | Ejecuta el programa Java con la interfaz gráfica del grafo | [jetbrains.com/idea/download](https://www.jetbrains.com/idea/download/) (elige **Community**, es gratis) |

> **Nota:** IntelliJ IDEA descargará automáticamente Java 25 (JDK) la primera vez que abras el proyecto. No necesitas instalar Java por separado.

---

## 🚀 Instalación paso a paso

### 1. Clonar el repositorio

```bash
git clone https://github.com/Alejandrox27/logistic_fleet.git
cd logistic_fleet
```

### 2. Crear el archivo de credenciales

```bash
# Windows (PowerShell)
Copy-Item config.properties.example config.properties

# Linux / Mac
cp config.properties.example config.properties
```

> El archivo `config.properties` ya viene con los valores correctos para Docker. No necesitas cambiar nada.

### 3. Levantar la base de datos con Docker

Abre una terminal (PowerShell, CMD o Git Bash) en la carpeta del proyecto y ejecuta:

```bash
docker compose up db -d
```

Esto descarga MySQL 8.0, crea la base de datos `logistics_fleet_db`, carga las 6 tablas y los datos de ejemplo. Solo tarda la primera vez (~1 minuto).

> **`-d`** significa "en segundo plano" (detached). Docker seguirá corriendo MySQL silenciosamente.

Para verificar que MySQL está corriendo:

```bash
docker compose ps
```

Deberías ver `logistics_mysql` con estado **healthy**.

### 4. Abrir el proyecto en IntelliJ IDEA

1. Abre **IntelliJ IDEA**
2. Clic en **Open** → navega a la carpeta `logistic_fleet` → **OK**
3. IntelliJ detectará que es un proyecto Maven y descargará las dependencias automáticamente
4. Si IntelliJ te pide configurar un **SDK/JDK**:
   - Ve a **File → Project Structure → Project → SDK**
   - Clic en **Add SDK → Download JDK**
   - Selecciona versión **25** (Oracle OpenJDK o Eclipse Temurin)
   - Clic en **Download** → **OK**

### 5. Ejecutar el programa

1. En el panel izquierdo de IntelliJ, navega a:
   ```
   src → main → java → org → example → Main.java
   ```
2. Clic derecho en `Main.java` → **Run 'Main.main()'**
3. En la consola inferior de IntelliJ aparecerá el menú:
   ```
   ==================================================
     LOGISTICS SYSTEM - BASE MODULE TESTING
   ==================================================

   --- MAIN MENU ---
   1. Vehicle Operations (Reports & Queries)
   2. Driver Operations (Reports & Queries)
   3. Route Operations (Reports & Queries)
   0. Exit Application
   Select an option:
   ```
4. Escribe un número y presiona **Enter** para navegar.

---

## 🎮 Qué puedes hacer

### Menú de Vehículos (opción 1)
- Ver todos los vehículos
- Reporte de riesgo de mantenimiento (bloquea vehículos automáticamente)
- Ranking de marcas más derrochadoras de combustible
- Vehículo más versátil (más destinos visitados)
- Costos operativos
- Crear vehículo, registrar mantenimiento, cambiar estado

### Menú de Conductores (opción 2)
- Reporte de fatiga (bloquea conductores con >2000 km)
- Conductores inactivos del mes actual

### Menú de Rutas (opción 3)
- Ver todas las rutas
- Reporte de eficiencia
- **🌐 Ver grafo visual de ciudades** — Abre una ventana interactiva con:
  - Zoom con rueda del ratón
  - Arrastrar nodos individuales
  - Paneo con clic y arrastre del fondo
- **🛣️ Ruta más corta (Dijkstra)** — Muestra todas las ciudades y calcula el camino óptimo

---

## 📋 Comandos útiles de Docker

| Acción | Comando |
|---|---|
| Iniciar la base de datos | `docker compose up db -d` |
| Ver estado de MySQL | `docker compose ps` |
| Detener la base de datos | `docker compose down` |
| Borrar todo y empezar de cero | `docker compose down -v` |
| Conectar MySQL Workbench | Host: `127.0.0.1`, Puerto: `3307`, User: `logistics_user`, Pass: `logistics123` |

---

## 📁 Estructura del proyecto

```
logistic_fleet/
├── src/main/java/org/example/
│   ├── Main.java                      ← Punto de entrada (menús)
│   ├── models/
│   │   ├── Vehicle.java, Driver.java, Route.java ...
│   │   ├── graphs/
│   │   │   ├── City.java              ← Vértice del grafo
│   │   │   ├── Road.java             ← Arista del grafo
│   │   │   └── CityGraph.java        ← Lista de adyacencia
│   │   ├── algorithms/
│   │   │   └── DijkstraAlgorithm.java ← Ruta más corta
│   │   └── views/
│   │       └── RouteMapVisualizer.java ← Grafo interactivo (Swing)
│   ├── services/                      ← Lógica de negocio
│   └── db/
│       ├── Connection_db.java         ← Conexión JDBC
│       ├── logistics_fleet_db.sql     ← Schema + datos
│       └── *DAO.java                  ← Acceso a datos
├── config.properties.example          ← Plantilla de credenciales
├── docker-compose.yml                 ← MySQL en Docker
├── Dockerfile                         ← Para correr todo en Docker
└── pom.xml                            ← Dependencias Maven
```

---

## 🧮 Matemáticas Discretas aplicadas

| Concepto | Dónde se aplica |
|---|---|
| **Grafos** | Red de ciudades como grafo ponderado `G=(V,E,w)` |
| **Dijkstra** | Cálculo de ruta más corta con cola de prioridad |
| **Relaciones de equivalencia** | `City.equals()` → reflexiva, simétrica, transitiva |
| **Lógica proposicional** | Validaciones de rutas como `p ∧ q ∧ r ∧ s ∧ t` |
| **Conjuntos** | `HashSet` para unicidad de categorías de licencia |
| **Árboles** | Herencia `Vehicle → HeavyTruck / DeliveryVan` |
| **Combinatoria** | `COUNT(DISTINCT destination)` en reportes |

---

## 🤖 Declaración sobre el uso de Inteligencia Artificial

### ¿Cómo se utilizó?

Para el desarrollo de este proyecto y la preparación del documento se utilizó la herramienta de inteligencia artificial Claude (Anthropic) como apoyo en la redacción, estructuración formal del texto, revisión de reglas y depuración técnica del código. El contenido conceptual, el análisis del dominio, la problemática identificada y el diseño del modelo de base de datos fueron desarrollados en su totalidad por los integrantes del grupo con base en su conocimiento directo de la ORI-UNAL y el curso de bases de datos.

A continuación, se detalla el uso específico de la herramienta en cada etapa del trabajo:

#### 1. Redacción y estructuración formal del texto
Se utilizó la IA exclusivamente como apoyo para mejorar la fluidez, la coherencia y la formalidad sintáctica de las ideas planteadas por el autor. El prompt utilizado fue el siguiente:

> *"Voy a explicar con mis propias palabras y me me ayudas a redactar: La organización de relaciones exteriores ORI de la UNAL es la entidad encargada de la relación profesional con instituciones fuera. Se incluyen convocatorias para pasantías, intercambios, etc. Es una parte importante porque le da visibilidad a la universidad y oportunidades a los estudiantes. El problema es que esta información no se puede ver por problemas que las páginas están caídas y que toda esta información se comparte particionada en correos individuales o en un Excel que ni siquiera abre. La idea es crear una base de datos con toda esta información unificada y de fácil acceso para que los estudiantes puedan ver a qué pueden aplicar sin complicarse."*

A partir de esta explicación en palabras propias, la IA ayudó únicamente a mejorar la presentación del texto, conservando en su totalidad las ideas, el diagnóstico y la solución propuesta.

#### 2. Formulación y validación de reglas de negocio
Por otro lado, para lograr mayor precisión en la definición de restricciones del sistema, se consultaron mejoras y correcciones conceptuales utilizando la siguiente estructura de prompt:

- `*Contexto del proyecto* "De acuerdo a este contexto, las siguientes reglas se justifican como restricciones para el buen funcionamiento del sistema" *Reglas*`

#### 3. Depuración de código SQL y apoyo en la traducción a MySQL
En la fase de implementación técnica y construcción del script de la base de datos, la herramienta sirvió como soporte para las siguientes tareas:

- **Depuración de código DDL:** Asistencia en la identificación y corrección de errores sintácticos y lógicos en el script SQL, garantizando el correcto funcionamiento de las restricciones de integridad (`CHECK`, `FOREIGN KEY`) y la lógica de validación previa en los disparadores (*triggers*).
- **Traducción y adaptación entre motores:** Apoyo técnico en el proceso de traducción de la lógica desde PostgreSQL hacia MySQL. Esto incluyó la adaptación de los tipos de datos (como el ajuste de los enumerados `ENUM`) y la reestructuración sintáctica de funciones o lógica de control hacia disparadores (*triggers*) mediante bloques `DELIMITER` en MySQL.

#### 4. Ayuda al momento de usar Docker para contenerizar el proyecto
Al final del proyecto, se optó por utilizar Docker para que la instalación y prueba del proyecto sea mucho más sencillo, sin embargo, no había trabajado antes con Docker, por esta razón se usó la IA para la ayuda de la configuración de este.

Para esto, se le pidió a la IA que modificara la carpeta del proyecto para adaptarla a Docker, de esta manera es más fácil para quien vaya a probar el proyecto.

*Cabe destacar que el uso de la IA se limitó a un rol consultivo y de soporte técnico; todo el texto redactado y el código SQL generado fue revisado, probado y validado por los integrantes del proyecto para asegurar el cumplimiento estricto de las reglas de negocio del proyecto.*

---

**Universidad Nacional de Colombia** — Matemáticas Discretas I — 2026
