package kz.shymkent.relaxhouse.models;

import java.util.List;

public class Cottage {
    String name;
    List<Client> clients;

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
