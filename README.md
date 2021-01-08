#### Antoine LECQUE
#### L3 informatique Université Clermont - Auvergne 

# Rapport TP Web-Serveur mini - blog

## Technologies utilisées :
Pour ce projet j'ai choisi d'utiliser comme base le projet java donnée en exemple. Cela me permettait d'avoir comme base un build gradle avec les librairies deja installées avec des fichiers de base et un exemple avec les articles qui me permettait de savoir plus facilement ce que je devais faire.

De plus les vidéos d'aide fournis utilisait cette technologie, cela permet donc de plus facilement implementer ce qu'on apprend grâce à ces vidéos (qui m'ont été par ailleurs d'une grande aide).

Concernant les difficultés rencontrées, je n'ai pas réussi à faire le REST avec une interface web. J'ai préféré me concentrer sur les requêtes http et l'api puisque c'est sur cela que porte ce cours. De plus je n'ai pas réussis à récupérer les informations d'une requête si celle-ci sont en format JSON dans le body.
Je récupère donc les informations dans l'url de la requête. 

Par exemple pour le login l'url complet de la requête sera : 

http://localhost:8081/api/login?username={username}&password={password}


## Fonctionnement du projet :

### Pour lancer le serveur : 
./gradlew run

### Pour tester les requêtes :
Un utilisateur admin est déjà créé :

**username :** Admin

**mdp :** admin0

### Endpoints REST :
Toutes les requêtes de création/modification/suppression **(sauf pour la création d'un utilisateur)** nécessites un token d'authentification dans le header.


Ce token est donné dans le header de la réponse a un login.

#### Articles :

GET /api/articles -> Récupère tous les articles.

GET /api/articles/{id} -> Récupère un articles.

POST /api/articles -> crée un article.

PUT /api/articles -> met à jour un article.

DELETE /api/articles -> supprime un article
#### Commentaires :
GET /api/comments -> Récupère tous les commentaires.

GET /api/comments/{id} -> Récupère un commentaire.

POST /api/comments -> crée un commentaire.

PUT /api/comments -> met à jour un commentaire.

DELETE /api/articles -> supprime un commentaire.
#### Utilisateurs :
GET /api/users -> Récupère tous les utilisateurs.

GET /api/users/{id} -> Récupère un utilisateur.

POST /api/users -> crée un utilisateur.

PUT /api/users -> met à jour un utilisateur.

DELETE /api/users -> supprime un utilisateur.

### Login
POST /api/login -> permet de récupérer un token de connexion pour pouvoir effectuer des requêtes.
