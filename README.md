# ProjetSyntheseArchi

Projet sur D&D pour architecture logicielle.

# API REST des héros

Cette API REST, développée en Java avec **Jersey (JAX-RS)**, utilise un stockage en mémoire pour gérer des héros.

Elle permet de créer un héros avec `POST /heroes`, puis de le consulter avec `GET /heroes/{id}`.

Le projet cible Java 21. Utiliser un JDK 21 pour compiler et lancer l'API avec Maven.

Lors de la création d'un héros, l'API retourne le statut HTTP `201 Created`, le héros créé dans le corps de la réponse et son URL dans l'en-tête `Location`.

## Prérequis

- JDK 21
- Maven

L'API utilise Jersey (Jakarta REST), le serveur HTTP Grizzly et Jackson pour le JSON.
Ces dépendances sont gérées par Maven.

## Lancement de l'API

Compiler le projet et exécuter les tests HTTP :

```bash
mvn clean verify
```

Lancer l'API à la racine du projet :

```bash
mvn compile exec:java
```

Le serveur utilise HTTP/1.1 et écoute sur `http://localhost:8080/`. Utiliser `Ctrl+C` pour l'arrêter.
Les héros sont conservés en mémoire et sont perdus lorsque le serveur est arrêté.
Pour un client Java `HttpClient`, sélectionner `HttpClient.Version.HTTP_1_1` ;
la négociation HTTP/2 n'est pas prise en charge par cette configuration.

Dans IntelliJ IDEA, ouvrir le fichier `pom.xml` avec **File > Open**, puis choisir
**Open as Project** pour charger le projet Maven. Sélectionner un JDK 21 pour le
projet et pour le lancement des tâches Maven.

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
Location: http://localhost:8080/heroes/550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json
```

**Exemple de corps de réponse :**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Aria",
  "species": "ELF",
  "heroClass": "RANGER",
  "level": 1,
  "currentHitPoints": 11,
  "maxHitPoints": 11,
  "currentMana": 0,
  "maxMana": 0
}
```

L'identifiant est un UUID généré à la création. Remplacer celui de l'exemple par
l'identifiant retourné pour consulter le héros créé.

Les espèces et classes acceptent les noms définis par les enums du domaine,
sans tenir compte de la casse : `HUMAN`, `ELF`, `DWARF`, `ORC` pour les espèces ;
`WARRIOR`, `RANGER`, `MAGE`, `CLERIC` pour les classes.

## Exemple de consultation d'un héros

**Requête :**

```http
GET /heroes/550e8400-e29b-41d4-a716-446655440000
```

**Exemple de réponse attendue :**

```http
HTTP/1.1 200 OK
Content-Type: application/json
```

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Aria",
  "species": "ELF",
  "heroClass": "RANGER",
  "level": 1,
  "currentHitPoints": 11,
  "maxHitPoints": 11,
  "currentMana": 0,
  "maxMana": 0
}
```
