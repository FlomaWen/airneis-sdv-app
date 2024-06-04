# Projet d'Application Kotlin - SUP DE VINCI

Ce projet est une application développée en Kotlin, conçue dans le cadre d'un projet d'étude pour l'école SUP DE VINCI. L'application vise à illustrer les compétences et les connaissances acquises par les étudiants en développement d'applications mobiles sous Android.

## Table des Matières

1. [Description](#description)
2. [Fonctionnalités](#fonctionnalités)
3. [Installation](#installation)
4. [Utilisation](#utilisation)
5. [Architecture](#architecture)
6. [Technologies Utilisées](#technologies-utilisées)
7. [Contact](#contact)

## Description

### Contexte

Cette application a été développée dans le cadre du programme de développement mobile de SUP DE VINCI. Elle illustre les compétences des étudiants en matière de conception, de développement et de déploiement d'applications Android.

### Objectifs

- Démontrer la maîtrise du langage Kotlin.
- Illustrer l'utilisation des principales bibliothèques et frameworks Android.
- Appliquer les meilleures pratiques en matière d'architecture logicielle.

## Fonctionnalités

- **Visualisation des catégories des produits** : Affiche les différentes catégories de produits disponibles.
- **Page produits filtrés** : Affiche les produits filtrés en fonction de la catégorie choisie avec des filtres supplémentaires comme les prix, les matériaux et la disponibilité en stock.
- **Page détail produit** : Affiche les détails des produits sélectionnés.
- **Page de recherche** : Permet de rechercher parmi tous les produits disponibles.
- **Page de connexion et d'inscription** : Permet aux utilisateurs de se connecter ou de s'inscrire.
- **Page panier** : Accessible uniquement si l'utilisateur est connecté, permet de visualiser et gérer le panier.
- **Page mon compte** : Affiche les informations du compte utilisateur, accessible uniquement si l'utilisateur est connecté.
- **Page récapitulatif de commande** : Affiche un récapitulatif de la commande après le passage par le panier.

## Installation

### Prérequis

- Android Studio installé
- JDK 8 ou supérieur

### Étapes d'Installation

1. Clonez le dépôt :
   ```bash
   git clone https://github.com/votre-repo/projet-kotlin.git
   ```
2. Ouvrez le projet dans Android Studio.
3. Synchronisez les dépendances du projet.
4. Exécutez l'application sur un émulateur ou un appareil Android.

## Utilisation

### Guide d'Utilisation

1. **Visualiser les catégories** : Depuis l'écran d'accueil, sélectionnez une catégorie pour voir les produits associés.
2. **Filtrer les produits** : Utilisez les options de filtrage pour affiner votre recherche de produits par prix, matériaux et disponibilité en stock.
3. **Voir les détails d'un produit** : Cliquez sur un produit pour voir ses détails.
4. **Recherche** : Utilisez la barre de recherche pour trouver des produits spécifiques.
5. **Connexion et inscription** : Accédez à la page de connexion ou d'inscription pour créer un compte ou vous connecter.
6. **Gérer le panier** : Une fois connecté, accédez à la page du panier pour voir et gérer vos articles.
7. **Accéder à votre compte** : Visualisez et modifiez les informations de votre compte depuis la page "Mon compte".
8. **Récapitulatif de commande** : Après avoir ajouté des articles au panier, visualisez le récapitulatif de votre commande.

## Architecture

### Structure du Projet

├── component
│ ├── Account
│ │ ├── AccountContent.kt
│ │ ├── AdressDialog.kt
│ │ ├── AdressesList.kt
│ │ ├── DialogAccount.kt
│ │ ├── PasswordChangeDialog.kt
│ │ └── PaymentList.kt
│ ├── Caroussels
│ │ ├── CarousselData.kt
│ │ └── CarousselNoData.kt
│ ├── Forms
│ │ ├── LoginForm.kt
│ │ └── RegistrationForm.kt
│ └── GlobalApp
│ ├── Drawer
│ │ ├── AppTopBar.kt
│ │ ├── ButtonStyled.kt
│ │ ├── SimpleButtonNav.kt
│ │ ├── SortByPriceText.kt
│ │ └── TextInputStyled.kt
├── data
│ ├── AuthTokenStorage.kt
│ └── FiltersState.kt
├── model
│ ├── Drawer
| | | ├── CustomDrawerState.kt
| | | └── NavigationItem.kt
│ │ ├── AccountModel.kt
│ │ ├── AdressesModel.kt
│ │ ├── CartModel.kt
│ │ ├── CategoryModel.kt
│ │ ├── LoginModel.kt
│ │ ├── MaterialsModel.kt
│ │ ├── PaymentModel.kt
│ │ ├── ProductModel.kt
│ │ └── SignUpModel.kt
├── ui.theme
│ ├── Color.kt
│ ├── Theme.kt
│ └── Type.kt
├── util
│ ├── Base_url_api.kt
│ └── Functions.kt
├── view
│ ├── AccountScreen.kt
│ ├── CartScreen.kt
│ ├── LoginScreen.kt
│ ├── MainScreen.kt
│ ├── OrderScreen.kt
│ ├── ProductDetailScreen.kt
│ ├── ProductScreen.kt
│ ├── SearchScreen.kt
│ └── SignUpScreen.kt
└── viewmodel
├── Account
│ ├── AccountViewModel.kt
│ ├── AdressesViewModel.kt
│ └── PaymentViewModel.kt
├── Login
│ ├── LoginUIEvent.kt
│ └── LoginViewModel.kt
├── SignUp
│ ├── SignUpUIEvent.kt
│ └── SignUpViewModel.kt
├── CartViewModel.kt
├── CategoryViewModel.kt
├── LogoutViewModel.kt
├── MaterialsViewModel.kt
├── ProductsViewModel.kt
└── MainActivity.kt

### Modèle Vue VueModel (MVVM)

L'application utilise l'architecture MVVM pour séparer les préoccupations et faciliter la maintenance du code.

## Technologies Utilisées

- Kotlin
- Android SDK
- Jetpack Compose
- Retrofit
- LiveData et ViewModel
- Coroutine

## Contact

**Nom de l'Étudiant :** Pescot-Jourdan Florian  
**Email :** florian.pescot-jourdan@supdevinci-edu.fr  
**École :** SUP DE VINCI

Merci pour l'intérêt que vous portez à notre projet éducatif !
