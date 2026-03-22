package com.quoridor.model;

import com.quoridor.util.Constantes;

/**
 * Représente un joueur de Quoridor.
 */
public class Joueur {
    public enum Type {
        HUMAIN, IA
    }

    private int id;
    private String nom;
    private Type type;
    private int barrieresRestantes;
    private Pion pion;
    private int ligneDeBut;

    /**
     * Constructeur d'un joueur.
     * @param id identifiant unique (1 ou 2)
     * @param nom nom du joueur
     * @param type HUMAIN ou IA
     * @param ligneDeBut la ligne (0 ou 8) que le joueur doit atteindre
     */
    public Joueur(int id, String nom, Type type, int ligneDeBut) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.ligneDeBut = ligneDeBut;
        this.barrieresRestantes = Constantes.NOMBRE_BARRIERES_INITIAL;
    }
    
    /**
     * Constructeur de copie pour la simulation de l'IA (Deep copy).
     */
    public Joueur(Joueur other) {
        this.id = other.id;
        this.nom = other.nom;
        this.type = other.type;
        this.barrieresRestantes = other.barrieresRestantes;
        this.ligneDeBut = other.ligneDeBut;
        if (other.pion != null) {
            this.pion = new Pion(other.pion);
            this.pion.setJoueur(this);
        }
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public Type getType() { return type; }
    
    public int getBarrieresRestantes() { return barrieresRestantes; }
    public void decrementeBarrieres() { this.barrieresRestantes--; }
    public void setBarrieresRestantes(int barrieresRestantes) { this.barrieresRestantes = barrieresRestantes; }

    public Pion getPion() { return pion; }
    public void setPion(Pion pion) { 
        this.pion = pion; 
        if(pion != null) {
            pion.setJoueur(this);
        }
    }

    public int getLigneDeBut() { return ligneDeBut; }
    
    @Override
    public String toString() {
        return nom + " (Barrières: " + barrieresRestantes + ")";
    }
}
