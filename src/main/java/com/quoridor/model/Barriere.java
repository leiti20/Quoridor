package com.quoridor.model;

import java.util.Objects;

/**
 * Représente une barrière placée sur le plateau.
 * Une barrière a une longueur de 2 cases et une orientation.
 */
public class Barriere {
    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    // Coordonnées du coin supérieur gauche de la barrière
    private int row;
    private int col;
    private Orientation orientation;

    /**
     * Constructeur d'une barrière.
     * @param row la ligne (0 à 7)
     * @param col la colonne (0 à 7)
     * @param orientation HORIZONTAL ou VERTICAL
     */
    public Barriere(int row, int col, Orientation orientation) {
        this.row = row;
        this.col = col;
        this.orientation = orientation;
    }
    
    /**
     * Constructeur de copie.
     */
    public Barriere(Barriere other) {
        this.row = other.row;
        this.col = other.col;
        this.orientation = other.orientation;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Orientation getOrientation() { return orientation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Barriere barriere = (Barriere) o;
        return row == barriere.row && col == barriere.col && orientation == barriere.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, orientation);
    }
    
    @Override
    public String toString() {
        return "Barriere{" + "row=" + row + ", col=" + col + ", orientation=" + orientation + '}';
    }
}
