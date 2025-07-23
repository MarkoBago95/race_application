# === STAGE 1: Build Angular app ===
FROM node:18 as build

# Postavi radni direktorij
WORKDIR /app

# Kopiraj package.json i package-lock.json
COPY package*.json ./

# Instaliraj ovisnosti
RUN npm install

# Kopiraj ostatak aplikacije
COPY . .

# Debug: Prikaži ime projekta iz angular.json ako si nesiguran
# RUN cat angular.json

# Build Angular aplikacije (prilagodi ime ako treba)
RUN npx ng build race_application_client 

# Debug: Prikaži što je build generirao
RUN ls -l /app/dist && ls -l /app/dist/race_application_client || echo "❌ dist/race_application_client not found"

# === STAGE 2: Serve with NGINX ===
FROM nginx:alpine

# Kopiraj build output iz prethodne faze
COPY --from=build /app/dist/race_application_client /usr/share/nginx/html

# Opcionalno: fallback routing (ako koristiš Angular Router)
COPY nginx.conf /etc/nginx/conf.d/default.conf
