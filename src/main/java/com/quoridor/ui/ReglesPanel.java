package com.quoridor.ui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class ReglesPanel extends JPanel {

    private MainFrame mainFrame;

    public ReglesPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Règles du Quoridor", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTextArea rulesText = new JTextArea();
        rulesText.setEditable(false);
        rulesText.setFont(new Font("Arial", Font.PLAIN, 16));
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);
        rulesText.setMargin(new Insets(20, 20, 20, 20));
        rulesText.setText(
            "BUT DU JEU :\n" +
            "Atteindre le premier la ligne opposée à sa ligne de départ.\n\n" +
            "DEROULEMENT :\n" +
            "A tour de rôle, chaque joueur choisit de :\n" +
            " - Déplacer son pion (d'une case horizontalement ou verticalement).\n" +
            " - Placer une barrière (composée de 2 tuiles).\n\n" +
            "LES BARRIERES :\n" +
            "Elles ne doivent jamais bloquer totalement l'accès à la ligne de but pour aucun joueur (il doit toujours exister un chemin).\n\n" +
            "LE FACE A FACE :\n" +
            "Si deux pions se font face sans barrière, le joueur dont c'est le tour peut sauter par dessus son adversaire. Si une barrière est située derrière l'adversaire, on peut bifurquer en diagonale.\n"
        );

        add(new JScrollPane(rulesText), BorderLayout.CENTER);

        JButton btnRetour = new JButton("Retour au Menu");
        btnRetour.addActionListener(e -> {
            if(mainFrame != null) mainFrame.showPanel("MENU");
        });
        JPanel southPanel = new JPanel();
        southPanel.add(btnRetour);
        add(southPanel, BorderLayout.SOUTH);
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
