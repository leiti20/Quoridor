package com.quoridor.ui;

import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@Component
public class MenuPanel extends JPanel {
    private MainFrame mainFrame;
    
    public MenuPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));
        
        JLabel title = new JLabel("QUORIDOR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 50, 0));
        add(title, BorderLayout.NORTH);
        
        JPanel boutonsPanel = new JPanel(new GridBagLayout());
        boutonsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0); // Espacement vertical
        gbc.fill = GridBagConstraints.NONE;
        
        JButton btnJouer = createButton("Jouer");
        JButton btnRegles = createButton("Règles");
        JButton btnExemples = createButton("Exemples de parties");
        JButton btnQuitter = createButton("Quitter");
        
        btnJouer.addActionListener(e -> { if(mainFrame != null) mainFrame.showPanel("NIVEAU"); });
        btnRegles.addActionListener(e -> { if(mainFrame != null) mainFrame.showPanel("REGLES"); });
        btnExemples.addActionListener(e -> { if(mainFrame != null) mainFrame.showPanel("EXEMPLES"); });
        btnQuitter.addActionListener(e -> System.exit(0));
        
        gbc.gridy = 0; boutonsPanel.add(btnJouer, gbc);
        gbc.gridy = 1; boutonsPanel.add(btnRegles, gbc);
        gbc.gridy = 2; boutonsPanel.add(btnExemples, gbc);
        gbc.gridy = 3; boutonsPanel.add(btnQuitter, gbc);
        
        add(boutonsPanel, BorderLayout.CENTER);
    }
    
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 20));
        btn.setPreferredSize(new Dimension(300, 45)); // Longueur et épaisseur fixées pour éviter l'étirement
        return btn;
    }
    
    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
}
