package com.quoridor.service;

import com.quoridor.model.Barriere;
import com.quoridor.model.EtatJeu;
import com.quoridor.model.Joueur;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final RegleService regleService;

    public GameService(RegleService regleService) {
        this.regleService = regleService;
    }

    /**
     * Déplace le pion courant si le mouvement est valide, puis passe au tour du joueur suivant.
     * @return true si le mouvement a été réalisé avec succès
     */
    public boolean movePion(EtatJeu etat, int r, int c) {
        if (etat.estTermine()) return false;
        
        List<int[]> validMoves = regleService.getMouvementsValides(etat);
        for (int[] m : validMoves) {
            if (m[0] == r && m[1] == c) {
                Joueur courrant = etat.getJoueurCourant();
                courrant.getPion().setRow(r);
                courrant.getPion().setCol(c);
                etat.ajouterCoupAHistorique(courrant.getNom() + " -> Déplacement en (" + r + "," + c + ")");
                
                if (!etat.estTermine()) {
                    etat.passerTour();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Place une barrière sur le plateau si elle est valide, puis passe au tour suivant.
     */
    public boolean placeBarriere(EtatJeu etat, Barriere barriere) {
        if (etat.estTermine()) return false;
        
        Joueur courrant = etat.getJoueurCourant();
        if (courrant.getBarrieresRestantes() <= 0) return false;

        if (regleService.validerPlacementBarriere(etat, barriere)) {
            etat.getPlateau().ajouteBarriere(barriere);
            courrant.decrementeBarrieres();
            etat.ajouterCoupAHistorique(courrant.getNom() + " -> Placement " + barriere);
            etat.passerTour();
            return true;
        }
        return false;
    }
}
