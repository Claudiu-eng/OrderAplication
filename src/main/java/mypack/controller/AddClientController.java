package mypack.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mypack.bll.ClientBLL;
import mypack.model.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddClientController{
    private ClientBLL clientBLL;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField nameTextField,emailTextFileld;

    public void initialize(Client client,ClientBLL clientBLL){
        this.clientBLL=clientBLL;
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        String name=nameTextField.getText();
        String email=emailTextFileld.getText();
        Client client=new Client();
        client.setEmail(email);
        client.setName(name);
        clientBLL.insertClient(client);
        nameTextField.setText("");
        emailTextFileld.setText("");
    }

    public void backBTNEvent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/mypack/Client.fxml"));
        root=loader.load();
        stage=(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

}
