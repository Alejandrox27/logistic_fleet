# ══════════════════════════════════════════════════════════════
#  Dockerfile — Logistics Fleet
#  Build multietapa: compila con Maven + corre con JRE mínimo
# ══════════════════════════════════════════════════════════════

# ── ETAPA 1: Compilación ───────────────────────────────────────
# Usamos la imagen oficial de Eclipse Temurin con JDK 25 y Maven
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /build

# Instalar Maven (la imagen de temurin no lo incluye)
RUN apt-get update && \
    apt-get install -y --no-install-recommends maven && \
    rm -rf /var/lib/apt/lists/*

# Copiar primero solo el pom.xml para aprovechar el cache de Docker:
# si el código cambia pero pom.xml no, Maven no re-descarga dependencias.
COPY pom.xml .
RUN mvn dependency:go-offline --no-transfer-progress -q

# Copiar el resto del código fuente
COPY src ./src

# Compilar y empaquetar como fat JAR (incluye mysql-connector-java)
RUN mvn package --no-transfer-progress -q -DskipTests

# ── ETAPA 2: Imagen de ejecución ──────────────────────────────
# Solo JRE (más liviano que JDK), sin Maven ni código fuente
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copiar el fat JAR desde la etapa de build
COPY --from=builder /build/target/*-jar-with-dependencies.jar app.jar

# Copiar el script de arranque
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

# El contenedor es interactivo (la app es de consola con Scanner)
# Se activa con: docker run -it  (o stdin_open/tty en compose)

ENTRYPOINT ["/app/docker-entrypoint.sh"]
