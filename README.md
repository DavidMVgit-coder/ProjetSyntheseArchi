# ProjetSyntheseArchi

Projet sur D&D pour architecture logicielle.

# API REST des héros

Cette API REST, développée en Java avec **Jersey (JAX-RS)**, utilise un stockage en mémoire pour gérer des héros.

Elle permet de créer un héros avec `POST /heroes`, puis de le consulter avec `GET /heroes/{id}`.

Lors de la création d'un héros, l'API retourne le statut HTTP `201 Created`, ainsi que l'identifiant du héros dans le corps de la réponse et son URL dans l'en-tête `Location`.

## Prérequis

- Java (version compatible avec le projet)
- Maven
- Jersey (JAX-RS)

## Lancement de l'API

À la racine du projet, compiler l'application avec Maven :

```bash
mvn clean package
```

La commande de démarrage sera précisée lorsque le serveur d'exécution de Jersey aura été choisi et configuré.

L'API est prévue pour écouter sur le port `8080`.

## Endpoints disponibles

| Méthode | Endpoint | Description |
|---|---|---|
| POST | `/heroes` | Créer un nouveau héros |
| GET | `/heroes/{id}` | Consulter un héros à partir de son identifiant |

## Exemple de création d'un héros

**Requête :**

```http
POST /heroes
Content-Type: application/json
```

**Corps JSON :**

```json
{
  "name": "Aria",
  "species": "ELF",
  "heroClass": "RANGER",
  "strength": 10,
  "dexterity": 15,
  "constitution": 12,
  "intelligence": 13,
  "wisdom": 14,
  "charisma": 11
}
```

**Réponse attendue :**

```http
HTTP/1.1 201 Created
Location: http://localhost:8080/heroes/1
Content-Type: application/json
```

**Exemple de corps de réponse :**

```json
{
  "id": 1
}
```

L'identifiant retourné permet ensuite de consulter le héros créé.

## Exemple de consultation d'un héros

**Requête :**

```http
GET /heroes/1
```

**Exemple de réponse attendue :**

```json
{
  "id": 1,
  "name": "Aria",
  "species": "ELF",
  "heroClass": "RANGER",
  "strength": 10,
  "dexterity": 15,
  "constitution": 12,
  "intelligence": 13,
  "wisdom": 14,
  "charisma": 11
}
```
