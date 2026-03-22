package com.quoridor.ai;

import com.quoridor.model.Barriere;
import com.quoridor.model.EtatJeu;
import com.quoridor.service.GameService;
import com.quoridor.service.RegleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlphaBetaService implements AIService {

    private final GameService gameService;
    private final RegleService regleService;
    private final HeuristiqueService heuristiqueService;
    private final ZobristHasher hasher;
    private final TranspositionTable table;

    public AlphaBetaService(GameService gameService, RegleService regleService, 
                            HeuristiqueService heuristiqueService, ZobristHasher hasher, 
                            TranspositionTable table) {
        this.gameService = gameService;
        this.regleService = regleService;
        this.heuristiqueService = heuristiqueService;
        this.hasher = hasher;
        this.table = table;
    }

    @Override
    public void jouerCoup(EtatJeu etatInitial, int depth) {
        int bestScore = Integer.MIN_VALUE;
        Object bestMove = null;
        int idIa = etatInitial.getJoueurCourant().getId();

        EtatJeu clone = new EtatJeu(etatInitial);

        // Mouvements de pion
        List<int[]> moves = regleService.getMouvementsValides(clone);
        for (int[] m : moves) {
            EtatJeu nextState = new EtatJeu(clone);
            gameService.movePion(nextState, m[0], m[1]);
            int score = alphaBeta(nextState, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, idIa);
            if (score > bestScore || bestMove == null) {
                bestScore = score;
                bestMove = m;
            }
        }

        // Placement de barrières
        if (clone.getJoueurCourant().getBarrieresRestantes() > 0) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    for (Barriere.Orientation o : Barriere.Orientation.values()) {
                        Barriere b = new Barriere(r, c, o);
                        if (regleService.validerPlacementBarriere(clone, b)) {
                            EtatJeu nextState = new EtatJeu(clone);
                            gameService.placeBarriere(nextState, b);
                            int score = alphaBeta(nextState, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, idIa);
                            if (score > bestScore) {
                                bestScore = score;
                                bestMove = b;
                            }
                        }
                    }
                }
            }
        }

        // Appliquer le move choisi à l'état réel
        if (bestMove instanceof int[]) {
            int[] m = (int[]) bestMove;
            gameService.movePion(etatInitial, m[0], m[1]);
        } else if (bestMove instanceof Barriere) {
            gameService.placeBarriere(etatInitial, (Barriere) bestMove);
        }
    }

    private int alphaBeta(EtatJeu etat, int depth, int alpha, int beta, boolean isMaximizingPlayer, int idIa) {
        if (depth == 0 || etat.estTermine()) {
            return heuristiqueService.evaluerState(etat, idIa);
        }

        long hash = hasher.hash(etat);
        TranspositionTable.Entry entry = table.get(hash);
        if (entry != null && entry.depth >= depth) {
            if (entry.type == TranspositionTable.NodeType.EXACT) return entry.value;
            if (entry.type == TranspositionTable.NodeType.LOWER && entry.value > alpha) alpha = entry.value;
            if (entry.type == TranspositionTable.NodeType.UPPER && entry.value < beta) beta = entry.value;
            if (alpha >= beta) return entry.value;
        }

        int originalAlpha = alpha;
        int bestScore;

        if (isMaximizingPlayer) {
            bestScore = Integer.MIN_VALUE;
            // Pions
            for (int[] m : regleService.getMouvementsValides(etat)) {
                EtatJeu nextState = new EtatJeu(etat);
                gameService.movePion(nextState, m[0], m[1]);
                bestScore = Math.max(bestScore, alphaBeta(nextState, depth - 1, alpha, beta, false, idIa));
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) break;
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int[] m : regleService.getMouvementsValides(etat)) {
                EtatJeu nextState = new EtatJeu(etat);
                gameService.movePion(nextState, m[0], m[1]);
                bestScore = Math.min(bestScore, alphaBeta(nextState, depth - 1, alpha, beta, true, idIa));
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) break;
            }
        }

        TranspositionTable.NodeType type = TranspositionTable.NodeType.EXACT;
        if (bestScore <= originalAlpha) type = TranspositionTable.NodeType.UPPER;
        else if (bestScore >= beta) type = TranspositionTable.NodeType.LOWER;

        table.put(hash, bestScore, depth, type);
        return bestScore;
    }
}
