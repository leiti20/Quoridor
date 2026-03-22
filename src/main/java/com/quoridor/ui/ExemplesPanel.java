package com.quoridor.ui;

import com.quoridor.util.Constantes;
import com.quoridor.model.*;
import com.quoridor.service.GameService;
import com.quoridor.service.RegleService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExemplesPanel extends JPanel {
    private MainFrame mainFrame;
    private List<EtatJeu> etats = new ArrayList<>();
    private int cursor = 0;
    
    private final JPanel boardDemoPanel;
    private final JLabel lblInfo;

    public ExemplesPanel(GameService gameService, RegleService regleService) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Exemples de parties (Replay)", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Simulation d'une séquence de coups pour l'exemple
        genererDemo(gameService);
        
        lblInfo = new JLabel("Tour 0 / " + (etats.size() - 1), SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Sous-panneau de dessin du replay
        boardDemoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (etats.isEmpty()) return;
                EtatJeu etatCourant = etats.get(cursor);
                
                int size = 36; // Encore plus petit pour que le carré soit complètement éloigné des rebords
                int gap = 6;
                
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int boardSize = 9 * size + 8 * gap;
                
                int offsetX = Math.max((panelWidth - boardSize) / 2, 0);
                int offsetY = Math.max((panelHeight - boardSize) / 2, 0);
                
                // Dessin plateau
                for(int r = 0; r < Constantes.TAILLE_PLATEAU; r++) {
                    for(int c = 0; c < Constantes.TAILLE_PLATEAU; c++) {
                        g2.setColor(new Color(139, 69, 19)); 
                        g2.fillRect(offsetX + c*(size+gap), offsetY + r*(size+gap), size, size);
                    }
                }
                
                // Pions
                for(Joueur j : etatCourant.getJoueurs()) {
                    Pion p = j.getPion();
                    if (j.getId() == 1) g2.setColor(Color.BLUE); else g2.setColor(Color.RED);
                    g2.fillOval(offsetX + p.getCol()*(size+gap) + (size/6), offsetY + p.getRow()*(size+gap) + (size/6), size * 2/3, size * 2/3);
                }
                
                // Barrières
                g2.setColor(Color.BLACK);
                for(Barriere b : etatCourant.getPlateau().getBarrieres()) {
                    int x = offsetX + b.getCol()*(size+gap);
                    int y = offsetY + b.getRow()*(size+gap);
                    if(b.getOrientation() == Barriere.Orientation.HORIZONTAL) {
                        g2.fillRect(x, y + size, size*2 + gap, gap);
                    } else {
                        g2.fillRect(x + size, y, gap, size*2 + gap);
                    }
                }
            }
        };
        boardDemoPanel.setPreferredSize(new Dimension(800, 650));
        add(boardDemoPanel, BorderLayout.CENTER);
        
        JPanel controls = new JPanel();
        controls.setOpaque(false);
        JButton btnPrev = new JButton("<< Précédent");
        JButton btnNext = new JButton("Suivant >>");
        
        btnPrev.addActionListener(e -> {
            if(cursor > 0) cursor--;
            lblInfo.setText("Tour " + cursor + " / " + (etats.size() - 1));
            boardDemoPanel.repaint();
        });
        
        btnNext.addActionListener(e -> {
            if(cursor < etats.size() - 1) cursor++;
            lblInfo.setText("Tour " + cursor + " / " + (etats.size() - 1));
            boardDemoPanel.repaint();
        });

        JButton btnRetour = new JButton("Retour au Menu");
        btnRetour.addActionListener(e -> {
            if(mainFrame != null) mainFrame.showPanel("MENU");
            cursor = 0; // reset preview
        });
        
        controls.add(btnRetour);
        controls.add(Box.createRigidArea(new Dimension(50, 0)));
        controls.add(btnPrev);
        controls.add(btnNext);
        controls.add(lblInfo);
        
        add(controls, BorderLayout.SOUTH);
    }
    
    private void genererDemo(GameService gameService) {
        Joueur j1 = new Joueur(1, "Joueur 1", Joueur.Type.HUMAIN, Constantes.LIGNE_BUT_J1);
        j1.setPion(new Pion(Constantes.LIGNE_DEPART_J1, Constantes.COLONNE_DEPART_J1));
        Joueur j2 = new Joueur(2, "Joueur 2", Joueur.Type.HUMAIN, Constantes.LIGNE_BUT_J2);
        j2.setPion(new Pion(Constantes.LIGNE_DEPART_J2, Constantes.COLONNE_DEPART_J2));
        
        EtatJeu e0 = new EtatJeu(List.of(j1, j2));
        etats.add(new EtatJeu(e0));
        
        // Simulation séquence
        gameService.movePion(e0, 7, 4); etats.add(new EtatJeu(e0));
        gameService.movePion(e0, 1, 4); etats.add(new EtatJeu(e0));
        gameService.placeBarriere(e0, new Barriere(7, 3, Barriere.Orientation.HORIZONTAL)); etats.add(new EtatJeu(e0));
        gameService.placeBarriere(e0, new Barriere(1, 4, Barriere.Orientation.HORIZONTAL)); etats.add(new EtatJeu(e0));
        gameService.movePion(e0, 7, 5); etats.add(new EtatJeu(e0));
        gameService.movePion(e0, 1, 5); etats.add(new EtatJeu(e0));
        gameService.movePion(e0, 6, 5); etats.add(new EtatJeu(e0));
        gameService.movePion(e0, 2, 5); etats.add(new EtatJeu(e0));
        gameService.placeBarriere(e0, new Barriere(5, 4, Barriere.Orientation.VERTICAL)); etats.add(new EtatJeu(e0));
    }
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
