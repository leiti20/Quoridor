package com.quoridor.service;

import com.quoridor.model.Barriere;
import com.quoridor.model.Joueur;
import com.quoridor.model.Pion;
import com.quoridor.model.Plateau;
import com.quoridor.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PathServiceTest {

    private PathService pathService;
    private Plateau plateau;
    private Joueur j1;

    @BeforeEach
    void setUp() {
        pathService = new PathService();
        plateau = new Plateau();
        j1 = new Joueur(1, "J1", Joueur.Type.HUMAIN, Constantes.LIGNE_BUT_J1);
        j1.setPion(new Pion(8, 4));
    }

    @Test
    void testHasPath_InitialState_ShouldBeTrue() {
        assertTrue(pathService.hasPath(plateau, j1, j1.getPion().getRow(), j1.getPion().getCol()));
    }

    @Test
    void testIsBlockedByBarrier_ShouldDetectBlock() {
        plateau.ajouteBarriere(new Barriere(7, 4, Barriere.Orientation.HORIZONTAL));
        // Devrait bloquer le passage de (8,4) vers (7,4)
        // et de (8,5) vers (7,5)
        assertTrue(pathService.isBlockedByBarrier(plateau, 8, 4, 7, 4));
        assertFalse(pathService.isBlockedByBarrier(plateau, 8, 4, 8, 5)); // mouvement horizontal non bloqué
    }
}
