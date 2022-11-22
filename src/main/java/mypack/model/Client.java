package mypack.model;

public class Client {
    private int id_client;
    private String name,email;

    public Client() {
    }

    public Client(int id_client, String name, String email) {
        this.id_client = id_client;
        this.name = name;
        this.email = email;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
