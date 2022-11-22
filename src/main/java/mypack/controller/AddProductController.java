package mypack.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mypack.bll.ClientBLL;
import mypack.bll.ProductBLL;
import mypack.model.Client;
import mypack.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController {
    private ProductBLL productBLL;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField nameTextField, stockTextFileld, priceTextField;

    public void initialize(Product product, ProductBLL productBLL) {
        this.productBLL = productBLL;
    }

    private boolean validateNumber(String s, boolean intreg) {
        int nr = 0;
        if (s.charAt(0) != '+' && s.charAt(0) != '-' && !Character.isDigit(s.charAt(0)))
            return false;
        for (int i = 1; i < s.length(); ++i)
            if (s.charAt(i) == '.')
                nr++;
            else if (!Character.isDigit(s.charAt(i)))
                return false;
        if (nr > 1)
            return false;
        if (nr > 0 && !intreg)
            return false;
        return true;
    }

    private void showAlertWithHeaderText(String p) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WARNING");
        alert.setContentText(p);
        alert.showAndWait();
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        String stoc = stockTextFileld.getText();
        String price = priceTextField.getText();
        if (stockTextFileld.getText().length() == 0 || nameTextField.getText().length() == 0 || priceTextField.getText().length() == 0)
            showAlertWithHeaderText("introduce ti date valide");
        if (!validateNumber(stoc, false) || !validateNumber(price, true))
            showAlertWithHeaderText("introduce ti date valide");
        else {
            Product product = new Product();
            product.setPrice(Float.parseFloat(price));
            product.setName(name);
            product.setStoc(Integer.parseInt(stoc));
            productBLL.insertProduct(product);
            nameTextField.setText("");
            stockTextFileld.setText("");
            priceTextField.setText("");
        }
    }

    public void backBTNEvent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypack/Product.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }


}
