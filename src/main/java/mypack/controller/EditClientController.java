package mypack.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mypack.bll.ClientBLL;
import mypack.bll.ProductBLL;
import mypack.model.Client;
import mypack.model.Product;

import java.io.IOException;

public class EditClientController {
    @FXML
    private Label messageFinal;
    @FXML
    private TextField newNameTextField, oldNameTextField, newEmailTextField, oldEmailTextField, id;
    private ClientBLL clientBLL;
    private Client client;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void initialize(Client client, ClientBLL clientBLL) {
        id.setText(String.valueOf(client.getId_client()));
        oldNameTextField.setText(client.getName());
        oldEmailTextField.setText(String.valueOf(client.getEmail()));
        this.clientBLL = clientBLL;
        this.client = client;
    }

    public void backBTNEvent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypack/Client.fxml"));
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

    public void editButtonOnAction(ActionEvent actionEvent) {
        messageFinal.setText("");
        if (newNameTextField.getText().equals("") || newEmailTextField.getText().equals(""))
            showAlertWithHeaderText("complete all fields!!!");
        else {
            if (clientBLL.updateClient(client)) {
                messageFinal.setText("Succesfull!");
                client.setEmail(newEmailTextField.getText());
                client.setName(newNameTextField.getText());
            }
        }

    }

}
