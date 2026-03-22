package com.quoridor.ui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class InfoPanel extends JPanel {

    private final JLabel tourLabel;
    private final JLabel barriereJ1Label;
    private final JLabel barriereJ2Label;
    private final JRadioButton modeDeplacement;
    private final JRadioButton modeBarriere;
    
    private Runnable onUndo;

    public void setOnUndo(Runnable onUndo) {
        this.onUndo = onUndo;
    }

    public InfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 0));
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tourLabel = new JLabel("Au tour de : Joueur 1");
        tourLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tourLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        barriereJ1Label = new JLabel("Joueur 1 (Bleu) : 10 barrières");
        barriereJ1Label.setForeground(Color.BLUE);
        barriereJ1Label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        barriereJ1Label.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        barriereJ2Label = new JLabel("Joueur 2 (Rouge) : 10 barrières");
        barriereJ2Label.setForeground(Color.RED);
        barriereJ2Label.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        // Options de jeu
        modeDeplacement = new JRadioButton("Mode Déplacement", true);
        modeBarriere = new JRadioButton("Mode Barrière", false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(modeDeplacement); bg.add(modeBarriere);
        
        JPanel modesPanel = new JPanel();
        modesPanel.setLayout(new BoxLayout(modesPanel, BoxLayout.Y_AXIS));
        modesPanel.setOpaque(false);
        modesPanel.add(modeDeplacement);
        modesPanel.add(modeBarriere);
        modesPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        JButton undoBtn = new JButton("Annuler coup (Undo)");
        undoBtn.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        undoBtn.addActionListener(e -> {
            if (onUndo != null) {
                onUndo.run();
            }
        });

        add(tourLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(barriereJ1Label);
        add(barriereJ2Label);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(modesPanel);
        add(Box.createVerticalGlue());
        add(undoBtn);
    }

    public void updateInfo(String nomJoueurCourant, int barrieresJ1, int barrieresJ2) {
        tourLabel.setText("Au tour de : " + nomJoueurCourant);
        barriereJ1Label.setText("Joueur 1 (Bleu) : " + barrieresJ1 + " barrières");
        barriereJ2Label.setText("Joueur 2 (Rouge) : " + barrieresJ2 + " barrières");
    }
    
    public boolean isModeBarriere() {
        return modeBarriere.isSelected();
    }
}
