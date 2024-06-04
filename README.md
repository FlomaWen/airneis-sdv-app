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

Le projet est organisé selon les meilleures pratiques en matière d'architecture logicielle, utilisant le modèle MVVM (Model-View-ViewModel) pour séparer les préoccupations.

#### Arborescence des Dossiers

- `component/Account/AccountContent.kt` : Contenu de la page de compte.
- `component/Account/AdressDialog.kt` : Dialogues pour l'ajout et la modification d'adresses.
- `component/Account/AdressesList.kt` : Liste des adresses de l'utilisateur.
- `component/Account/DialogAccount.kt` : Dialogues spécifiques aux comptes utilisateurs.
- `component/Account/PasswordChangeDialog.kt` : Dialogue pour le changement de mot de passe.
- `component/Account/PaymentList.kt` : Liste des méthodes de paiement.

- `component/Caroussels/CarousselData.kt` : Gestion des données pour les caroussels de produits.
- `component/Caroussels/CarousselNoData.kt` : Affichage d'un caroussel sans données.

- `component/Forms/LoginForm.kt` : Formulaire de connexion.
- `component/Forms/RegistrationForm.kt` : Formulaire d'inscription.

- `component/GlobalApp/Drawer/AppTopBar.kt` : Barre supérieure de l'application.
- `component/GlobalApp/Drawer/ButtonStyled.kt` : Boutons stylisés.
- `component/GlobalApp/Drawer/SimpleButtonNav.kt` : Navigation avec des boutons simples.
- `component/GlobalApp/Drawer/SortByPriceText.kt` : Texte pour le tri par prix.
- `component/GlobalApp/Drawer/TextInputStyled.kt` : Champs de saisie stylisés.

- `data/AuthTokenStorage.kt` : Gestion du stockage des tokens d'authentification.
- `data/FiltersState.kt` : État des filtres de produits.

- `model/Drawer/AccountModel.kt` : Modèle de données pour les comptes utilisateurs.
- `model/Drawer/AdressesModel.kt` : Modèle de données pour les adresses.
- `model/Drawer/CartModel.kt` : Modèle de données pour le panier.
- `model/Drawer/CategoryModel.kt` : Modèle de données pour les catégories de produits.
- `model/Drawer/LoginModel.kt` : Modèle de données pour la connexion.
- `model/Drawer/MaterialsModel.kt` : Modèle de données pour les matériaux des produits.
- `model/Drawer/PaymentModel.kt` : Modèle de données pour les méthodes de paiement.
- `model/Drawer/ProductModel.kt` : Modèle de données pour les produits.
- `model/Drawer/SignUpModel.kt` : Modèle de données pour l'inscription.

- `ui.theme/Color.kt` : Définition des couleurs de l'application.
- `ui.theme/Theme.kt` : Thèmes de l'application.
- `ui.theme/Type.kt` : Définition des types de texte.

- `util/Base_url_api.kt` : Configuration de l'URL de base de l'API.
- `util/Functions.kt` : Fonctions utilitaires.

- `view/AccountScreen.kt` : Écran de gestion du compte utilisateur.
- `view/CartScreen.kt` : Écran du panier.
- `view/LoginScreen.kt` : Écran de connexion.
- `view/MainScreen.kt` : Écran principal.
- `view/OrderScreen.kt` : Écran de récapitulatif de commande.
- `view/ProductDetailScreen.kt` : Écran de détail des produits.
- `view/ProductScreen.kt` : Écran de liste des produits.
- `view/SearchScreen.kt` : Écran de recherche de produits.
- `view/SignUpScreen.kt` : Écran d'inscription.

- `viewmodel/Account/AccountViewModel.kt` : ViewModel pour les comptes utilisateurs.
- `viewmodel/Account/AdressesViewModel.kt` : ViewModel pour les adresses.
- `viewmodel/Account/PaymentViewModel.kt` : ViewModel pour les méthodes de paiement.
- `viewmodel/Login/LoginUIEvent.kt` : Événements UI pour la connexion.
- `viewmodel/Login/LoginViewModel.kt` : ViewModel pour la connexion.
- `viewmodel/SignUp/SignUpUIEvent.kt` : Événements UI pour l'inscription.
- `viewmodel/SignUp/SignUpViewModel.kt` : ViewModel pour l'inscription.
- `viewmodel/CartViewModel.kt` : ViewModel pour le panier.
- `viewmodel/CategoryViewModel.kt` : ViewModel pour les catégories de produits.
- `viewmodel/LogoutViewModel.kt` : ViewModel pour la déconnexion.
- `viewmodel/MaterialsViewModel.kt` : ViewModel pour les matériaux des produits.
- `viewmodel/ProductsViewModel.kt` : ViewModel pour les produits.
- `viewmodel/MainActivity.kt` : Activité principale de l'application.

### Modèle Vue VueModel (MVVM)

L'application utilise l'architecture MVVM pour séparer les préoccupations et faciliter la maintenance du code. Voici les principaux composants :

- **Model** : Contient les classes de données et les sources de données (locales ou distantes).
- **View** : Composables Jetpack Compose qui affichent les données et reçoivent les interactions de l'utilisateur.
- **ViewModel** : Gère la logique d'application et les données à afficher. Utilise LiveData ou StateFlow pour réagir aux changements de données.

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
