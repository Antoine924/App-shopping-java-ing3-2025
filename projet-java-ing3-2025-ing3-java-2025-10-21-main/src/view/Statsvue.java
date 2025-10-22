package view;

// Import des DAO et bibliothèques nécessaires pour les graphiques
import DAO.StatscommandeDAO;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

// Classe qui gère l'affichage de la fenêtre des statistiques
public class Statsvue extends JFrame {

    private StatscommandeDAO statDAO; // DAO pour récupérer les statistiques en base

    public Statsvue() {
        super("Statistiques des ventes"); // Titre de la fenêtre
        statDAO = new StatscommandeDAO(); // Initialisation du DAO

        setSize(1000, 650); // Taille de la fenêtre
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Ferme l'application à la fermeture de la fenêtre

        // Titre en haut de la fenêtre
        JLabel titre = new JLabel("Tableau de bord des ventes", SwingConstants.CENTER);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titre.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Création des onglets pour les différentes statistiques
        JTabbedPane onglets = new JTabbedPane();
        onglets.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        onglets.addTab("Ventes totales", creerGraphiqueVentesTotales());
        onglets.addTab("Produits populaires", creerGraphiqueProduitsVendus());
        onglets.addTab("Par client", creerGraphiqueParClient());

        // Bouton pour revenir au menu admin
        JButton boutonRetour = new JButton("↩ Retour au menu");
        boutonRetour.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        boutonRetour.setBackground(new Color(230, 240, 255));
        boutonRetour.addActionListener(e -> {
            dispose(); // Ferme cette fenêtre
            new Menuadminvue().setVisible(true); // Ouvre le menu admin
        });

        // Panel en bas pour contenir le bouton retour
        JPanel panneauBas = new JPanel();
        panneauBas.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panneauBas.add(boutonRetour);

        // Layout général de la fenêtre
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(titre, BorderLayout.NORTH);
        getContentPane().add(onglets, BorderLayout.CENTER);
        getContentPane().add(panneauBas, BorderLayout.SOUTH);
    }

    // Crée le panneau qui affiche le total des ventes
    private JPanel creerGraphiqueVentesTotales() {
        double totalVentes = statDAO.calculerVentesTotales(); // Récupère les ventes totales

        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setBackground(Color.white);
        panneau.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titre = new JLabel("Total des ventes", SwingConstants.CENTER);
        titre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JLabel valeur = new JLabel(String.format("%.2f €", totalVentes), SwingConstants.CENTER);
        valeur.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        valeur.setForeground(new Color(34, 139, 34)); // Couleur verte

        panneau.add(titre, BorderLayout.NORTH);
        panneau.add(valeur, BorderLayout.CENTER);

        return panneau;
    }

    // Crée un histogramme des produits les plus vendus
    private JPanel creerGraphiqueProduitsVendus() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> stats = statDAO.getQuantitesParArticle(); // Quantités par article

        // Remplissage du dataset
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            dataset.addValue(entry.getValue(), "Quantité", entry.getKey());
        }

        // Création du graphique
        JFreeChart chart = ChartFactory.createBarChart(
                "", "Article", "Quantité vendue", dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        styliserBarChart(chart, new Color(100, 149, 237)); // Style personnalisé

        return new ChartPanel(chart);
    }

    // Crée un camembert des ventes par client
    private JPanel creerGraphiqueParClient() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Double> stats = statDAO.getVentesParClient(); // Ventes par client

        // Remplissage du dataset
        for (Map.Entry<String, Double> entry : stats.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Création du graphique
        JFreeChart chart = ChartFactory.createPieChart(
                "", dataset,
                true, true, false);

        styliserPieChart(chart); // Style personnalisé

        return new ChartPanel(chart);
    }

    // Applique un style à un graphique en barres
    private void styliserBarChart(JFreeChart chart, Color barColor) {
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, barColor);
        // StandardBarPainter non modifié pour garder un rendu simple
    }

    // Applique un style à un graphique en camembert
    private void styliserPieChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.white);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 14));
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 150));
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);
        plot.setOutlineVisible(false);
    }

    // Méthode principale pour lancer la fenêtre
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Statsvue().setVisible(true));
    }
}
