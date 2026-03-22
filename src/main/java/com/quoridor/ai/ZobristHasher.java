package com.quoridor.ai;

import com.quoridor.model.Barriere;
import com.quoridor.model.EtatJeu;
import com.quoridor.model.Joueur;
import com.quoridor.util.Constantes;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Calcul rapide d'un identifiant unique (hash) pour un plateau de jeu à l'aide de
 * la méthode de Zobrist.
 */
@Component
public class ZobristHasher {

    private long[][][] pionZobrist;
    private long[][][] barriereZobrist;
    private long tourZobrist;

    public ZobristHasher() {
        Random rand = new Random(42); 
        pionZobrist = new long[2][Constantes.TAILLE_PLATEAU][Constantes.TAILLE_PLATEAU];
        for (int i=0; i<2; i++) {
            for (int r=0; r<Constantes.TAILLE_PLATEAU; r++) {
                for (int c=0; c<Constantes.TAILLE_PLATEAU; c++) {
                    pionZobrist[i][r][c] = rand.nextLong();
                }
            }
        }
        
        barriereZobrist = new long[2][Constantes.TAILLE_PLATEAU-1][Constantes.TAILLE_PLATEAU-1];
        for (int o=0; o<2; o++) {
            for (int r=0; r<Constantes.TAILLE_PLATEAU-1; r++) {
                for (int c=0; c<Constantes.TAILLE_PLATEAU-1; c++) {
                    barriereZobrist[o][r][c] = rand.nextLong();
                }
            }
        }
        
        tourZobrist = rand.nextLong();
    }

    public long hash(EtatJeu etat) {
        long h = 0;
        
        for (int i=0; i<etat.getJoueurs().size(); i++) {
            Joueur j = etat.getJoueurs().get(i);
            h ^= pionZobrist[i][j.getPion().getRow()][j.getPion().getCol()];
        }
        
        for (Barriere b : etat.getPlateau().getBarrieres()) {
            int o = b.getOrientation() == Barriere.Orientation.HORIZONTAL ? 0 : 1;
            h ^= barriereZobrist[o][b.getRow()][b.getCol()];
        }
        
        if (etat.getIndexJoueurCourant() == 1) {
            h ^= tourZobrist;
        }
        
        return h;
    }
}
