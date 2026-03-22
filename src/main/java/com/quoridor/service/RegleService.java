package com.quoridor.service;

import com.quoridor.model.Barriere;
import com.quoridor.model.EtatJeu;
import com.quoridor.model.Joueur;
import com.quoridor.model.Plateau;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegleService {

    private final PathService pathService;

    public RegleService(PathService pathService) {
        this.pathService = pathService;
    }

    /**
     * Retourne tous les mouvements valides d'un joueur, y compris les sauts par-dessus l'adversaire.
     */
    public List<int[]> getMouvementsValides(EtatJeu etat) {
        List<int[]> validMoves = new ArrayList<>();
        Joueur current = etat.getJoueurCourant();
        Joueur adv = etat.getAdversaire();
        Plateau plateau = etat.getPlateau();
        
        int r = current.getPion().getRow();
        int c = current.getPion().getCol();
        
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];
            
            if (plateau.estDansPlateau(nr, nc)) {
                if (!pathService.isBlockedByBarrier(plateau, r, c, nr, nc)) {
                    
                    if (adv.getPion().getRow() == nr && adv.getPion().getCol() == nc) {
                        // Saut droit
                        int nnr = nr + d[0];
                        int nnc = nc + d[1];
                        if (plateau.estDansPlateau(nnr, nnc) && !pathService.isBlockedByBarrier(plateau, nr, nc, nnr, nnc)) {
                            validMoves.add(new int[]{nnr, nnc});
                        } else {
                            // Saut diagonal (bifurcation si barrière derrière le pion sauté ou bord du plateau)
                            int[][] diagDirs = (d[0] == 0) ? new int[][]{{-1, 0}, {1, 0}} : new int[][]{{0, -1}, {0, 1}};
                            for (int[] dd : diagDirs) {
                                int dr = nr + dd[0];
                                int dc = nc + dd[1];
                                if (plateau.estDansPlateau(dr, dc) && !pathService.isBlockedByBarrier(plateau, nr, nc, dr, dc)) {
                                    validMoves.add(new int[]{dr, dc});
                                }
                            }
                        }
                    } else {
                        validMoves.add(new int[]{nr, nc});
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * Valide de manière stricte le placement d'une barrière (chevauchement + fermeture de chemin).
     */
    public boolean validerPlacementBarriere(EtatJeu etat, Barriere barriere) {
        Plateau plateau = etat.getPlateau();
        int br = barriere.getRow();
        int bc = barriere.getCol();
        
        if (!plateau.estBarriereDansLimites(br, bc)) return false;
        
        for (Barriere b : plateau.getBarrieres()) {
            if (b.getRow() == br && b.getCol() == bc) return false;
            if (barriere.getOrientation() == Barriere.Orientation.HORIZONTAL && b.getOrientation() == Barriere.Orientation.HORIZONTAL) {
                if (b.getRow() == br && Math.abs(b.getCol() - bc) == 1) return false;
            }
            if (barriere.getOrientation() == Barriere.Orientation.VERTICAL && b.getOrientation() == Barriere.Orientation.VERTICAL) {
                if (b.getCol() == bc && Math.abs(b.getRow() - br) == 1) return false;
            }
        }
        
        Plateau tempPlateau = new Plateau(plateau);
        tempPlateau.ajouteBarriere(barriere);
        
        for (Joueur j : etat.getJoueurs()) {
            if (!pathService.hasPath(tempPlateau, j, j.getPion().getRow(), j.getPion().getCol())) {
                return false;
            }
        }
        
        return true;
    }
}
