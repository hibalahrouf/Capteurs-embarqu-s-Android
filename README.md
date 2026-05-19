# SensorExplorerApp — Android Sensors Laboratory

## Présentation

SensorExplorerApp est une application Android développée en Java permettant d’explorer et d’exploiter plusieurs capteurs embarqués d’un smartphone Android.

L’application utilise :

- SensorManager
- SensorEventListener
- Fragments
- Graphiques temps réel personnalisés

Le projet démontre l’utilisation de nombreux capteurs Android ainsi qu’une logique simple de reconnaissance d’activité.

---

# Demonstration

https://github.com/user-attachments/assets/cd7de3a6-0a7e-42e0-b611-a1f3db0b9417

---

# Package de l’application

```java
com.example.sensorexplorerapp
```

---

# Fonctionnalités implémentées

## Liste des capteurs disponibles

Affichage complet des capteurs détectés sur le téléphone avec :

- nom
- fabricant
- version
- type textuel
- type entier
- résolution
- consommation énergétique
- portée maximale
- délai minimal
- mode de reporting

---

## Capteurs avec graphe temps réel

L’application affiche des mesures sous forme de courbes dynamiques pour :

- Température
- Humidité
- Proximité
- Champ magnétique
- Accéléromètre
- Gravité
- Gyroscope

---

## Compteur de pas

Implémentation du capteur :

```text
TYPE_STEP_COUNTER
```

Fonctionnalités :

- pas depuis le démarrage du téléphone
- pas de la session actuelle
- gestion de permission ACTIVITY_RECOGNITION

---

## Boussole numérique

Création d’une boussole à partir de :

- accéléromètre
- magnétomètre

Affichage :

- direction en degrés
- direction textuelle (Nord, Est, Sud, Ouest…)

---

## Reconnaissance simple d’activité

Détection approximative de :

- téléphone stable
- mouvement léger
- marche
- saut ou mouvement brusque

La reconnaissance repose sur l’analyse des données de l’accéléromètre.

---

# Organisation du projet

```text
app/src/main/java/com/example/sensorexplorerapp/

├── fragments/
│   ├── SensorsListFragment.java
│   ├── LiveSensorFragment.java
│   ├── MotionSensorFragment.java
│   ├── StepCounterFragment.java
│   ├── CompassFragment.java
│   └── ActivityRecognitionFragment.java
│
├── utils/
│   ├── SensorDetails.java
│   └── SampleSimulator.java
│
├── views/
│   └── LineChartView.java
│
└── MainActivity.java
```

---

# Description des fichiers principaux

## MainActivity.java

Gère :
- la navigation
- l’ouverture des fragments
- le menu principal

---

## SensorsListFragment.java

Affiche tous les capteurs disponibles sur le dispositif Android.

---

## LiveSensorFragment.java

Fragment réutilisable pour :

- température
- humidité
- proximité
- champ magnétique

Affiche :
- valeur courante
- graphe temps réel

---

## MotionSensorFragment.java

Fragment utilisé pour :

- accéléromètre
- gravité
- gyroscope

Affiche :
- valeurs X, Y, Z
- norme du vecteur
- graphe dynamique

---

## StepCounterFragment.java

Gère :
- le compteur de pas
- la permission runtime
- les statistiques de marche

---

## CompassFragment.java

Implémente une boussole numérique basée sur :

- Sensor.TYPE_ACCELEROMETER
- Sensor.TYPE_MAGNETIC_FIELD

---

## ActivityRecognitionFragment.java

Réalise une classification simple des mouvements à partir de l’accéléromètre.

---

## SensorDetails.java

Classe utilitaire permettant de formatter les informations techniques des capteurs.

---

## SampleSimulator.java

Génère des valeurs simulées lorsque certains capteurs ne sont pas disponibles.

---

## LineChartView.java

Vue personnalisée permettant de dessiner les graphes temps réel sans bibliothèque externe.

---

# Technologies utilisées

- Java
- Android Studio
- SensorManager
- SensorEventListener
- Fragments
- Canvas
- Path
- Custom Views

---

# Permissions utilisées

Dans `AndroidManifest.xml` :

```xml
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION"/>
```

---

# Compilation du projet

## Android Studio

1. Ouvrir le projet
2. Vérifier le SDK Android
3. Lancer Gradle Sync
4. Exécuter l’application

---

## Build via terminal

```bash
.\gradlew.bat assembleDebug
```

---

# Tests réalisés

## Test 1 — Liste des capteurs

Menu :
```text
Sensors
```

Résultat attendu :

- affichage de tous les capteurs
- affichage des caractéristiques techniques

---

## Test 2 — Température

Menu :
```text
Temperature
```

Résultat attendu :

- affichage de la température
- mise à jour du graphe

---

## Test 3 — Humidité

Menu :
```text
Humidity
```

Résultat attendu :

- évolution dynamique de l’humidité

---

## Test 4 — Proximité

Menu :
```text
Proximity
```

Résultat attendu :

- changement de valeur lorsque la main approche du capteur

---

## Test 5 — Champ magnétique

Menu :
```text
Magnetic Field
```

Résultat attendu :

- variation du champ magnétique lors de la rotation du téléphone

---

## Test 6 — Accéléromètre

Menu :
```text
Accelerometer
```

Résultat attendu :

- affichage des axes X, Y, Z
- variation lors des mouvements

---

## Test 7 — Gravité

Menu :
```text
Gravity
```

Résultat attendu :

- affichage de la composante gravitationnelle

---

## Test 8 — Gyroscope

Menu :
```text
Gyroscope
```

Résultat attendu :

- variation de la rotation selon les axes

---

## Test 9 — Compteur de pas

Menu :
```text
Steps
```

Résultat attendu :

- affichage des pas
- incrémentation lors de la marche

---

## Test 10 — Boussole

Menu :
```text
Compass
```

Résultat attendu :

- affichage des degrés
- affichage des directions cardinales

---

## Test 11 — Reconnaissance d’activité

Menu :
```text
Activity
```

Tester :

- téléphone immobile
- marche
- secousse
- rotation

Résultat attendu :

- classification du mouvement détecté

---

# Test avec l’émulateur Android

Dans Android Emulator :

```text
Extended Controls > Virtual Sensors
```

Permet de simuler :

- accéléromètre
- magnétomètre
- proximité
- autres capteurs supportés



---

# Mode simulation

Lorsque certains capteurs sont absents :

- l’application active automatiquement des valeurs simulées ;
- les graphes restent fonctionnels ;
- les écrans restent testables sur émulateur.

---



# Résultat final

L’application finale permet :

- d’explorer les capteurs Android ;
- de visualiser des mesures temps réel ;
- d’exploiter les capteurs de mouvement ;
- de créer une boussole numérique ;
- de mettre en œuvre une logique simple de reconnaissance d’activité.

Le projet respecte une architecture modulaire basée sur des fragments et des composants réutilisables.

---

