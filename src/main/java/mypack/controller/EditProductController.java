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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mypack.bll.ProductBLL;
import mypack.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProductController {
    @FXML
    private Label messageFinal;
    @FXML
    private TextField newNameTextField, oldNameTextField, newStockTextField, oldStockTextField, newPriceTextField, oldPriceTextField, id;
    private ProductBLL productBLL;
    private Product product;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize(Product product, ProductBLL productBLL) {
        id.setText(String.valueOf(product.getId_product()));
        oldNameTextField.setText(product.getName());
        oldPriceTextField.setText(String.valueOf(product.getPrice()));
        oldStockTextField.setText(String.valueOf(product.getStoc()));
        this.productBLL = productBLL;
        this.product = product;
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

    private void showAlertWithHeaderText(String p) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WARNING");
        alert.setContentText(p);
        alert.showAndWait();
    }

    public void editButtonOnAction(ActionEvent actionEvent) throws IOException {
        messageFinal.setText("");
        if (newNameTextField.getText().equals("") || newPriceTextField.getText().equals("") || newStockTextField.getText().equals(""))
            showAlertWithHeaderText("complete all fields!!!");
        else {
            if (newStockTextField.getText().length() == 0 || newNameTextField.getText().length() == 0 || newPriceTextField.getText().length() == 0)
                showAlertWithHeaderText("introduce ti date valide");
            if (!validateNumber(newStockTextField.getText(), false) || !validateNumber(newPriceTextField.getText(), true))
                showAlertWithHeaderText("introduce ti date valide");
            else {
                product.setStoc(Integer.parseInt(newStockTextField.getText()));
                product.setPrice(Float.parseFloat(newPriceTextField.getText()));
                product.setName(newNameTextField.getText());
                productBLL.updateProduct(product);
                messageFinal.setText("Succesfull!");
            }

        }

    }
}
