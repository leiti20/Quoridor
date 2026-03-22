package com.quoridor.ai;

import com.quoridor.model.*;
import com.quoridor.service.GameService;
import com.quoridor.service.PathService;
import com.quoridor.service.RegleService;
import com.quoridor.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlphaBetaServiceTest {

    private AlphaBetaService alphaBetaService;
    private EtatJeu etat;

    @BeforeEach
    void setUp() {
        PathService pathService = new PathService();
        RegleService regleService = new RegleService(pathService);
        GameService gameService = new GameService(regleService);
        HeuristiqueService heuristiqueService = new HeuristiqueService(pathService);
        ZobristHasher hasher = new ZobristHasher();
        TranspositionTable table = new TranspositionTable();
        
        alphaBetaService = new AlphaBetaService(gameService, regleService, heuristiqueService, hasher, table);
        
        Joueur j1 = new Joueur(1, "J1", Joueur.Type.IA, Constantes.LIGNE_BUT_J1);
        j1.setPion(new Pion(8, 4));
        
        Joueur j2 = new Joueur(2, "J2", Joueur.Type.HUMAIN, Constantes.LIGNE_BUT_J2);
        j2.setPion(new Pion(0, 4));
        
        etat = new EtatJeu(List.of(j1, j2));
    }

    @Test
    void testJouerCoup_MinimaxAvance() {
        alphaBetaService.jouerCoup(etat, 2);
        
        assertEquals(1, etat.getIndexJoueurCourant()); // Le tour passe au J2
        int newRow = etat.getJoueurs().get(0).getPion().getRow();
        
        // L'IA est au row 8, son but est au row 0. L'heuristique valorise la diminution de la distance.
        // Ceteris paribus, elle devrait avancer toutes droites (row 7).
        assertEquals(7, newRow, "Le pion IA devrait s'avancer vers sa ligne de but.");
    }
}
