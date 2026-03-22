# Rapport de Projet Quoridor

## 1. Manuel d'utilisation
> *(Note pour l'étudiant: Vous pouvez ajouter vos propres captures d'écran PDF ou Images ici pour illustrer les éléments du jeu avant de le rendre)* 

- **Écran d'Accueil (Menu Principal)** : Au lancement, laissez-vous guider et choisissez entre "Jouer", "Règles", "Exemples de parties", ou "Quitter".
- **Menu Paramètres (Configuration)** : Permet de choisir le Mode de jeu et l'Intelligence Artificielle (Alpha-Bêta, Négamax, SSS*) ainsi que la profondeur de l'arbre minimax.
- **En Jeu (Plateau 9x9)** : L'écran de la partie affiche le plateau et les obstacles. Un panel latéral affiche le joueur actuel et le compteur de barrières.  
   - Les encadrés verts indiquent mathématiquement les mouvements valides pour votre pion Bleu. 
   - Un bouton Radio sur la droite vous permet de basculer la souris entre le **"Mode Déplacement"** et le mode de visualisation **"Mode Barrière"** (qui devient gris translucide sur la grille pour prévisualiser). 
   - Le bouton "Annuler coup" annule instantanément l'erreur de jeu humaine, ainsi que la dernière action jouée par l'ordinateur afin de ne pas fausser le tour.

## 2. Description des structures de données
Le jeu repose sur un héritage Model/Service fortement typé et testable unitairement :
- `Pion` et `Barriere` : Géolocalisent formellement les acteurs du plateau via coordonnées `(row, col)` et orientation (Horizonale/Verticale). 
- `Joueur` : Conserve le pointeur vers le pion, gère le stock immuable de 10 instances de barrières dans l'inventaire, avec une constante symbolisant la classe `IA` ou `Humain`.
- `EtatJeu` : C'est le noeud absolu. Il intègre un snapshot complet du `Plateau`, avec deep-copies optimisées.  
  👉 Grâce à ses **Copy Constructors**, le système garantit une duplication massive d'instances non-bloquante du jeu lors de la descente de l'arbre heuristique, évitant de corrompre le déroulement graphique du vrai plateau pendant la "réflexion" de l'IA (en asynchrone via des Threads GUI sécurisés).
- **ZobristHasher** : Tableau de hachage 3D de 64 bits pré-généré aléatoirement. Permet à nos noeuds de calculer l'identifiant (empreinte) exact d'un plateau juste en effectuant une instruction binaire `XOR` bit-à-bit extrêmement peu coûteuse en CPU.
- **TranspositionTable (Cache de jeu)** : Mémorise dynamiquement via hash les sous-arbres déjà visités. Si, dans nos permutations Minimax, le jeu revient à un état identique, nous évitons les recalculs inutiles de centaines de noeuds (`EXACT`, `LOWER` ou `UPPER` bounds), garantissant des réponses éclair d'Alpha-Bêta de niveau 4 ou +.

## 3. Description des principales fonctions
Les règles métiers de l'espace ludique Quoridorien sont extraites des modèles :
- `PathService.hasPath()` : Implémente un algorithme de traversée dynamique `BFS (Breadth-First Search)`. Il a pour mandat de certifier à 100% que la pose d'une barrière sur la grille n'enferme pas un joueur. Il stoppe instantanément la boucle d'actions si son graphe simulé ne trouve plus de chemin (`true/false`).
- `RegleService.getMouvementsValides()` : Effectue les contrôles de collisions et gère les fameuses "règles d'exceptions" : si un mur protège l'adversaire ou bloque l'horizon, le pion obtiendra la capacité vectorielle de bifurquer exceptionnellement en saut ou diagonale valide.

## 4. Stratégie gagnante et Fonction d'Évaluation
La théorie des jeux prouve que Quoridor détient un espace de complexité phénoménal (plus vaste ou similaire au jeu des échecs), bloquant la formalisation d'une stratégie gagnante unique.
La stratégie codée repose alors sur "l'Allongement du Pathfinding adversaire".

**Algorithmes de Décision** :
- `AlphaBetaService` et `NegAlphaBetaService` (Négamax) simulent des stratégies antagonistes Minimax en ne retenant que la voie alpha-bêta pertinente.
- Notre `HeuristiqueService.evaluerState(...)` simule constamment le plus court chemin de retour :  
  Équation finale du Score Heuristique d'un noeud = `Distance_Adversaire - Distance_IA + (Barrières_IA - Barrières_Adv)`  
  Si l'ordinateur remarque que son pion (IA) est à une `k` distance minimale, elle avancera. Si sa distance devient trop importante relativement à l'humain, elle optera pour injecter une barrière stratégique sur l'ordinateur adverse et remonter son ratio de victoire d'arbre !
