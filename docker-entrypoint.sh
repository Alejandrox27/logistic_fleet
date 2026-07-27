#!/bin/bash
# ══════════════════════════════════════════════════════════════
#  docker-entrypoint.sh — Logistics Fleet
# ══════════════════════════════════════════════════════════════
set -e

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

echo "⏳ Esperando que MySQL esté listo en ${DB_HOST}:${DB_PORT}..."
MAX_TRIES=30
COUNT=0

# Usamos la prueba de socket nativa de Bash (/dev/tcp)
until (echo > "/dev/tcp/${DB_HOST}/${DB_PORT}") 2>/dev/null; do
  COUNT=$((COUNT + 1))
  if [ "$COUNT" -ge "$MAX_TRIES" ]; then
    echo "❌ MySQL no respondió en ${DB_HOST}:${DB_PORT} después de ${MAX_TRIES} intentos. Abortando."
    exit 1
  fi
  echo "   ... intento ${COUNT}/${MAX_TRIES} (esperando 3s)"
  sleep 3
done

echo "✅ Conexión con MySQL establecida. Iniciando aplicación..."
echo ""

# Generar config.properties como respaldo
cat > /app/config.properties <<EOF
db.HOST=${DB_HOST}
db.PORT=${DB_PORT}
db.NAME=${DB_NAME}
db.USER=${DB_USER}
db.PASS=${DB_PASS}
EOF

# Ejecutar la aplicación Java
exec java -jar /app/app.jar
