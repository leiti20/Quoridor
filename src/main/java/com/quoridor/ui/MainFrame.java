package com.quoridor.ui;

import com.quoridor.util.Constantes;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainFrame extends JFrame {
    
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    
    private final MenuPanel menuPanel;
    private final NiveauPanel niveauPanel;
    private final ReglesPanel reglesPanel;
    private final ExemplesPanel exemplesPanel;
    private final PlateauPanel plateauPanel;
    private final InfoPanel infoPanel;
    
    public MainFrame(MenuPanel menuPanel, NiveauPanel niveauPanel, ReglesPanel reglesPanel, 
                     ExemplesPanel exemplesPanel, PlateauPanel plateauPanel, InfoPanel infoPanel) {
        this.menuPanel = menuPanel;
        this.niveauPanel = niveauPanel;
        this.reglesPanel = reglesPanel;
        this.exemplesPanel = exemplesPanel;
        this.plateauPanel = plateauPanel;
        this.infoPanel = infoPanel;
        
        initUI();
    }
    
    private void initUI() {
        setTitle("Quoridor - Projet IA");
        setSize(Constantes.LARGEUR_FENETRE, Constantes.HAUTEUR_FENETRE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(niveauPanel, "NIVEAU");
        mainPanel.add(reglesPanel, "REGLES");
        mainPanel.add(exemplesPanel, "EXEMPLES");
        
        JPanel jeuPanel = new JPanel(new BorderLayout());
        jeuPanel.add(plateauPanel, BorderLayout.CENTER);
        jeuPanel.add(infoPanel, BorderLayout.EAST);
        mainPanel.add(jeuPanel, "JEU");
        
        add(mainPanel);
        
        JMenuBar mb = new JMenuBar();
        JMenu f = new JMenu("Fichier");
        JMenuItem mNew = new JMenuItem("Nouvelle Partie");
        JMenuItem mMenu = new JMenuItem("Menu Principal");
        JMenuItem mQuit = new JMenuItem("Quitter");
        
        mNew.addActionListener(e -> showPanel("NIVEAU"));
        mMenu.addActionListener(e -> showPanel("MENU"));
        mQuit.addActionListener(e -> System.exit(0));
        f.add(mNew); f.add(mMenu); f.addSeparator(); f.add(mQuit);
        
        JMenu help = new JMenu("Aide");
        JMenuItem mRules = new JMenuItem("Règles");
        mRules.addActionListener(e -> showPanel("REGLES"));
        help.add(mRules);
        
        mb.add(f); mb.add(help);
        setJMenuBar(mb);
        
        menuPanel.setMainFrame(this);
        niveauPanel.setMainFrame(this);
        reglesPanel.setMainFrame(this);
        exemplesPanel.setMainFrame(this);
        plateauPanel.setMainFrame(this);
    }
    
    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }
}
