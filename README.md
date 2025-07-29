

# 🏃‍ Trail Race Management App

Ova aplikacija omogućuje upravljanje trail utrkama i prijavama trkača. Građena je kao mikroservisna aplikacija s CQRS arhitekturom, koristeći:
- **Spring Boot + Gradle** za back-end (command i query servisi)
- **Angular 14** za front-end
- **PostgreSQL + RabbitMQ** u Dockeru
- **JWT autentikaciju** za aplikanta i administratora

---

## 🧱 Arhitektura

<img width="1024" height="1024" alt="image" src="https://github.com/user-attachments/assets/e487adbe-8cf4-487b-a858-c2c022a3468b" />



 
markdown


- `command-service`: Upravlja `POST`, `PATCH`, `DELETE` zahtjevima. Samo administrator može pristupiti.
- `query-service`: Exponira `GET` zahtjeve za trkače i prijave.
- `RabbitMQ`: Povezuje servise putem događaja.
- `client`: Angular aplikacija koja koristi JWT za pristup.

---

## 🚀 Pokretanje aplikacije

### 1. Kloniraj repozitorij
```bash
git clone https://github.com/MarkoBago95/race_application.git
cd trail-race-app
2. Pokreni sve servise
bash

docker-compose down -v --remove-orphans
docker-compose up --build
Nakon pokretanja dostupne komponente:

Angular frontend: http://localhost:4200

Command API: http://localhost:8080

Query API: http://localhost:8081

RabbitMQ UI: http://localhost:15672 (user/password)

👤 Uloge
Uloga	Token	Akcije
APPLICANT	koristi JWT s "role": "APPLICANT"	može pregledavati utrke, prijaviti se, brisati prijave
ADMINISTRATOR	"role": "ADMINISTRATOR"	može dodavati, uređivati i brisati utrke

Token možeš generirati preko hardkodirane forme u Angular login komponenti, ili koristiti Postman s već generiranim JWT.

📦 Projektna struktura
Angular frontend (race_application_client)


src/app/
├── core/              # Autentikacija, guardovi, interceptori
├── shared/            # Komponente, modeli, pipeovi
├── features/          # Funkcionalni moduli (races, applications, login)
├── services/          # API komunikacija
├── app-routing.module.ts
└── app.module.ts
Spring Boot servisi (command-service, query-service)
css

src/main/java/com/trail/
├── controller/
├── entity/
├── event/
├── repository/
├── service/
└── security/
🧪 Testni podaci
Automatski se unose prilikom pokretanja docker-compose, kroz init.sql:

sql

INSERT INTO race (...) VALUES (...);
INSERT INTO application (...) VALUES (...);
🔐 Autentikacija (JWT)
Tokeni se šalju kao Authorization: Bearer <token> u headerima.

Spring koristi JwtAuthenticationFilter (custom) i JwtDecoder (resource-server).

🛠️ Build i Makefile

makefile

run:
	docker-compose up --build

stop:
	docker-compose down -v

build-client:
	cd race_application_client && npm install && npm run build

build-command:
	cd race-application-command-service && ./gradlew clean build

build-query:
	cd race-application-query-service && ./gradlew clean build
📎 Dodatno
DB pristup (DBeaver/psql): localhost:5432, user: postgres, pass: postgres

Spring profiles: koristi application.yml, svi podaci konfigurirani kroz env promjenjive

CORS: Omogućen za http://localhost:4200 u oba servisa

✅ TODO (za produkciju)
 Validacija i obavijesti u Angularu

 Registracija korisnika

 Reset lozinke

 Logging + monitoring

 Deploy na cloud (npr. Fly.io, Render, Heroku, GCP)

🧠 Autor
Projekt za vježbu mikroservisa s CQRS, RabbitMQ i JWT.
