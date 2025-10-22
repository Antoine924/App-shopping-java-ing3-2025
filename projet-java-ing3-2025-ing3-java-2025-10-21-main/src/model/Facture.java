package model;

import java.text.DecimalFormat;

/**
 * Classe qui permet de générer une facture à partir d’un panier.
 */
public class Facture {
    private Panier panier;
    private Client client;

    public Facture(Client client, Panier panier) {
        this.client = client;
        this.panier = panier;
    }

    // Affiche la facture complète avec les détails des articles
    public String genererFacture() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#0.00");

        sb.append("📄 FACTURE\n");
        sb.append("Client : ").append(client.getNom()).append(" (").append(client.getTypeClient()).append(")\n\n");

        sb.append("Articles achetés :\n");

        for (Article article : panier.getArticles()) {
            int qte = panier.getQuantite(article);
            double prixUnitaire = (qte >= article.getSeuilGros()) ? article.getPrixGros() : article.getPrixUnitaire();
            double sousTotal = prixUnitaire * qte;

            sb.append("- ").append(article.getNom())
                    .append(" x").append(qte)
                    .append(" → ").append(df.format(sousTotal)).append(" €\n");
        }

        sb.append("\n💰 Total à payer : ").append(df.format(panier.calculerTotal())).append(" €");

        return sb.toString();
    }
}


