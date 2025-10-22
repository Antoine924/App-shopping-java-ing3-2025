package model;

import java.util.*;

/**
 * Représente le panier d'un client.
 * Contient les articles ajoutés avec leurs quantités.
 */
public class Panier {
    // HashMap pour stocker chaque article et sa quantité
    private Map<Article, Integer> articles;

    public Panier() {
        this.articles = new HashMap<>();
    }

    /**
     * Ajoute un article au panier avec une certaine quantité.
     * Si l'article existe déjà, on ajoute la quantité en plus.
     */
    public void ajouterArticle(Article article, int quantite) {
        if (article == null) return; // Vérifie que l'article existe
        if (article.getId() == 0) { // Vérifie que l'article est bien valide avec un ID
            System.out.println(" Article sans ID, ne peut pas être ajouté au panier : " + article);
            return;
        }

        // Ajoute l'article ou augmente sa quantité existante
        articles.put(article, articles.getOrDefault(article, 0) + quantite);

        // Message console pour suivre l'ajout
        System.out.println(" Ajouté au panier : " + article.getId() + " - " + article.getNom() + " x" + quantite);
    }

    /**
     * Supprime complètement un article du panier.
     */
    public void supprimerArticle(Article article) {
        articles.remove(article); // Retire l'article de la HashMap
    }

    /**
     * Vide complètement le panier.
     */
    public void vider() {
        articles.clear(); // Efface tout le contenu du panier
    }

    /**
     * Renvoie tous les articles présents dans le panier (sans doublons).
     */
    public Set<Article> getArticles() {
        return articles.keySet(); // Retourne tous les articles du panier
    }

    /**
     * Renvoie la quantité d’un article spécifique.
     */
    public int getQuantite(Article article) {
        return articles.getOrDefault(article, 0); // Retourne la quantité ou 0 si l'article n'existe pas
    }

    /**
     * Calcule le total du panier en tenant compte du prix de gros si applicable.
     */
    public double calculerTotal() {
        double total = 0.0;

        for (Map.Entry<Article, Integer> entry : articles.entrySet()) {
            Article article = entry.getKey();
            int quantite = entry.getValue();

            // Si on dépasse le seuil de quantité, on applique le prix de gros
            if (quantite >= article.getSeuilGros()) {
                total += article.getPrixGros() * quantite;
            } else {
                // Sinon prix normal unitaire
                total += article.getPrixUnitaire() * quantite;
            }
        }

        return total;
    }

    @Override
    public String toString() {
        // Génère un affichage lisible du panier
        StringBuilder sb = new StringBuilder(" Panier :\n");

        for (Map.Entry<Article, Integer> entry : articles.entrySet()) {
            Article article = entry.getKey();
            int quantite = entry.getValue();

            sb.append("- ").append(article.getNom())
              .append(" x").append(quantite)
              .append("\n");
        }

        // Ajoute le total du panier à la fin
        sb.append(" Total : ").append(calculerTotal()).append(" €");
        return sb.toString();
    }
}
