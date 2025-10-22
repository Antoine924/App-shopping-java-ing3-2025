package controller;

import DAO.ArticleDAO;
import model.Article;

import java.util.List;

public class ArticleController {
    private ArticleDAO dao;

    public ArticleController() {
        this.dao = new ArticleDAO();
    }

    public void ajouterArticle(Article article) {
        dao.ajouterArticle(article);
    }

    public void modifierArticle(Article article) {
        dao.modifierArticle(article);
    }

    public void supprimerArticle(int id) {
        dao.supprimerArticle(id);
    }

    public List<Article> listerArticles() {
        return dao.listerArticles();
    }
}
