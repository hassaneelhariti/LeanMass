# 🏋️ LeanMass Calculator

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-7.0+-green.svg)](https://developer.android.com/)
[![Material Design](https://img.shields.io/badge/Material%20Design-3.0-blue.svg)](https://material.io/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**LeanMass Calculator** est une application Android moderne qui calcule votre Masse Corporelle Maigre (Lean Body Mass) en utilisant la formule de Boer.

## 📱 Captures d'écran

| Splash | Connexion | Calculateur | Historique |
|--------|-----------|-------------|------------|
| 🖼️ | 🖼️ | 🖼️ | 🖼️ |

## ✨ Fonctionnalités

- ✅ Calcul précis de la Masse Corporelle Maigre (LBM)
- ✅ Formule de Boer (Homme/Femme)
- ✅ Calcul automatique de la masse grasse
- ✅ Authentification utilisateur (inscription/connexion)
- ✅ Mode Invité - Utilisation sans compte
- ✅ Historique des mesures sauvegardé localement
- ✅ Suppression des entrées
- ✅ Partage des résultats
- ✅ Interface Material Design 3
- ✅ Bottom Navigation fixe avec Fragments
- ✅ Thème sombre (Dark Mode)

## 🛠️ Stack Technique

| Catégorie | Technologies |
|-----------|--------------|
| Langage | Kotlin |
| UI | Material Design 3, ViewBinding |
| Architecture | Fragments, MVVM |
| Base de données | SQLite |
| Stockage | SharedPreferences |
| Navigation | BottomNavigationView |

## 📐 Formule de calcul

### Formule de Boer

| Sexe | Formule |
|------|---------|
| 👨 Homme | `LBM = (0.407 × poids) + (0.267 × taille) - 19.2` |
| 👩 Femme | `LBM = (0.252 × poids) + (0.473 × taille) - 48.3` |

### Seuils de satisfaction

| Sexe | LBM minimum |
|------|-------------|
| Homme | ≥ 38.0 kg |
| Femme | ≥ 24.0 kg |

## 🚀 Installation

### Depuis le code source

```bash
# Cloner le projet
git clone https://github.com/hassaneelhariti/LeanMass.git
cd LeanMass

# Builder avec Gradle
./gradlew assembleDebug

# Installer sur le téléphone (ADB requis)
adb install app/build/outputs/apk/debug/app-debug.apk
