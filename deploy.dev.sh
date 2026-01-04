#!/bin/bash
set -e

cd /home/ubuntu/snapping-be-app

mkdir -p nginx/conf.d

set -a
source .env
source image-tag
set +a

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

docker pull ${DOCKER_HUB_USERNAME}/snapping-be-app:${IMAGE_TAG}
docker-compose -f $COMPOSE_FILE up -d $NEW_SERVICE

echo "Health check..."
for i in {1..120}; do
  STATUS=$(docker inspect -f '{{.State.Health.Status}}' "$NEW_CONTAINER" || true)
  [ "$STATUS" = "healthy" ] && break
  sleep 1
done

if [ "$STATUS" != "healthy" ]; then
  docker-compose logs $NEW_SERVICE
  exit 1
fi

sed "s|__UPSTREAM__|${NEW_CONTAINER}:8080|" nginx/app.conf.template \
  > nginx/conf.d/app.conf

docker-compose exec -T nginx nginx -t
docker-compose exec -T nginx nginx -s reload

echo "$NEW_COLOR" > "$ACTIVE_COLOR_FILE"

docker-compose stop $OLD_SERVICE || true
docker-compose rm -f $OLD_SERVICE || true

docker image prune -f
