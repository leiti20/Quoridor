package com.quoridor.service;

import com.quoridor.model.*;
import com.quoridor.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegleServiceTest {

    private RegleService regleService;
    private EtatJeu etat;

    @BeforeEach
    void setUp() {
        PathService pathService = new PathService();
        regleService = new RegleService(pathService);

        Joueur j1 = new Joueur(1, "J1", Joueur.Type.HUMAIN, Constantes.LIGNE_BUT_J1);
        j1.setPion(new Pion(8, 4));
        
        Joueur j2 = new Joueur(2, "J2", Joueur.Type.HUMAIN, Constantes.LIGNE_BUT_J2);
        j2.setPion(new Pion(0, 4));

        etat = new EtatJeu(List.of(j1, j2));
    }

    @Test
    void testMouvementValides_InitialStated() {
        List<int[]> moves = regleService.getMouvementsValides(etat);
        assertEquals(3, moves.size()); // Haut, Gauche, Droite (Pas en bas car row 8)
    }

    @Test
    void testPlacementBarriereInvalide_HorsLimites() {
        Barriere b = new Barriere(8, 4, Barriere.Orientation.HORIZONTAL); // Ligne 8 invalide (0 à 7)
        assertFalse(regleService.validerPlacementBarriere(etat, b));
    }
}
