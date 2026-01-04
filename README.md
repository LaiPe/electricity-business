# ⚡ Electricity Business

**Plateforme complète de gestion de stations de recharge pour véhicules électriques**

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green?logo=spring)
![React](https://img.shields.io/badge/React-19.1.1-blue?logo=react)
![Vite](https://img.shields.io/badge/Vite-7.2.4-purple?logo=vite)
![MySQL](https://img.shields.io/badge/MySQL-8.3-blue?logo=mysql)
![MongoDB](https://img.shields.io/badge/MongoDB-7.0-green?logo=mongodb)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?logo=docker)
![Nginx](https://img.shields.io/badge/Nginx-Alpine-green?logo=nginx)

---

## 📋 Table des matières

- [Vue d'ensemble](#-vue-densemble)
- [Fonctionnalités](#-fonctionnalités)
- [Stack technologique](#-stack-technologique)
- [Architecture logicielle](#-architecture-logicielle)
- [Guide de déploiement](#-guide-de-déploiement)
- [Documentation](#-documentation)
- [Licence](#-licence)

---

## 🔍 Vue d'ensemble

**Electricity Business** est une plateforme web complète permettant la gestion d'un écosystème de stations de recharge pour véhicules électriques. Elle connecte les propriétaires de bornes de recharge avec les conducteurs de véhicules électriques à la recherche de créneaux de recharge.

Le projet se compose de deux applications distinctes :

```
electricity-business/
├── electricity-business-back/    # API REST (Spring Boot)
└── electricity-business-front/   # Interface utilisateur (React)
```

---

## ✨ Fonctionnalités

### Authentification & Sécurité
- Inscription avec vérification d'email
- Authentification JWT via cookies HTTP-only
- Gestion des rôles (USER, ADMIN) et des statuts (banni, non vérifié)

### Gestion des utilisateurs
- Profils utilisateur personnalisables
- Tableaux de bord adaptés selon le rôle

### Véhicules électriques
- Catalogue de modèles de véhicules avec caractéristiques techniques
- Gestion des véhicules personnels (CRUD)

### Stations de recharge
- Création et gestion de lieux et stations
- Recherche géolocalisée des stations proches
- Carte interactive avec clustering dynamique
- Géocodage direct et inverse

### Réservations
- Réservation de créneaux horaires
- Workflow complet : pending → accepted → ongoing → completed
- Actions : accepter, refuser, annuler, démarrer, terminer
- Système d'évaluation post-recharge
- Export PDF et Excel

---

## 🛠️ Stack technologique

### Backend

| Catégorie | Technologie | Version | Description |
|-----------|-------------|---------|-------------|
| **Runtime** | Java | 21 | Eclipse Temurin |
| **Framework** | Spring Boot | 3.5.3 | Framework principal |
| **Web** | Spring Web | 3.5.3 | API REST |
| **Sécurité** | Spring Security | 6.x | Authentification et autorisation |
| **ORM** | Spring Data JPA | 3.5.3 | Mapping objet-relationnel |
| **NoSQL** | Spring Data MongoDB | 3.5.3 | Accès MongoDB |
| **Validation** | Spring Validation | 3.5.3 | Validation des données |
| **Monitoring** | Spring Actuator | 3.5.3 | Health checks et métriques |
| **JWT** | JJWT | 0.12.3 | Gestion des tokens JWT |
| **Mapping** | MapStruct | 1.5.5 | Mapping DTO ↔ Entity |
| **Utilitaires** | Lombok | latest | Réduction du boilerplate |
| **PDF** | iText 7 | 8.0.2 | Génération de documents PDF |
| **Excel** | Apache POI | 5.2.5 | Génération de fichiers Excel |
| **Build** | Maven | 3.9+ | Gestion des dépendances |

### Bases de données

| Technologie | Version | Usage |
|-------------|---------|-------|
| **MySQL** | 8.3 | Données relationnelles (utilisateurs, véhicules, réservations) |
| **MongoDB** | 7.0 | Données géospatiales (stations avec index 2dsphere) |
| **H2** | latest | Base en mémoire pour développement |

### Frontend

| Catégorie | Technologie | Version | Description |
|-----------|-------------|---------|-------------|
| **Core** | React | 19.1.1 | Bibliothèque UI |
| **DOM** | React DOM | 19.1.1 | Rendu DOM |
| **Routing** | React Router DOM | 7.9.5 | Navigation SPA |
| **Build** | Vite | 7.2.4 | Bundler et dev server |
| **Cartographie** | MapLibre GL | 5.14.0 | Rendu cartographique WebGL |
| **Map React** | react-map-gl | 8.1.0 | Wrapper React pour MapLibre |
| **CSS** | Bootstrap | 5.3.8 | Framework CSS (via CDN) |
| **Icônes** | Bootstrap Icons | 1.13.1 | Icônes SVG |
| **Cookies** | js-cookie | 3.0.5 | Gestion des cookies |
| **JWT** | jwt-decode | 4.0.0 | Décodage des tokens JWT |
| **Validation** | PropTypes | 15.8.1 | Validation des props |
| **Linting** | ESLint | 9.39.1 | Qualité du code |

### APIs externes

| Service | Usage |
|---------|-------|
| **OpenStreetMap Nominatim** | Géocodage (adresse → coordonnées) |
| **Photon (Komoot)** | Géocodage inverse (coordonnées → adresse) |
| **Carto Basemaps** | Tuiles cartographiques |
| **Geolocation API** | Position GPS du navigateur |

### Infrastructure & DevOps

| Technologie | Version | Usage |
|-------------|---------|-------|
| **Docker** | latest | Conteneurisation |
| **Docker Compose** | latest | Orchestration multi-conteneurs |
| **Nginx** | Alpine | Serveur web frontend |
| **Node.js** | 18 Alpine | Build frontend |
| **Maven** | 3.9.6 | Build backend |
| **Eclipse Temurin** | 21 JRE Alpine | Runtime Java production |
| **GHCR** | - | Registry d'images Docker |

---

## 🏗️ Architecture logicielle

### Vue d'ensemble

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT (Navigateur)                             │
└─────────────────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         FRONTEND (React + Vite)                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │   Pages     │  │ Components  │  │  Contexts   │  │       Hooks         │ │
│  │             │  │             │  │             │  │                     │ │
│  │ - Home      │  │ - Form      │  │ - Auth      │  │ - useApiCall        │ │
│  │ - Search    │  │ - Map       │  │ - Bookings  │  │ - useGeolocation    │ │
│  │ - Dashboard │  │ - Spinner   │  │ - List      │  │ - useStationAddress │ │
│  │ - Auth      │  │ - Dashboard │  │ - Error     │  │ - useViewport       │ │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────────────┘ │
│                                       │                                      │
│  ┌────────────────────────────────────┼────────────────────────────────────┐ │
│  │                           Services Layer                                │ │
│  │  BookingService │ StationService │ VehicleService │ UserService │ Geo  │ │
│  └────────────────────────────────────┼────────────────────────────────────┘ │
│                                       │                                      │
│  ┌────────────────────────────────────┼────────────────────────────────────┐ │
│  │                    ApiRequest.js (Client HTTP)                          │ │
│  └────────────────────────────────────┼────────────────────────────────────┘ │
└───────────────────────────────────────┼─────────────────────────────────────┘
                                        │ HTTP (cookies JWT)
                                        ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         BACKEND (Spring Boot)                                │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │                         Security Layer                                  ││
│  │  JwtFilter → SecurityConfig → CORS → BCrypt                            ││
│  └─────────────────────────────────────────────────────────────────────────┘│
│                                       │                                      │
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │                        Controllers (REST API)                           ││
│  │  AuthController │ UserController │ VehicleController │ ...             ││
│  └─────────────────────────────────────────────────────────────────────────┘│
│                                       │                                      │
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │                         Services (Business Logic)                       ││
│  │  AuthService │ UserService │ VehicleService │ StationService │ ...     ││
│  └─────────────────────────────────────────────────────────────────────────┘│
│                                       │                                      │
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │                              DTOs + MapStruct                           ││
│  └─────────────────────────────────────────────────────────────────────────┘│
│                                       │                                      │
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │                           Repositories                                  ││
│  │           JPA Repositories          │       MongoDB Repositories       ││
│  └─────────────────────────────────────────────────────────────────────────┘│
└───────────────────────────────────────┬─────────────────────────────────────┘
                                        │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
        ┌───────────────────────┐               ┌───────────────────────┐
        │       MySQL 8.3       │               │     MongoDB 7.0       │
        │                       │               │                       │
        │ - Users               │               │ - Stations            │
        │ - Vehicles            │               │   (avec index         │
        │ - Bookings            │               │    géospatial         │
        │ - Places              │               │    2dsphere)          │
        │ - VehicleModels       │               │                       │
        └───────────────────────┘               └───────────────────────┘
```

### Structure Backend

```
electricity-business-back/
├── src/main/java/com/laipe/electricitybusiness/
│   ├── config/              # Configuration Spring
│   │   ├── SecurityConfig   # Spring Security + CORS
│   │   ├── JwtFilter        # Filtre d'authentification JWT
│   │   └── MongoConfig      # Configuration MongoDB
│   │
│   ├── controller/          # Endpoints REST
│   │   ├── AuthController   # /api/auth/*
│   │   ├── UserController   # /api/users/*
│   │   ├── VehicleController # /api/vehicles/*
│   │   ├── PlaceController  # /api/places/*
│   │   ├── StationController # /api/stations/*
│   │   └── BookingController # /api/bookings/*
│   │
│   ├── dto/                 # Data Transfer Objects
│   │   ├── request/         # DTOs de requête
│   │   ├── response/        # DTOs de réponse
│   │   └── mapper/          # Mappers MapStruct
│   │
│   ├── model/               # Entités
│   │   ├── jpa/             # Entités JPA (MySQL)
│   │   └── mongo/           # Documents MongoDB
│   │
│   ├── repository/          # Accès données
│   │   ├── jpa/             # Repositories JPA
│   │   └── mongo/           # Repositories MongoDB
│   │
│   ├── service/             # Logique métier
│   │
│   └── utils/               # Utilitaires
│       ├── JwtUtils         # Génération/validation JWT
│       └── ExportUtils      # Génération PDF/Excel
│
├── src/main/resources/
│   ├── application-dev.properties    # Config développement
│   ├── application-preprod.properties # Config pré-production
│   ├── application-prod.properties   # Config production
│   └── data/                         # Données d'initialisation
│
├── Dockerfile               # Image Docker multi-stage
├── docker-compose.yml       # Pré-production
└── docker-compose.prod.yml  # Production
```

### Structure Frontend

```
electricity-business-front/
├── src/
│   ├── main.jsx             # Point d'entrée
│   ├── Router.jsx           # Configuration des routes
│   ├── RouteGuard.jsx       # Protection des routes
│   │
│   ├── components/          # Composants réutilisables
│   │   ├── form/            # Input, Button, Toggle, etc.
│   │   ├── map/             # Carte, markers, popups
│   │   ├── dashboard/       # Composants du tableau de bord
│   │   ├── search/          # Formulaire de recherche
│   │   └── spinner/         # Indicateurs de chargement
│   │
│   ├── contexts/            # État global React
│   │   ├── AuthContext      # Authentification
│   │   ├── BookingsContext  # Réservations
│   │   ├── ListContext      # Listes génériques
│   │   └── GlobalErrorContext # Gestion d'erreurs
│   │
│   ├── hooks/               # Hooks personnalisés
│   │   ├── useApiCall       # Appels API
│   │   ├── useFetch         # Fetch générique
│   │   ├── useGeolocation   # Géolocalisation
│   │   ├── useStationAddress # Adresse des stations
│   │   └── useViewport      # Responsive design
│   │
│   ├── layouts/             # Mise en page
│   │   ├── Header           # Navigation
│   │   ├── Footer           # Pied de page
│   │   └── DashboardLayout  # Layout dashboard
│   │
│   ├── pages/               # Pages de l'application
│   │   ├── Home             # Page d'accueil
│   │   ├── Search           # Recherche de stations
│   │   ├── BookingCreate    # Création de réservation
│   │   ├── auth/            # Login, Register, etc.
│   │   ├── dashboard/       # Pages du dashboard
│   │   └── navigation/      # Error, NotFound, etc.
│   │
│   ├── services/            # Services API
│   │   ├── BookingService   # Réservations
│   │   ├── StationService   # Stations
│   │   ├── VehicleService   # Véhicules
│   │   ├── UserService      # Utilisateurs
│   │   └── GeoService       # Géocodage
│   │
│   ├── utils/               # Utilitaires
│   │   ├── ApiRequest       # Client HTTP
│   │   ├── DateUtils        # Formatage dates
│   │   └── MapUtils         # Utilitaires carte
│   │
│   ├── config/              # Configuration
│   │   └── routes.js        # Définition des routes
│   │
│   └── assets/              # Ressources statiques
│       └── css/globals.css  # Styles globaux
│
├── Dockerfile               # Build multi-stage
├── nginx.conf               # Config Nginx
├── docker-compose.yml       # Dev/Prod
└── vite.config.js           # Configuration Vite
```

### Flux d'authentification

```
┌──────────┐     POST /auth/register      ┌──────────┐     INSERT      ┌──────────┐
│  Client  │ ───────────────────────────► │   API    │ ───────────────►│  MySQL   │
└──────────┘                              └──────────┘                 └──────────┘
                                                │
                                                ▼ Email de vérification
                                                
┌──────────┐     POST /auth/login         ┌──────────┐    Vérification ┌──────────┐
│  Client  │ ───────────────────────────► │   API    │ ───────────────►│  MySQL   │
└──────────┘                              └──────────┘                 └──────────┘
     ▲                                          │
     │         JWT en cookie HTTP-only          │
     └──────────────────────────────────────────┘

┌──────────┐     Requête + Cookie JWT     ┌──────────┐
│  Client  │ ───────────────────────────► │   API    │ ─► Validation JWT ─► Réponse
└──────────┘                              └──────────┘
```

---

## 🚀 Guide de déploiement

### Prérequis

| Outil | Version | Usage |
|-------|---------|-------|
| Java | 21+ | Runtime backend (dev) |
| Node.js | 18+ | Build frontend |
| Maven | 3.9+ | Build backend |
| Docker | 20+ | Conteneurisation |
| Docker Compose | 2+ | Orchestration |
| MongoDB | 7.0 | Requis en dev local |

---

### Partie 1 : Déploiement du Backend

#### Développement local

```bash
# Cloner le projet
git clone --recurse-submodules https://github.com/LaiPe/electricity-business.git
cd electricity-business/electricity-business-back

# Lancer MongoDB localement (requis)
# mongod --dbpath /path/to/data

# Démarrer l'application (profil dev avec H2)
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

**URLs disponibles :**
- API : http://localhost:8080/api
- H2 Console : http://localhost:8080/h2-console
- Health : http://localhost:8080/actuator/health

#### Pré-production (Docker)

```bash
cd electricity-business-back

# Démarrer tous les services (MySQL + MongoDB + API)
docker-compose up -d

# Vérifier les logs
docker-compose logs -f app
```

**Services déployés :**
| Service | Conteneur | Port |
|---------|-----------|------|
| API Spring | eb-app_preprod | 8080 |
| MySQL | eb-mysql_preprod | 3306 |
| MongoDB | eb-mongodb_preprod | 27018 |

#### Production (Docker)

```bash
cd electricity-business-back

# Créer le fichier .env.prod avec les variables sensibles
cat > .env.prod << EOF
MYSQL_ROOT_PASSWORD=secure_root_password
MYSQL_DATABASE=eb_db
MYSQL_USER=eb_user
MYSQL_PASSWORD=secure_password
MONGO_INITDB_ROOT_USERNAME=admin
MONGO_INITDB_ROOT_PASSWORD=secure_mongo_password
MONGO_INITDB_DATABASE=eb_db
PROD_DB_USER=eb_user
PROD_DB_PASS=secure_password
PROD_MONGO_USER=admin
PROD_MONGO_PASS=secure_mongo_password
JWT_SECRET=your_very_long_and_secure_jwt_secret_key
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
EOF

# Déployer en production
docker-compose -f docker-compose.prod.yml up -d
```

**Caractéristiques production :**
- Images depuis GitHub Container Registry
- Réseau interne isolé pour les bases de données
- Volumes persistants pour les données
- Health checks configurés
- Conteneurs en mode read-only
- Capabilities Docker minimales

---

### Partie 2 : Déploiement du Frontend

#### Développement local

```bash
cd electricity-business-front

# Installer les dépendances
npm install

# Créer le fichier .env
echo "VITE_API_URL=http://localhost:8080/api" > .env

# Démarrer le serveur de développement
npm run dev
```

**URL disponible :** http://localhost:5173

#### Production (Docker)

```bash
cd electricity-business-front

# Build et déploiement
docker-compose up -d --build
```

Le Dockerfile effectue un build multi-stage :
1. **Stage Build** : Node.js 18 Alpine compile l'application
2. **Stage Production** : Nginx Alpine sert les fichiers statiques

**Configuration Nginx incluse :**
- Gestion du routage SPA (fallback vers index.html)
- Headers de sécurité
- Compression gzip
- Cache des assets statiques

#### Build manuel (sans Docker)

```bash
cd electricity-business-front

# Variables d'environnement de production
export VITE_API_URL=https://api.your-domain.com/api

# Build de production
npm run build

# Les fichiers sont générés dans dist/
# À servir via Nginx, Apache, ou tout serveur web statique
```

---

### Configuration des environnements

| Environnement | Backend | Frontend | Bases de données |
|---------------|---------|----------|------------------|
| **Dev** | localhost:8080 | localhost:5173 | H2 (mémoire) + MongoDB local |
| **Preprod** | Docker :8080 | Docker :3000 | MySQL + MongoDB (Docker) |
| **Prod** | Docker (GHCR) | Docker (Nginx) | MySQL + MongoDB (volumes) |

---

## 📚 Documentation

| Document | Description |
|----------|-------------|
| [electricity-business-back/README.md](electricity-business-back/README.md) | Documentation du backend |
| [electricity-business-front/README.md](electricity-business-front/README.md) | Documentation du frontend |
| [electricity-business-back/ENDPOINTS.md](electricity-business-back/ENDPOINTS.md) | Documentation de l'API REST |
| [electricity-business-back/ENVIRONNEMENTS.md](electricity-business-back/ENVIRONNEMENTS.md) | Guide des environnements |

---

## 📄 Licence

Ce projet est distribué sous **licence MIT**. Voir les fichiers `LICENSE` pour plus de détails.

---

**Développé avec ❤️ par [LaiPe](https://github.com/LaiPe)**