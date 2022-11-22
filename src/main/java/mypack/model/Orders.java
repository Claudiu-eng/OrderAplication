package mypack.model;

public class Orders {
    private int id_order,id_client,id_product,cantity;

    public Orders(){

    }

    public Orders(int id_order, int id_client, int id_product, int cantity) {
        this.id_order = id_order;
        this.id_client = id_client;
        this.id_product = id_product;
        this.cantity = cantity;
    }


    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getCantity() {
        return cantity;
    }

    public void setCantity(int cantity) {
        this.cantity = cantity;
    }
}
