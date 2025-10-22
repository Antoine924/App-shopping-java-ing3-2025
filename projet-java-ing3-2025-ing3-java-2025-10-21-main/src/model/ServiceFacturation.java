package model;

/**
 * Classe utilitaire pour gérer les règles de facturation et les calculs de total.
 */
public class ServiceFacturation {

    /**
     * Calcule le total à payer en fonction du panier et du type de client.
     * @param client le client concerné
     * @param panier le panier du client
     * @return le total final à payer (avec remises éventuelles)
     */
    public static double calculerTotalFinal(Client client, Panier panier) {
        double total = panier.calculerTotal();

        // 🔸 Exemple de remise pour les anciens clients : -10%
        if (client.getTypeClient().equalsIgnoreCase("ANCIEN")) {
            total *= 0.9;
        }

        // Tu peux ajouter d'autres règles ici (TVA, bons de réduction, etc.)
        return total;
    }

    /**
     * Calcule le montant de la remise appliquée.
     * @param client le client concerné
     * @param panier le panier du client
     * @return le montant de la remise
     */
    public static double calculerRemise(Client client, Panier panier) {
        double totalSansRemise = panier.calculerTotal();
        double totalAvecRemise = calculerTotalFinal(client, panier);

        return totalSansRemise - totalAvecRemise;
    }
}
