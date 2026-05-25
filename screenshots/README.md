# 🏋️ LeanMass Calculator

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-7.0+-green.svg)](https://developer.android.com/)
[![Material Design](https://img.shields.io/badge/Material%20Design-3.0-blue.svg)](https://material.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**LeanMass Calculator** - Application Android de calcul de la Masse Corporelle Maigre (LBM) avec la formule de Boer.

## 📱 Captures d'écran

| Splash Screen | Connexion | Inscription |
|---------------|-----------|-------------|
| <img src="screenshots/splash_screen.jpg" width="200"> | <img src="screenshots/login.jpg" width="200"> | <img src="screenshots/register.jpg" width="200"> |

| Calculateur | Résultats | Historique |
|-------------|-----------|------------|
| <img src="screenshots/calculation_results.jpg" width="200"> | <img src="screenshots/calculation_results.jpg" width="200"> | <img src="screenshots/historique.jpg" width="200"> |

## ✨ Fonctionnalités

- 📊 Calcul précis de la Masse Corporelle Maigre (LBM)
- 👤 Authentification utilisateur + Mode invité
- 📜 Historique des mesures avec SQLite
- 🎨 Interface Material Design moderne
- 📤 Partage des résultats
- 🏋️ Formules pour Homme et Femme

## 🛠️ Technologies

- Kotlin
- SQLite
- Material Design 3
- ViewBinding
- Fragments + BottomNavigationView

## 📐 Formule de Boer

**Homme :** `LBM = (0.407 × poids) + (0.267 × taille) - 19.2`

**Femme :** `LBM = (0.252 × poids) + (0.473 × taille) - 48.3`

## 🚀 Installation

```bash
git clone https://github.com/hassaneelhariti/LeanMass.git
cd LeanMass
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk


## 3. Version alternative avec galerie d'images

Si vous préférez une galerie verticale :

```markdown
## 📱 Captures d'écran

### Splash Screen
<img src="screenshots/splash_screen.jpg" width="250">

### Écran de connexion
<img src="screenshots/login.jpg" width="250">

### Écran d'inscription
<img src="screenshots/register.jpg" width="250">

### Calculateur LBM
<img src="screenshots/calculation_results.jpg" width="250">

### Résultats de calcul
<img src="screenshots/calculation_results.jpg" width="250">

### Historique des mesures
<img src="screenshots/historique.jpg" width="250">
