package com.quoridor;

import com.quoridor.ui.MainFrame;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class QuoridorApplication implements CommandLineRunner {

    private final ApplicationContext context;

    public QuoridorApplication(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        // Swing applications require headless mode to be disabled
        new SpringApplicationBuilder(QuoridorApplication.class)
            .headless(false)
            .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = context.getBean(MainFrame.class);
            mainFrame.setVisible(true);
        });
    }
}
