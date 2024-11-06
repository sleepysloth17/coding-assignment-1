#!/bin/sh

set -e

echo "Substituting api url... "
envsubst '\$API_URL' < /etc/nginx/conf.d/default.tmpl > /etc/nginx/conf.d/default.conf

echo "Starting nginx daemon ... "

/usr/sbin/nginx -g "daemon off;"