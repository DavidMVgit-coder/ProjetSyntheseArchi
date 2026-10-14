# ProjetSyntheseArchi

Projet sur D&D pour architecture logicielle.

## API REST des héros

L'API en mémoire permet de créer un héros avec POST /heroes, puis de le consulter avec GET /heroes/{id}. La création répond 201 Created et retourne l'identifiant dans le corps et dans l'en-tête Location.

Lancer l'API avec Java 17 à 26 et Maven en exécutant mvn spring-boot:run à la racine du projet. Le serveur écoute sur le port 8080.

Exemple de corps JSON pour POST /heroes :

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

La réponse fournit un champ id; utiliser sa valeur dans GET /heroes/{id}. Les espèces et classes acceptent les noms définis par les enums du domaine, sans tenir compte de la casse.
