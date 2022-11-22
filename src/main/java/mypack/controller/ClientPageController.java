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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mypack.bll.ClientBLL;
import mypack.bll.ProductBLL;
import mypack.model.Client;
import mypack.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientPageController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableColumn<Client, Integer> id_client;
    @FXML
    private TableColumn<Client,String> name,email;

    @FXML
    private TableView clientsTable;
    private ClientBLL clientBLL;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientBLL = new ClientBLL();
        id_client.setCellValueFactory(new PropertyValueFactory<>("id_client"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        ArrayList<Client> clients= clientBLL.getClients();
        ObservableList<Client> clients1= FXCollections.observableArrayList(clients);
        if (clients != null) {
            clientsTable.setItems(clients1);
        }

    }

    private void initialize(String file, FXMLLoader loader) {
        if (file.contains("EditClient.fxml")) {
            EditClientController editClientController = loader.getController();
            editClientController.initialize((Client) clientsTable.getSelectionModel().getSelectedItem(), clientBLL);
        } else if (file.endsWith("AddClient.fxml")) {
            AddClientController addClientController = loader.getController();
            addClientController.initialize(null, clientBLL);
        }
    }

    private void changeScene(String fxmlFile,ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource(fxmlFile));
        root=loader.load();
        stage=(Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        initialize(fxmlFile,loader);
        stage.show();
    }
    @FXML
    private void backBTNEvent(ActionEvent actionEvent) throws IOException {

        changeScene("/mypack/MainPage.fxml",actionEvent);
    }
    @FXML
    private void editClientEvent(ActionEvent actionEvent) throws IOException {
        Client client = (Client) clientsTable.getSelectionModel().getSelectedItem();
        if (client == null)
            showAlertWithHeaderText("niciun produs selectat spre eliminare");
        else
            changeScene("/mypack/EditClient.fxml", actionEvent);
    }
    @FXML
    private void deleteClientEvent(ActionEvent actionEvent) {
        Client client= (Client) clientsTable.getSelectionModel().getSelectedItem();
        if(client==null)
            showAlertWithHeaderText("niciun client selectat pt eliminare");
        else {
            clientBLL.deleteClient(client);
            clientsTable.getItems().remove(client);
        }
    }
    private void showAlertWithHeaderText(String p) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WARNING");
        alert.setContentText(p);
        alert.showAndWait();
    }
    @FXML
    private void addClientEvent(ActionEvent actionEvent) throws IOException {
        changeScene("/mypack/AddClient.fxml",actionEvent);

    }

}
