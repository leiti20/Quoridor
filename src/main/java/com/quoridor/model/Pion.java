package com.quoridor.model;

import java.util.Objects;

/**
 * Représente un pion sur le plateau de jeu.
 */
public class Pion {
    private int row;
    private int col;
    private Joueur joueur;

    /**
     * Constructeur pour initialiser un pion.
     * @param row la ligne (0 à 8)
     * @param col la colonne (0 à 8)
     */
    public Pion(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /**
     * Constructeur de copie (Deep copy pour l'IA).
     * @param other le pion à copier
     */
    public Pion(Pion other) {
        this.row = other.row;
        this.col = other.col;
    }

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    
    public int getCol() { return col; }
    public void setCol(int col) { this.col = col; }

    public Joueur getJoueur() { return joueur; }
    public void setJoueur(Joueur joueur) { this.joueur = joueur; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pion pion = (Pion) o;
        return row == pion.row && col == pion.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
    @Override
    public String toString() {
        return "Pion{" + "row=" + row + ", col=" + col + '}';
    }
}
