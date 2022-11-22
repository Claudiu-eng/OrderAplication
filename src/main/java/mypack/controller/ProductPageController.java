package mypack.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mypack.bll.ProductBLL;
import mypack.model.Client;
import mypack.model.Product;

import javax.swing.text.TabableView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductPageController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableColumn<Product, Integer> id_product, stoc;
    @FXML
    private TableColumn<Product, String> name;
    @FXML
    private TableColumn<Product, Float> price;
    @FXML
    private TableView productsTable;
    private ProductBLL productBLL;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productBLL = new ProductBLL();
        id_product.setCellValueFactory(new PropertyValueFactory<>("id_product"));
        stoc.setCellValueFactory(new PropertyValueFactory<>("stoc"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        ArrayList<Product> products = productBLL.getProducts();
        ObservableList<Product> products1 = FXCollections.observableArrayList(products);
        if (products != null) {
            productsTable.setItems(products1);
        }

    }

    public void backBTNEvent(ActionEvent actionEvent) throws IOException {

        changeScene("/mypack/MainPage.fxml", actionEvent);
    }

    private void initialize(String file, FXMLLoader loader) {
        if (file.contains("EditProduct.fxml")) {
            EditProductController editProductController = loader.getController();
            editProductController.initialize((Product) productsTable.getSelectionModel().getSelectedItem(), productBLL);
        } else if (file.endsWith("AddProduct.fxml")) {
            AddProductController addProductController = loader.getController();
            addProductController.initialize(null, productBLL);
        }
    }


    private void changeScene(String fxmlFile, ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        root = loader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        initialize(fxmlFile, loader);
        stage.show();
    }

    public void editProductEvent(ActionEvent actionEvent) throws IOException {
        Product product = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (product == null)
            showAlertWithHeaderText("niciun produs selectat spre eliminare");
        else
            changeScene("/mypack/EditProduct.fxml", actionEvent);
    }

    public void deleteProductEvent(ActionEvent actionEvent) {
        Product product = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (product == null)
            showAlertWithHeaderText("niciun produs selectat spre eliminare");
        else {
            productBLL.deleteProduct(product);
            productsTable.getItems().remove(product);
        }
    }

    private void showAlertWithHeaderText(String p) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WARNING");
        alert.setContentText(p);
        alert.showAndWait();
    }

    public void addProductEvent(ActionEvent actionEvent) throws IOException {
        changeScene("/mypack/AddProduct.fxml", actionEvent);

    }

}
