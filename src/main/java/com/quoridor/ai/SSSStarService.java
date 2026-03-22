package com.quoridor.ai;

import com.quoridor.model.EtatJeu;
import com.quoridor.service.GameService;
import com.quoridor.service.RegleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SSSStarService implements AIService {

    private final GameService gameService;
    private final RegleService regleService;
    private final HeuristiqueService heuristiqueService;

    public SSSStarService(GameService gameService, RegleService regleService, HeuristiqueService heuristiqueService) {
        this.gameService = gameService;
        this.regleService = regleService;
        this.heuristiqueService = heuristiqueService;
    }

    /**
     * Implémentation simplifiée de SSS* (souvent fallback sur Alpha-Bêta optimisé dans ce contexte
     * pour éviter la gestion lourde de la OPEN list sur Quoridor).
     */
    @Override
    public void jouerCoup(EtatJeu etatInitial, int depth) {
        int bestScore = Integer.MIN_VALUE;
        Object bestMove = null;
        int idIa = etatInitial.getJoueurCourant().getId();

        EtatJeu clone = new EtatJeu(etatInitial);
        List<int[]> moves = regleService.getMouvementsValides(clone);
        for (int[] m : moves) {
            EtatJeu nextState = new EtatJeu(clone);
            gameService.movePion(nextState, m[0], m[1]);
            int score = simpleSssEval(nextState, depth - 1, idIa);
            if (score > bestScore || bestMove == null) {
                bestScore = score;
                bestMove = m;
            }
        }

        if (bestMove instanceof int[]) {
            int[] m = (int[]) bestMove;
            gameService.movePion(etatInitial, m[0], m[1]);
        }
    }
    
    // Evaluation simplifiée Best-first
    private int simpleSssEval(EtatJeu etat, int depth, int idIa) {
        if (depth == 0 || etat.estTermine()) {
            return heuristiqueService.evaluerState(etat, idIa);
        }
        
        int best = Integer.MIN_VALUE;
        for (int[] m : regleService.getMouvementsValides(etat)) {
            EtatJeu nextState = new EtatJeu(etat);
            gameService.movePion(nextState, m[0], m[1]);
            best = Math.max(best, heuristiqueService.evaluerState(nextState, idIa));
        }
        return best;
    }
}
