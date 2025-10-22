package controller;

import DAO.ClientDAO;
import model.Client;

import java.util.List;

public class ClientController {
    private ClientDAO dao;

    public ClientController() {
        this.dao = new ClientDAO();
    }

    public void ajouterClient(Client client) {
        dao.ajouterClient(client);
    }

    public void modifierClient(Client client) {
        dao.modifierClient(client);
    }

    public void supprimerClient(int id) {
        dao.supprimerClient(id);
    }

    public List<Client> listerClients() {
        return dao.listerClients();
    }
}
