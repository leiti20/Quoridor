package com.quoridor.ai;

import com.quoridor.model.*;
import com.quoridor.util.Constantes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ZobristHasherTest {

    @Test
    void testHashDeterministic() {
        ZobristHasher hasher = new ZobristHasher();
        
        Joueur j1 = new Joueur(1, "1", Joueur.Type.HUMAIN, Constantes.LIGNE_BUT_J1);
        j1.setPion(new Pion(8,4));
        Joueur j2 = new Joueur(2, "2", Joueur.Type.IA, Constantes.LIGNE_BUT_J2);
        j2.setPion(new Pion(0,4));
        
        EtatJeu etat = new EtatJeu(List.of(j1, j2));
        
        long h1 = hasher.hash(etat);
        long h2 = hasher.hash(etat);
        
        assertEquals(h1, h2);
    }
}
