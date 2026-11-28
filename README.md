# ProjetSyntheseArchi

Projet sur D&D pour architecture logicielle.

API REST développée en Java avec **Jersey (JAX-RS)**. Elle gère des héros, des
comptes joueurs et des lobbies. Toutes les données sont conservées en mémoire et
sont perdues lorsque le serveur est arrêté.

Le code est découpé en modules (`hero`, `player`, `lobby`, `item`), chacun
organisé en couches `domain`, `application`, `infrastructure` et `api`.

## Prérequis

- JDK 21
- Maven

L'API utilise Jersey (Jakarta REST), le serveur HTTP Grizzly et Jackson pour le JSON.
Ces dépendances sont gérées par Maven.

## Lancement de l'API

Compiler le projet et exécuter les tests :

```bash
mvn clean verify
```

Lancer l'API à la racine du projet :

```bash
mvn compile exec:java
```

Le serveur utilise HTTP/1.1 et écoute sur `http://localhost:8080/`. Utiliser `Ctrl+C` pour l'arrêter.
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
| POST | `/players` | Créer un compte joueur |
| GET | `/players/{playerId}` | Consulter son propre profil (joueur authentifié) |
| POST | `/lobbies` | Créer un lobby (joueur authentifié) |
| DELETE | `/lobbies/{lobbyId}/players/{playerId}` | Quitter un lobby (joueur authentifié) |

## Format des erreurs

Toutes les erreurs ont le même format JSON :

```json
{
  "timestamp": "2026-11-26T21:30:36.863Z",
  "status": 400,
  "code": "INVALID_REQUEST",
  "message": "La caractéristique STRENGTH doit être comprise entre 3 et 18 (valeur reçue : 25).",
  "path": "/heroes"
}
```

Un corps JSON mal formé donne `400 INVALID_REQUEST`.

# Héros

## Création d'un héros

**Requête :**

```http
POST /heroes
Content-Type: application/json
```

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

L'identifiant est un UUID généré à la création.

Règles de validation :

- le nom contient de 2 à 30 caractères, uniquement des lettres et des espaces
  simples, sans espace au début ni à la fin ; il doit être unique ;
- les espèces et classes acceptent les noms des enums du domaine, sans tenir
  compte de la casse : `HUMAN`, `ELF`, `DWARF`, `ORC` pour les espèces ;
  `WARRIOR`, `RANGER`, `MAGE`, `CLERIC` pour les classes ;
- chaque caractéristique est comprise entre 3 et 18 avant le bonus d'espèce.

| Situation | Statut HTTP | Code d'erreur |
|---|---|---|
| Donnée absente ou invalide | `400` | `INVALID_REQUEST` |
| Nom déjà utilisé par un autre héros | `409` | `HERO_ALREADY_EXISTS` |

## Consultation d'un héros

**Requête :**

```http
GET /heroes/550e8400-e29b-41d4-a716-446655440000
```

**Réponse attendue :** `200 OK` avec le même corps que lors de la création.

| Situation | Statut HTTP | Code d'erreur |
|---|---|---|
| Identifiant qui n'est pas un UUID | `400` | `INVALID_REQUEST` |
| Aucun héros avec cet identifiant | `404` | `HERO_NOT_FOUND` |

# Joueurs

## Création d'un compte

`POST /players` crée un compte sans authentification préalable.

```json
{
  "pseudonym": "Aria",
  "email": "aria@mail.com",
  "password": "dragon-2026"
}
```

Réponse `201 Created`, avec l'URL du joueur dans l'en-tête `Location`. Le mot de
passe n'est jamais renvoyé ; il est stocké haché avec Argon2.

```json
{
  "id": "5174e1a0-4b78-3b22-82db-ec7a316e4725",
  "pseudonym": "Aria",
  "email": "aria@mail.com"
}
```

Règles de validation :

- le pseudonyme contient de 3 à 20 lettres, chiffres ou `_` ;
- le courriel doit être une adresse valide d'au plus 254 caractères ;
- le mot de passe contient au moins 8 caractères ;
- le pseudonyme et le courriel sont uniques, sans tenir compte de la casse.

| Situation | Statut HTTP | Code d'erreur |
|---|---|---|
| Donnée absente ou invalide | `400` | `INVALID_REQUEST` |
| Pseudonyme déjà utilisé | `409` | `PSEUDONYM_TAKEN` |
| Courriel déjà utilisé | `409` | `EMAIL_TAKEN` |
| Mot de passe trop court | `422` | `PASSWORD_TOO_SHORT` |

## Consultation du profil

`GET /players/{playerId}` renvoie le profil du joueur connecté. Aucun corps de
requête : l'identité vient du contexte d'authentification (voir
« Raccordement à l'authentification »).

Réponse `200 OK`. Le mot de passe et son hachage ne sont jamais renvoyés.

```json
{
  "id": "5174e1a0-4b78-3b22-82db-ec7a316e4725",
  "pseudonym": "Aria",
  "email": "aria@mail.com"
}
```

Un joueur ne peut consulter que son propre profil. Demander un autre
identifiant donne toujours `403`, que ce joueur existe ou non : la route ne
révèle pas l'existence des autres comptes.

| Situation | Statut HTTP | Code d'erreur |
|---|---|---|
| Joueur non authentifié | `401` | `AUTHENTICATION_REQUIRED` |
| Profil d'un autre joueur | `403` | `FORBIDDEN` |
| Joueur authentifié sans compte | `404` | `PLAYER_NOT_FOUND` |

## Connexion

La ressource `SessionResource` (`POST /sessions`) et le cas d'utilisation
`LoginUseCase` existent, mais ne sont pas encore branchés dans
`ApiConfiguration` : il manque une implémentation de `TokenService`. Tant que ce
n'est pas fait, `POST /sessions` répond `404`.

Les erreurs de connexion sont déjà traduites par le gestionnaire d'erreurs :
`401 INVALID_CREDENTIALS` pour des identifiants invalides et
`400 INVALID_REQUEST` pour un courriel ou un mot de passe manquant.

# Lobbies

## Création d'un lobby

`POST /lobbies` crée un lobby pour le joueur authentifié. Aucun corps de requête
n'est nécessaire : l'identité de l'hôte vient du contexte d'authentification du
serveur. Les identifiants de joueurs sont distincts des identifiants de héros.

Le créateur devient l'hôte et l'unique membre du lobby. Le lobby est `OPEN`, sa
capacité est de 4 joueurs et il reste 3 places. Un joueur déjà membre d'un lobby
`OPEN` ne peut pas en créer un autre, même avec deux requêtes simultanées.

Exemple de réponse à une création authentifiée (`201 Created`) :

```json
{
  "id": "e31b7953-827c-4859-9224-7f2b16a5da59",
  "hostId": "8feadfd3-8280-4d70-8770-fc28c6641c22",
  "memberIds": ["8feadfd3-8280-4d70-8770-fc28c6641c22"],
  "capacity": 4,
  "availableSlots": 3,
  "status": "OPEN"
}
```

| Situation | Statut HTTP | Code d'erreur |
|---|---|---|
| Identité authentifiée absente ou inexploitable | `401` | `AUTHENTICATION_REQUIRED` |
| Joueur déjà membre d'un lobby ouvert | `409` | `PLAYER_ALREADY_IN_OPEN_LOBBY` |

## Quitter un lobby

`DELETE /lobbies/{lobbyId}/players/{playerId}` retire le joueur du lobby et
répond `204 No Content`. Un joueur ne peut retirer que lui-même.

Si l'hôte quitte le lobby, le membre suivant devient l'hôte. Quand le dernier
membre part, le lobby passe à `CLOSED`.

| Situation | Statut HTTP | Code d'erreur |
|---|---|---|
| Identité authentifiée absente ou inexploitable | `401` | `AUTHENTICATION_REQUIRED` |
| Tentative de retirer un autre joueur | `403` | `FORBIDDEN` |
| Lobby inexistant | `404` | `LOBBY_NOT_FOUND` |
| Joueur qui n'est pas membre du lobby | `404` | `PLAYER_NOT_IN_LOBBY` |
| Partie déjà commencée | `409` | `GAME_ALREADY_STARTED` |

## Raccordement à l'authentification

Les routes des lobbies et `GET /players/{playerId}` exigent une identité vérifiée. Un filtre Jersey devra
valider le jeton de connexion puis installer un `SecurityContext` dont
`getUserPrincipal().getName()` contient l'UUID du joueur connecté. Fournir un
`playerId` dans le JSON ou dans un en-tête ne suffit pas à s'authentifier.

Tant que ce filtre n'est pas branché, le serveur refuse ces routes avec `401`. Les tests HTTP utilisent un filtre réservé aux tests pour exercer les
cas authentifiés.
