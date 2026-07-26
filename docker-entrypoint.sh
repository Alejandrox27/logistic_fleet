#!/bin/bash
# ══════════════════════════════════════════════════════════════
#  docker-entrypoint.sh — Logistics Fleet
#
#  Este script se ejecuta ANTES de arrancar la aplicación Java.
#  Su trabajo es:
#    1. Esperar a que MySQL esté completamente listo
#    2. Generar config.properties con las credenciales del entorno
#    3. Lanzar la aplicación
# ══════════════════════════════════════════════════════════════
set -e

# ── Valores con defaults si no se pasan variables de entorno ──
DB_HOST="${DB_HOST:-db}"
DB_PORT="${DB_PORT:-3306}"
DB_NAME="${DB_NAME:-logistics_fleet_db}"
DB_USER="${DB_USER:-logistics_user}"
DB_PASS="${DB_PASS:-logistics123}"

echo ""
echo "╔══════════════════════════════════════════════╗"
echo "║          LOGISTICS FLEET — Docker            ║"
echo "╚══════════════════════════════════════════════╝"
echo ""
echo "  Base de datos : ${DB_HOST}:${DB_PORT}/${DB_NAME}"
echo "  Usuario DB    : ${DB_USER}"
echo ""

# ── Esperar a que MySQL acepte conexiones ─────────────────────
# Aunque docker-compose tiene healthcheck, damos un margen extra.
echo "⏳ Esperando que MySQL esté listo..."
MAX_TRIES=30
COUNT=0

until java -cp /app/app.jar \
      com.mysql.cj.jdbc.Driver 2>/dev/null || \
      nc -z "${DB_HOST}" "${DB_PORT}" 2>/dev/null; do
  COUNT=$((COUNT + 1))
  if [ "$COUNT" -ge "$MAX_TRIES" ]; then
    echo "❌ MySQL no respondió después de ${MAX_TRIES} intentos. Abortando."
    exit 1
  fi
  echo "   ... intento ${COUNT}/${MAX_TRIES} (esperando 3s)"
  sleep 3
done

echo "✅ MySQL disponible. Iniciando aplicación..."
echo ""

# ── Generar config.properties desde variables de entorno ──────
# Esto permite que Connection_db.java también funcione por la ruta
# de fallback de config.properties si fuera necesario.
cat > /app/config.properties <<EOF
db.HOST=${DB_HOST}
db.PORT=${DB_PORT}
db.NAME=${DB_NAME}
db.USER=${DB_USER}
db.PASS=${DB_PASS}
EOF

# ── Lanzar la aplicación Java ─────────────────────────────────
exec java -jar /app/app.jar
