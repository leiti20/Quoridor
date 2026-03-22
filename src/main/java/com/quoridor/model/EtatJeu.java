package com.quoridor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente l'état complet de la partie à un instant t.
 */
public class EtatJeu {
    private Plateau plateau;
    private List<Joueur> joueurs;
    private int indexJoueurCourant;
    private List<String> historiqueCoups;

    /**
     * Construit un état initial.
     * @param joueurs la liste des 2 joueurs (doivent avoir leurs pions positionnés)
     */
    public EtatJeu(List<Joueur> joueurs) {
        this.plateau = new Plateau();
        this.joueurs = joueurs;
        this.indexJoueurCourant = 0; // Joueur 1 commence
        this.historiqueCoups = new ArrayList<>();
    }
    
    /**
     * Constructeur de copie (Deep copy) nécessaire pour la simulation de l'IA.
     * @param other l'état à copier
     */
    public EtatJeu(EtatJeu other) {
        this.plateau = new Plateau(other.plateau);
        this.joueurs = new ArrayList<>();
        for (Joueur j : other.joueurs) {
            this.joueurs.add(new Joueur(j));
        }
        this.indexJoueurCourant = other.indexJoueurCourant;
        this.historiqueCoups = new ArrayList<>(other.historiqueCoups);
    }

    public Plateau getPlateau() { return plateau; }
    
    public List<Joueur> getJoueurs() { return joueurs; }
    
    public Joueur getJoueurCourant() { return joueurs.get(indexJoueurCourant); }
    
    public Joueur getAdversaire() { return joueurs.get(1 - indexJoueurCourant); }
    
    public int getIndexJoueurCourant() { return indexJoueurCourant; }
    
    public void passerTour() {
        this.indexJoueurCourant = 1 - this.indexJoueurCourant;
    }
    
    public List<String> getHistoriqueCoups() { return historiqueCoups; }
    
    public void ajouterCoupAHistorique(String coup) {
        historiqueCoups.add(coup);
    }
    
    /**
     * Vérifie si un joueur a atteint sa ligne de but.
     */
    public boolean estTermine() {
        return getJoueurs().stream().anyMatch(j -> j.getPion().getRow() == j.getLigneDeBut());
    }
    
    public Joueur getGagnant() {
        for (Joueur j : joueurs) {
            if (j.getPion().getRow() == j.getLigneDeBut()) {
                return j;
            }
        }
        return null;
    }
}
