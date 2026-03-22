package com.quoridor.ai;

import com.quoridor.model.EtatJeu;
import com.quoridor.model.Joueur;
import com.quoridor.model.Plateau;
import com.quoridor.service.PathService;
import com.quoridor.util.Constantes;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class HeuristiqueService {

    private final PathService pathService;

    public HeuristiqueService(PathService pathService) {
        this.pathService = pathService;
    }

    /**
     * Evalue le plateau avec la méthode de Dijkstra/BFS.
     * Score = Dist(Adversaire) - Dist(IA) + Delta Barrières
     */
    public int evaluerState(EtatJeu etat, int iaId) {
        Joueur ia = etat.getJoueurs().stream().filter(j -> j.getId() == iaId).findFirst().orElse(null);
        Joueur adv = etat.getJoueurs().stream().filter(j -> j.getId() != iaId).findFirst().orElse(null);

        if (ia == null || adv == null) return 0;
        
        if (ia.getPion().getRow() == ia.getLigneDeBut()) return 10000;
        if (adv.getPion().getRow() == adv.getLigneDeBut()) return -10000;

        int distIa = getSortestPathLength(etat.getPlateau(), ia);
        int distAdv = getSortestPathLength(etat.getPlateau(), adv);

        int score = distAdv - distIa;
        score += (ia.getBarrieresRestantes() - adv.getBarrieresRestantes());
        
        return score;
    }

    private int getSortestPathLength(Plateau plateau, Joueur joueur) {
        int[][] dist = new int[Constantes.TAILLE_PLATEAU][Constantes.TAILLE_PLATEAU];
        for (int i=0; i<Constantes.TAILLE_PLATEAU; i++) {
            for (int j=0; j<Constantes.TAILLE_PLATEAU; j++) {
                dist[i][j] = -1;
            }
        }
        
        Queue<int[]> queue = new LinkedList<>();
        int sr = joueur.getPion().getRow();
        int sc = joueur.getPion().getCol();
        queue.add(new int[]{sr, sc});
        dist[sr][sc] = 0;
        
        int goalRow = joueur.getLigneDeBut();
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            
            if (r == goalRow) return dist[r][c];
            
            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];
                
                if (nr >= 0 && nr < Constantes.TAILLE_PLATEAU && nc >= 0 && nc < Constantes.TAILLE_PLATEAU) {
                    if (dist[nr][nc] == -1 && !pathService.isBlockedByBarrier(plateau, r, c, nr, nc)) {
                        dist[nr][nc] = dist[r][c] + 1;
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }
        return 999;
    }
}
