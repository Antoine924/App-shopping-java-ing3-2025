package controller;

import DAO.LigneCommandeDAO;
import model.LigneCommande;

import java.util.List;

public class LigneCommandeController {
    private LigneCommandeDAO dao;

    public LigneCommandeController() {
        this.dao = new LigneCommandeDAO();
    }

    public void ajouterLigneCommande(LigneCommande ligne) {
        dao.ajouterLigneCommande(ligne);
    }

    public void supprimerLigneCommande(int idLigne) {
        dao.supprimerLigneCommande(idLigne);
    }

    public List<LigneCommande> listerLignesParCommande(int idCommande) {
        return dao.listerLignesParCommande(idCommande);
    }
}
