#!/bin/bash
set -e

cd /home/ubuntu/snapping-be-app
mkdir -p nginx/conf.d

set -a
source .env
set +a

echo "Docker login..."
echo "$DOCKER_HUB_PASSWORD" | docker login \
  -u "$DOCKER_HUB_USERNAME" \
  --password-stdin

if [ ! -f image-tag ]; then
  echo "image-tag file not found"
  exit 1
fi

IMAGE_TAG=$(grep IMAGE_TAG image-tag | cut -d= -f2 | tr -d '\r\n')
export IMAGE_TAG

echo "IMAGE_TAG=$IMAGE_TAG"
echo "DOCKER_HUB_USERNAME=$DOCKER_HUB_USERNAME"

COMPOSE_FILE="docker-compose.dev.yml"
ACTIVE_COLOR_FILE=".active_color"
APP_NAME="snapping-be-app"

if [ ! -f "$ACTIVE_COLOR_FILE" ]; then
  echo "blue" > "$ACTIVE_COLOR_FILE"
fi

ACTIVE_COLOR=$(cat "$ACTIVE_COLOR_FILE")
NEW_COLOR=$([ "$ACTIVE_COLOR" = "blue" ] && echo "green" || echo "blue")

NEW_SERVICE="app_${NEW_COLOR}"
OLD_SERVICE="app_${ACTIVE_COLOR}"
NEW_CONTAINER="${APP_NAME}-${NEW_COLOR}"

echo "Pull image..."
docker pull ${DOCKER_HUB_USERNAME}/snapping-be-app:${IMAGE_TAG}

if ! docker ps --format '{{.Names}}' | grep -q snapping-be-app-nginx; then
  echo "Start nginx (first deploy)"
  docker-compose -f $COMPOSE_FILE up -d nginx
fi

echo "Start new service: $NEW_SERVICE"
docker-compose -f $COMPOSE_FILE up -d $NEW_SERVICE

STATUS="starting"
for i in {1..120}; do
  STATUS=$(docker inspect -f '{{.State.Health.Status}}' "$NEW_CONTAINER" 2>/dev/null || echo "not-found")
  echo "[$i] status=$STATUS"
  [ "$STATUS" = "healthy" ] && break
  [ "$STATUS" = "unhealthy" ] && exit 1
  sleep 1
done

[ "$STATUS" != "healthy" ] && exit 1

sed "s|__UPSTREAM__|${NEW_CONTAINER}:8080|" nginx/app.conf.template \
  > nginx/conf.d/app.conf

docker-compose exec -T nginx nginx -t
docker-compose exec -T nginx nginx -s reload

echo "$NEW_COLOR" > "$ACTIVE_COLOR_FILE"

docker-compose stop $OLD_SERVICE || true
docker-compose rm -f $OLD_SERVICE || true

docker image prune -f
