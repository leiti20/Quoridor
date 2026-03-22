package com.quoridor.ai;

import com.quoridor.model.Barriere;
import com.quoridor.model.EtatJeu;
import com.quoridor.service.GameService;
import com.quoridor.service.RegleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NegAlphaBetaService implements AIService {

    private final GameService gameService;
    private final RegleService regleService;
    private final HeuristiqueService heuristiqueService;

    public NegAlphaBetaService(GameService gameService, RegleService regleService, HeuristiqueService heuristiqueService) {
        this.gameService = gameService;
        this.regleService = regleService;
        this.heuristiqueService = heuristiqueService;
    }

    @Override
    public void jouerCoup(EtatJeu etatInitial, int depth) {
        int bestScore = Integer.MIN_VALUE;
        Object bestMove = null;
        int color = 1;

        EtatJeu clone = new EtatJeu(etatInitial);

        List<int[]> moves = regleService.getMouvementsValides(clone);
        for (int[] m : moves) {
            EtatJeu nextState = new EtatJeu(clone);
            gameService.movePion(nextState, m[0], m[1]);
            int score = -negamax(nextState, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, -color);
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

    private int negamax(EtatJeu etat, int depth, int alpha, int beta, int color) {
        if (depth == 0 || etat.estTermine()) {
            return color * heuristiqueService.evaluerState(etat, etat.getJoueurCourant().getId());
        }

        int bestValue = Integer.MIN_VALUE;
        for (int[] m : regleService.getMouvementsValides(etat)) {
            EtatJeu nextState = new EtatJeu(etat);
            gameService.movePion(nextState, m[0], m[1]);
            int val = -negamax(nextState, depth - 1, -beta, -alpha, -color);
            bestValue = Math.max(bestValue, val);
            alpha = Math.max(alpha, val);
            if (alpha >= beta) break;
        }

        return bestValue;
    }
}
