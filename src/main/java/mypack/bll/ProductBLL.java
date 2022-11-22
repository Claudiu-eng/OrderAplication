package mypack.bll;

import mypack.dao.ProductDAO;
import mypack.model.Product;

import java.util.ArrayList;

public class ProductBLL {
    private ProductDAO productDAO;

    public ProductBLL() {
        this.productDAO = new ProductDAO();

    }
    public ArrayList<Product> getProducts(){
        return productDAO.findAll();
    }
    public void deleteProduct(Product product){
        productDAO.delete(product);
    }
    public void insertProduct(Product product){
        productDAO.insert(product);
    }
    public void updateProduct(Product product){
        productDAO.update(product);
    }
}
