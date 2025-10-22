package controller;

import DAO.CommandeDAO;
import model.Commande;

import java.util.List;

public class CommandeController {
    private CommandeDAO dao;

    public CommandeController() {
        this.dao = new CommandeDAO();
    }

    public void ajouterCommande(Commande commande) {
        dao.ajouterCommande(commande);
    }

    public void supprimerCommande(int idCommande) {
        dao.supprimerCommande(idCommande);
    }

    public List<Commande> listerCommandes() {
        return dao.listerCommandes();
    }
}
