# ReleveTerrain

## Vue d'ensemble

**ReleveTerrain** est une application Android conçue pour la collecte de données sur le terrain. Elle permet aux utilisateurs d'enregistrer les détails d'un site, de capturer les coordonnées GPS, de prendre des observations, et de sauvegarder ces données pour les consulter ultérieurement. L'application est optimisée pour être simple et efficace.

---

## Fonctionnalités

1. **Collecte de données sur le terrain** :
   - Enregistrer le nom du site, la date, la description et le type de terrain.
   - Capturer les coordonnées GPS directement depuis l'appareil.
   - Ajouter des observations et indiquer l'état du site.

2. **Stockage des données** :
   - Sauvegarde des données localement dans une base de données SQLite.
   - Les informations incluent les détails du site, les coordonnées GPS, le type de terrain et les observations.

3. **Visualisation des données** :
   - Afficher les relevés sauvegardés sous forme de liste.
   - Voir les détails de chaque site enregistré.

4. **Interface utilisateur intuitive** :
   - Formulaires conviviaux pour l'entrée des données.
   - Mise en page défilable pour une meilleure accessibilité sur les petits écrans.

---

## Structure de l'application

### Activités

#### 1. **MainActivity**
   - **Objectif** : Formulaire principal pour la collecte de données.
   - **Composants clés** :
     - Champs pour le nom du site, la date et la description.
     - Liste déroulante pour sélectionner le type de terrain.
     - Boutons pour capturer les coordonnées GPS, sauvegarder les entrées et consulter les relevés enregistrés.
   - **Logique** :
     - Gestion des permissions GPS et validation.
     - Les coordonnées GPS capturées sont affichées et sauvegardées avec les autres détails du site.
     - Les entrées sont enregistrées dans la base de données SQLite à l'aide de `DatabaseHelper`.
   - **Mise en page associée** : `activity_main.xml`.

#### 2. **ViewListActivity**
   - **Objectif** : Affiche les relevés sauvegardés sous forme de liste.
   - **Composants clés** :
     - Une `ListView` pour afficher toutes les entrées sauvegardées.
   - **Logique** :
     - Récupère les données de la base de données SQLite à l'aide de `DatabaseHelper`.
     - Configure un `SimpleCursorAdapter` pour lier les données de la base à la `ListView`.
   - **Mise en page associée** : `activity_view_list.xml`.

---

### Layouts

#### 1. **activity_main.xml**
   - **Objectif** : Mise en page pour la saisie des données sur le terrain.
   - **Structure** :
     - **ScrollView** : Rend la mise en page défilable.
     - **LinearLayout** : Contient tous les éléments du formulaire.
     - Champs d'entrée pour les détails du site (nom, date, description, etc.).
     - Boutons pour capturer les coordonnées GPS et sauvegarder les données.
     - Liste déroulante pour le type de terrain et cases à cocher pour l'état du site.

#### 2. **activity_view_list.xml**
   - **Objectif** : Affiche les relevés enregistrés dans une liste.
   - **Structure** :
     - **ScrollView** : Garantit l'accessibilité sur des écrans plus petits.
     - **LinearLayout** : Contient le titre et une `ListView`.
     - `ListView` : Affichée dynamiquement avec les relevés sauvegardés.

#### 3. **item_entry.xml**
   - **Objectif** : Définit la structure de chaque élément dans la `ListView`.
   - **Structure** :
     - Affiche le nom du site, la date, les coordonnées GPS, la description, le type de terrain, les observations, et l'état.
     - Style épuré avec des marges et des espacements.

---

### Base de données

#### **DatabaseHelper**
   - **Objectif** : Gérer les opérations de base de données SQLite.
   - **Nom de la base** : `ReleveTerrain.db`
   - **Table** : `entries`
   - **Colonnes** :
     - `id` (clé primaire) : Identifiant unique pour chaque entrée.
     - `site_name` : Nom du site.
     - `date` : Date de l'entrée.
     - `coordinates` : Coordonnées GPS du site.
     - `description` : Description du site.
     - `terrain_type` : Type de terrain.
     - `observations` : Observations supplémentaires.
     - `condition` : État du site.

   - **Opérations** :
     - **Insertion** : Ajouter une nouvelle entrée.
     - **Récupération** : Lire toutes les entrées pour les afficher dans `ViewListActivity`.

---

### Ressources

#### 1. **Chaînes de caractères (`res/values/strings.xml`)**
   - Définit le nom de l'application (`ReleveTerrain`).
   - Inclut un tableau de types de terrains (`Herbe`, `Sable`, `Roche`, etc.).

#### 2. **Permissions (`AndroidManifest.xml`)**
   - Demande les permissions `ACCESS_FINE_LOCATION` et `ACCESS_COARSE_LOCATION` pour les fonctionnalités GPS.

#### 3. **Manifeste de l'application**
   - Déclare :
     - `MainActivity` comme activité principale (lancement).
     - `ViewListActivity` pour afficher les relevés sauvegardés.
   - Prend en charge la sauvegarde et les configurations RTL.

---

## Flux de travail de l'application

1. **Lancer l'application** :
   - L'utilisateur est dirigé vers `MainActivity`.
   - Remplir les détails du site, capturer les coordonnées GPS, et ajouter des observations.

2. **Enregistrer les données** :
   - Appuyer sur "Enregistrer" pour sauvegarder les données localement dans SQLite.

3. **Consulter les relevés sauvegardés** :
   - Naviguer vers `ViewListActivity` via le bouton "Voir les Relevés Sauvegardés".
   - Parcourir tous les relevés enregistrés dans une liste.

---

## Points forts techniques

- **Intégration GPS** :
   - Utilisation de `FusedLocationProviderClient` pour obtenir des coordonnées GPS précises.
   - Gestion des permissions de localisation de manière dynamique.

- **Base de données SQLite** :
   - Stockage local des entrées.
   - Récupération et affichage efficaces des données.

- **Interface utilisateur dynamique** :
   - Formulaires défilables pour une accessibilité sur différents appareils.
   - Utilisation de composants comme `Spinner`, `EditText` et `CheckBox` pour une saisie fluide.

---

## Instructions d'installation

1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/najoua-elhabti1/PROJET_Formulaire-de-Releve-de-Terrain_G5

2. **Ouvrir dans Android Studio** :

- Importer le projet dans Android Studio.
- Synchroniser les fichiers Gradle.
3. **Exécuter l'application** :

Construire et déployer sur un émulateur ou un appareil physique.

## Améliorations futures
1. **Export des données** :
Ajouter une option pour exporter les données sous forme de fichier CSV ou PDF.
2. **Intégration cloud** :
Synchronisation des relevés avec un stockage cloud.
3. **Cartes hors ligne** :
Intégrer des cartes pour visualiser les localisations.