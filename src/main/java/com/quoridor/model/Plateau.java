package com.quoridor.model;

import com.quoridor.util.Constantes;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente la grille 9x9 et l'ensemble des barrières posées.
 */
public class Plateau {
    
    private List<Barriere> barrieres;

    /**
     * Initialise un plateau vide.
     */
    public Plateau() {
        this.barrieres = new ArrayList<>();
    }
    
    /**
     * Constructeur de copie (Deep copy).
     */
    public Plateau(Plateau other) {
        this.barrieres = new ArrayList<>();
        for (Barriere b : other.barrieres) {
            this.barrieres.add(new Barriere(b));
        }
    }

    public List<Barriere> getBarrieres() {
        return barrieres;
    }

    /**
     * Ajoute une barrière sur le plateau.
     * @param barriere la barrière à ajouter
     */
    public void ajouteBarriere(Barriere barriere) {
        this.barrieres.add(barriere);
    }
    
    /**
     * Vérifie si une coordonnée est dans les limites du plateau (cases).
     */
    public boolean estDansPlateau(int row, int col) {
        return row >= 0 && row < Constantes.TAILLE_PLATEAU && col >= 0 && col < Constantes.TAILLE_PLATEAU;
    }
    
    /**
     * Vérifie si les coordonnées de la barrière sont valides.
     * Une barrière à (row, col) bloque entre (row,col)-(row,col+1) si H,
     * et elle fait 2 cases de long, donc elle ne peut pas être à row=8 ou col=8.
     */
    public boolean estBarriereDansLimites(int row, int col) {
        return row >= 0 && row < Constantes.TAILLE_PLATEAU - 1 && col >= 0 && col < Constantes.TAILLE_PLATEAU - 1;
    }
}
