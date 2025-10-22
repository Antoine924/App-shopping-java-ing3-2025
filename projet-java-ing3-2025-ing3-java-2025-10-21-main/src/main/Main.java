package main;

import view.FenetrePrincipale;
import view.Gestionarticleview;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetrePrincipale().setVisible(true));
    }
}
