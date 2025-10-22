package controller;

import DAO.StatistiqueDAO;

public class StatistiqueController {
    private StatistiqueDAO dao;

    public StatistiqueController() {
        this.dao = new StatistiqueDAO();
    }

    public int compterArticles() {
        return dao.compterArticles();
    }

    public double prixMoyenArticles() {
        return dao.prixMoyenArticles();
    }

    // Tu peux en ajouter d'autres si tu étends le DAO plus tard
}
