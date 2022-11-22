package mypack.model;

public class Product {
    private int id_product;
    private String name;
    private int stoc;
    private float price;

    public Product(int id_product,String name, int stoc, float price) {
        this.id_product=id_product;
        this.name = name;
        this.stoc = stoc;
        this.price = price;
    }

    public Product(){

    }

    @Override
    public String toString() {
        return "Product{" +
                "id_product=" + id_product +
                ", name='" + name + '\'' +
                ", stoc=" + stoc +
                ", price=" + price +
                '}';
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
