package com.quoridor.ui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class NiveauPanel extends JPanel {

    private MainFrame mainFrame;
    private final PlateauPanel plateauPanel;

    public NiveauPanel(PlateauPanel plateauPanel) {
        this.plateauPanel = plateauPanel;
        
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Configuration de la Partie", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); 
        gbc.anchor = GridBagConstraints.WEST;

        // Mode
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mode de jeu :"), gbc);
        JComboBox<String> modeBox = new JComboBox<>(new String[]{"Humain vs IA", "Humain vs Humain"});
        modeBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        formPanel.add(modeBox, gbc);

        // Algorithme
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Algorithme de l'IA :"), gbc);
        JComboBox<String> algoBox = new JComboBox<>(new String[]{"Alpha-Bêta", "Négα-Bêta", "SSS*"});
        algoBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        formPanel.add(algoBox, gbc);

        // Niveau
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Niveau (Profondeur) :"), gbc);
        JComboBox<String> levelBox = new JComboBox<>(new String[]{"Facile (1)", "Moyen (3)", "Difficile (5)"});
        levelBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        formPanel.add(levelBox, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        
        JButton btnRetour = new JButton("Retour");
        btnRetour.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRetour.setPreferredSize(new Dimension(150, 40));
        btnRetour.addActionListener(e -> {
            if(mainFrame != null) mainFrame.showPanel("MENU");
        });
        
        JButton btnDemarrer = new JButton("Démarrer la partie !");
        btnDemarrer.setFont(new Font("Arial", Font.BOLD, 16));
        btnDemarrer.setPreferredSize(new Dimension(220, 40));
        btnDemarrer.addActionListener(e -> {
            if (mainFrame != null) {
                String mode = (String) modeBox.getSelectedItem();
                String algo = (String) algoBox.getSelectedItem();
                String lvl = (String) levelBox.getSelectedItem();
                
                int depth = 3; 
                if (lvl != null && lvl.contains("1")) depth = 1;
                else if (lvl != null && lvl.contains("5")) depth = 5;
                
                this.plateauPanel.startNewGame(mode, algo, depth);
                mainFrame.showPanel("JEU");
            }
        });
        
        bottomPanel.add(btnRetour);
        bottomPanel.add(btnDemarrer);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
