package com.quoridor.ai;

import com.quoridor.model.EtatJeu;

public interface AIService {
    /**
     * Choisit et exécute le meilleur coup pour l'IA dans la partie.
     */
    void jouerCoup(EtatJeu etat, int depth);
}
