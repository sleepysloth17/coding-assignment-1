# Install dependancies and build
FROM node:20-alpine3.19 AS builder

ARG API_URL

WORKDIR /app
COPY package.json package-lock.json /
RUN npm ci

COPY . .
# Hack to allow defining API_URL on build, quick and dirty, but I should really use production/development builds
RUN echo "export const environment = { apiUrl: '/api' };" > ./src/environments/environment.ts
# build
RUN npm run build


# Use the compiled code
FROM nginx:alpine

ARG API_URL
ENV API_URL=$API_URL

COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.tmpl
COPY ./init_container.sh /usr/local/bin/
RUN chmod +x /usr/local/bin/init_container.sh

EXPOSE 80

ENTRYPOINT ["init_container.sh"]