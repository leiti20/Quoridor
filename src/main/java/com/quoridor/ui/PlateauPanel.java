package com.quoridor.ui;

import com.quoridor.util.Constantes;
import com.quoridor.model.*;
import com.quoridor.service.GameService;
import com.quoridor.service.RegleService;
import com.quoridor.ai.AlphaBetaService;
import com.quoridor.ai.NegAlphaBetaService;
import com.quoridor.ai.SSSStarService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

@Component
public class PlateauPanel extends JPanel {

    private EtatJeu etatJeu;
    private final GameService gameService;
    private final InfoPanel infoPanel;
    private final RegleService regleService;
    
    private final AlphaBetaService alphaBetaService;
    private final NegAlphaBetaService negAlphaBetaService;
    private final SSSStarService sssStarService;
    
    private int hoverRow = -1;
    private int hoverCol = -1;
    private Barriere.Orientation hoverOrientation = Barriere.Orientation.HORIZONTAL;
    
    private MainFrame mainFrame;
    private final java.util.Stack<EtatJeu> undoStack = new java.util.Stack<>();
    
    private String gameMode = "Humain vs IA";
    private String iaAlgo = "Alpha-Bêta";
    private int currentDepth = 3;

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    private int[] getBoardOffsets() {
        int size = Constantes.TAILLE_CASE;
        int gap = 10;
        int boardSize = 9 * size + 8 * gap;
        int maxW = getWidth() > 0 ? getWidth() : Constantes.LARGEUR_FENETRE - 250;
        int maxH = getHeight() > 0 ? getHeight() : Constantes.HAUTEUR_FENETRE - 60;
        
        int offsetX = Math.max((maxW - boardSize) / 2, 20);
        int offsetY = Math.max((maxH - boardSize) / 2, 20);
        return new int[]{offsetX, offsetY};
    }
    
    public void undo() {
        if (!undoStack.isEmpty()) {
            this.etatJeu = undoStack.pop();
            this.hoverRow = -1; 
            this.hoverCol = -1;
            repaint();
            Joueur j1 = etatJeu.getJoueurs().get(0);
            Joueur j2 = etatJeu.getJoueurs().get(1);
            infoPanel.updateInfo(etatJeu.getJoueurCourant().getNom(), j1.getBarrieresRestantes(), j2.getBarrieresRestantes());
        }
    }
    
    public void startNewGame(String mode, String algo, int depth) {
        this.gameMode = mode;
        this.iaAlgo = algo;
        this.currentDepth = depth;
        
        Joueur.Type typeJ1 = Joueur.Type.HUMAIN;
        Joueur.Type typeJ2 = Joueur.Type.HUMAIN;
        
        if ("Humain vs IA".equals(mode)) {
            typeJ2 = Joueur.Type.IA;
        } else if ("IA vs IA".equals(mode)) {
            typeJ1 = Joueur.Type.IA;
            typeJ2 = Joueur.Type.IA;
        }
        
        Joueur j1 = new Joueur(1, typeJ1 == Joueur.Type.HUMAIN ? "Joueur 1 (Vous)" : "Joueur 1 (IA)", typeJ1, Constantes.LIGNE_BUT_J1);
        j1.setPion(new Pion(Constantes.LIGNE_DEPART_J1, Constantes.COLONNE_DEPART_J1));
        
        Joueur j2 = new Joueur(2, typeJ2 == Joueur.Type.HUMAIN ? "Joueur 2 (Vous)" : "Joueur 2 (IA)", typeJ2, Constantes.LIGNE_BUT_J2);
        j2.setPion(new Pion(Constantes.LIGNE_DEPART_J2, Constantes.COLONNE_DEPART_J2));
        
        this.etatJeu = new EtatJeu(java.util.List.of(j1, j2));
        this.undoStack.clear();
        this.hoverRow = -1; 
        this.hoverCol = -1;
        infoPanel.updateInfo(etatJeu.getJoueurCourant().getNom(), j1.getBarrieresRestantes(), j2.getBarrieresRestantes());
        repaint();
        
        if (typeJ1 == Joueur.Type.IA) {
            updateUITurn();
        }
    }

    public PlateauPanel(GameService gameService, InfoPanel infoPanel, 
                        RegleService regleService, 
                        AlphaBetaService alphaBetaService,
                        NegAlphaBetaService negAlphaBetaService,
                        SSSStarService sssStarService) {
        this.gameService = gameService;
        this.infoPanel = infoPanel;
        this.regleService = regleService;
        this.alphaBetaService = alphaBetaService;
        this.negAlphaBetaService = negAlphaBetaService;
        this.sssStarService = sssStarService;
        
        setBackground(new Color(220, 220, 220));
        
        infoPanel.setOnUndo(() -> undo());
        startNewGame(this.gameMode, this.iaAlgo, this.currentDepth);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (etatJeu.estTermine() || etatJeu.getJoueurCourant().getType() == Joueur.Type.IA) {
                    return; 
                }

                int[] offsets = getBoardOffsets();
                int size = Constantes.TAILLE_CASE;
                int gap = 10;
                int cellSize = size + gap;

                int mx = e.getX() - offsets[0];
                int my = e.getY() - offsets[1];

                if (mx < 0 || my < 0) return;

                int col = mx / cellSize;
                int row = my / cellSize;
                int remX = mx % cellSize;
                int remY = my % cellSize;

                boolean coupJoue = false;
                EtatJeu beforeMove = new EtatJeu(etatJeu); 

                if (infoPanel.isModeBarriere()) {
                    if (row >= 0 && row < Constantes.TAILLE_PLATEAU - 1 && col >= 0 && col < Constantes.TAILLE_PLATEAU - 1) {
                        Barriere b = new Barriere(row, col, hoverOrientation);
                        coupJoue = gameService.placeBarriere(etatJeu, b);
                    }
                } else {
                    if (row < Constantes.TAILLE_PLATEAU && col < Constantes.TAILLE_PLATEAU && remX <= size && remY <= size) {
                        coupJoue = gameService.movePion(etatJeu, row, col);
                    }
                }

                if (coupJoue) {
                    undoStack.push(beforeMove); 
                    hoverRow = -1; 
                    updateUITurn();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!infoPanel.isModeBarriere() || etatJeu.estTermine() || etatJeu.getJoueurCourant().getType() == Joueur.Type.IA) {
                    hoverRow = -1; hoverCol = -1;
                    repaint();
                    return;
                }

                int[] offsets = getBoardOffsets();
                int size = Constantes.TAILLE_CASE;
                int gap = 10;
                int cellSize = size + gap;

                int mx = e.getX() - offsets[0];
                int my = e.getY() - offsets[1];

                if (mx < 0 || my < 0) {
                    hoverRow = -1; hoverCol = -1;
                    repaint();
                    return;
                }

                int col = mx / cellSize;
                int row = my / cellSize;
                int remX = mx % cellSize;
                int remY = my % cellSize;

                if (row >= 0 && row < Constantes.TAILLE_PLATEAU - 1 && col >= 0 && col < Constantes.TAILLE_PLATEAU - 1) {
                    hoverRow = row;
                    hoverCol = col;
                    if (remX > remY) {
                        hoverOrientation = Barriere.Orientation.VERTICAL;
                    } else {
                        hoverOrientation = Barriere.Orientation.HORIZONTAL;
                    }
                } else {
                    hoverRow = -1; hoverCol = -1;
                }
                repaint();
            }
        });
    }

    private void updateUITurn() {
        Joueur j1 = etatJeu.getJoueurs().get(0);
        Joueur j2 = etatJeu.getJoueurs().get(1);
        infoPanel.updateInfo(etatJeu.getJoueurCourant().getNom(), j1.getBarrieresRestantes(), j2.getBarrieresRestantes());
        repaint(); 

        Joueur gagnant = etatJeu.getGagnant();
        if (gagnant != null) {
            JOptionPane.showMessageDialog(this, "Partie terminée ! " + gagnant.getNom() + " a gagné !");
            if (mainFrame != null) {
                mainFrame.showPanel("MENU");
            }
            return;
        }

        if (etatJeu.getJoueurCourant().getType() == Joueur.Type.IA) {
            new Thread(() -> {
                try {
                    Thread.sleep(500); 
                } catch (InterruptedException ex) { }

                if (iaAlgo.equals("Alpha-Bêta")) {
                    alphaBetaService.jouerCoup(etatJeu, currentDepth);
                } else if (iaAlgo.equals("Négα-Bêta")) {
                    negAlphaBetaService.jouerCoup(etatJeu, currentDepth);
                } else if (iaAlgo.equals("SSS*")) {
                    sssStarService.jouerCoup(etatJeu, currentDepth);
                }

                SwingUtilities.invokeLater(() -> updateUITurn());
            }).start();
        }
    }

    public void setEtatJeu(EtatJeu etatJeu) {
        this.etatJeu = etatJeu;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int[] offsets = getBoardOffsets();
        int offsetX = offsets[0];
        int offsetY = offsets[1];
        int size = Constantes.TAILLE_CASE;
        int gap = 10;
        
        for(int r = 0; r < Constantes.TAILLE_PLATEAU; r++) {
            for(int c = 0; c < Constantes.TAILLE_PLATEAU; c++) {
                g2.setColor(new Color(139, 69, 19)); 
                g2.fillRect(offsetX + c*(size+gap), offsetY + r*(size+gap), size, size);
            }
        }
        
        if (!etatJeu.estTermine() && etatJeu.getJoueurCourant().getType() == Joueur.Type.HUMAIN && !infoPanel.isModeBarriere()) {
            g2.setColor(new Color(0, 255, 0, 100)); 
            List<int[]> validMoves = regleService.getMouvementsValides(etatJeu);
            for (int[] m : validMoves) {
                g2.fillRect(offsetX + m[1]*(size+gap), offsetY + m[0]*(size+gap), size, size); 
            }
        }
        
        if (!etatJeu.estTermine() && etatJeu.getJoueurCourant().getType() == Joueur.Type.HUMAIN && infoPanel.isModeBarriere() && hoverRow != -1) {
            Barriere testB = new Barriere(hoverRow, hoverCol, hoverOrientation);
            if (regleService.validerPlacementBarriere(etatJeu, testB)) {
                g2.setColor(new Color(100, 100, 100, 150)); 
            } else {
                g2.setColor(new Color(255, 0, 0, 150)); 
            }
            int x = offsetX + hoverCol*(size+gap);
            int y = offsetY + hoverRow*(size+gap);
            if (hoverOrientation == Barriere.Orientation.HORIZONTAL) {
                g2.fillRect(x, y + size, size*2 + gap, gap);
            } else {
                g2.fillRect(x + size, y, gap, size*2 + gap);
            }
        }
        
        for(Joueur j : etatJeu.getJoueurs()) {
            Pion p = j.getPion();
            if (j.getId() == 1) g2.setColor(Color.BLUE);
            else g2.setColor(Color.RED);
            
            g2.fillOval(offsetX + p.getCol()*(size+gap) + 10, offsetY + p.getRow()*(size+gap) + 10, size - 20, size - 20);
        }
        
        g2.setColor(Color.BLACK);
        for(Barriere b : etatJeu.getPlateau().getBarrieres()) {
            int x = offsetX + b.getCol()*(size+gap);
            int y = offsetY + b.getRow()*(size+gap);
            if(b.getOrientation() == Barriere.Orientation.HORIZONTAL) {
                g2.fillRect(x, y + size, size*2 + gap, gap);
            } else {
                g2.fillRect(x + size, y, gap, size*2 + gap);
            }
        }
    }
}
