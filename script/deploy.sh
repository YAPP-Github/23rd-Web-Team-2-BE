IS_GREEN=$(docker-compose -p baro-green -f docker-compose.yml ps | grep Up)

if [ -z "$IS_GREEN" ]; then
  echo "blue -> green"
  docker rm baro-green
  docker-compose --profile green -p baro-green -f docker-compose.yml up -d

  BEFORE_COMPOSE_COLOR="blue"
  AFTER_COMPOSE_COLOR="green"

  sudo sed -i 's/$backend_port 8081/$backend_port 8080/' /etc/nginx/sites-available/default
else
  echo "green -> blue"
  docker rm baro-blue
  docker-compose --profile blue -p baro-blue -f docker-compose.yml up -d

  BEFORE_COMPOSE_COLOR="green"
  AFTER_COMPOSE_COLOR="blue"

  sudo sed -i 's/$backend_port 8080/$backend_port 8081/' /etc/nginx/sites-available/default
fi

while true
do
  sleep 3

  EXIST_AFTER=$(docker-compose -p baro-${AFTER_COMPOSE_COLOR} -f docker-compose.yml ps | grep Up)
  if [ -n "$EXIST_AFTER" ]; then
    nginx -s reload

    docker-compose -p baro-${BEFORE_COMPOSE_COLOR} stop
    echo "$BEFORE_COMPOSE_COLOR down"
    break
  fi
done;
