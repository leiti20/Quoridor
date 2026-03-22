package com.quoridor.service;

import com.quoridor.model.Barriere;
import com.quoridor.model.Joueur;
import com.quoridor.model.Plateau;
import com.quoridor.util.Constantes;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class PathService {

    /**
     * Vérifie si le joueur peut atteindre sa ligne de but moyennant les barrières actuelles.
     * Utilise un BFS pour garantir qu'un chemin existe.
     * @param plateau Le plateau actuel
     * @param joueur Le joueur à évaluer
     * @param tmpRow position courante ou simulée
     * @param tmpCol position courante ou simulée
     * @return true si une case de la ligne de but est atteignable, false sinon.
     */
    public boolean hasPath(Plateau plateau, Joueur joueur, int tmpRow, int tmpCol) {
        boolean[][] visited = new boolean[Constantes.TAILLE_PLATEAU][Constantes.TAILLE_PLATEAU];
        Queue<int[]> queue = new LinkedList<>();
        
        queue.add(new int[]{tmpRow, tmpCol});
        visited[tmpRow][tmpCol] = true;
        
        int goalRow = joueur.getLigneDeBut();
        
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            
            if (r == goalRow) return true;
            
            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];
                
                if (nr >= 0 && nr < Constantes.TAILLE_PLATEAU && nc >= 0 && nc < Constantes.TAILLE_PLATEAU) {
                    if (!visited[nr][nc] && !isBlockedByBarrier(plateau, r, c, nr, nc)) {
                        visited[nr][nc] = true;
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Détermine si le passage entre deux cases adjacentes (r1, c1) et (r2, c2) est bloqué par une barrière.
     */
    public boolean isBlockedByBarrier(Plateau plateau, int r1, int c1, int r2, int c2) {
        for (Barriere b : plateau.getBarrieres()) {
            int br = b.getRow();
            int bc = b.getCol();
            
            if (b.getOrientation() == Barriere.Orientation.HORIZONTAL) {
                if (Math.min(r1, r2) == br && Math.max(r1, r2) == br + 1) {
                    if (c1 == bc || c1 == bc + 1) return true;
                }
            } else { // VERTICAL
                if (Math.min(c1, c2) == bc && Math.max(c1, c2) == bc + 1) {
                    if (r1 == br || r1 == br + 1) return true;
                }
            }
        }
        return false;
    }
}
