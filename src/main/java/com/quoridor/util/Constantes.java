package com.quoridor.util;

/**
 * Constantes globales pour le jeu Quoridor
 */
public final class Constantes {

    private Constantes() {
        // Constructeur privé pour empêcher l'instanciation
    }

    public static final int TAILLE_PLATEAU = 9;
    public static final int NOMBRE_BARRIERES_INITIAL = 10;
    
    // Coordonnées de départ du Joueur 1 (en bas)
    public static final int LIGNE_DEPART_J1 = 8;
    public static final int COLONNE_DEPART_J1 = 4;
    public static final int LIGNE_BUT_J1 = 0;

    // Coordonnées de départ du Joueur 2 (en haut)
    public static final int LIGNE_DEPART_J2 = 0;
    public static final int COLONNE_DEPART_J2 = 4;
    public static final int LIGNE_BUT_J2 = 8;

    // Interface Graphique
    public static final int LARGEUR_FENETRE = 720;
    public static final int HAUTEUR_FENETRE = 640;
    public static final int TAILLE_CASE = 45;
}
