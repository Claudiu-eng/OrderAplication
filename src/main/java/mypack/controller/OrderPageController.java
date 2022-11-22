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
import mypack.bll.OrderBLL;
import mypack.bll.ProductBLL;
import mypack.model.Client;
import mypack.model.Orders;
import mypack.model.Product;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderPageController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableColumn id_client;
    @FXML
    private TableColumn nameClient, email;
    @FXML
    private TableView clientsTable, productsTable, ordersTable;
    private ClientBLL clientBLL;
    @FXML
    private TableColumn id_product, stoc;
    @FXML
    private TableColumn nameProduct;
    @FXML
    private TableColumn price;
    private ProductBLL productBLL;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TableColumn id_order, id_productOrder, id_clientOrder, cantity;
    private OrderBLL orderBLL;
    private FileWriter fileWriter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            fileWriter=new FileWriter("factura.txt",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        productBLL = new ProductBLL();
        orderBLL = new OrderBLL();
        id_product.setCellValueFactory(new PropertyValueFactory<>("id_product"));
        stoc.setCellValueFactory(new PropertyValueFactory<>("stoc"));
        nameProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        ArrayList<Product> products = productBLL.getProducts();
        ObservableList<Product> products1 = FXCollections.observableArrayList(products);
        if (products != null) {
            productsTable.setItems(products1);
        }
        clientBLL = new ClientBLL();
        id_client.setCellValueFactory(new PropertyValueFactory<>("id_client"));
        nameClient.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        ArrayList<Client> clients = clientBLL.getClients();
        ObservableList<Client> clients1 = FXCollections.observableArrayList(clients);
        if (clients != null) {
            clientsTable.setItems(clients1);
        }
        id_order.setCellValueFactory(new PropertyValueFactory<>("id_order"));
        id_productOrder.setCellValueFactory(new PropertyValueFactory<>("id_product"));
        id_clientOrder.setCellValueFactory(new PropertyValueFactory<>("id_client"));
        cantity.setCellValueFactory(new PropertyValueFactory<>("cantity"));
        ArrayList<Orders> orders = orderBLL.getOrders();
        ObservableList<Orders> orders1 = FXCollections.observableArrayList(orders);
        if (orders != null) {
            ordersTable.setItems(orders1);
        }
    }


    public void backBTNEvent(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mypack/MainPage.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        stage.show();
    }

    public void deleteOrderEvent(ActionEvent actionEvent) {
        Orders orders = (Orders) ordersTable.getSelectionModel().getSelectedItem();
        if (orders == null)
            showAlertWithHeaderText("nicio comanda selectat pt eliminare");
        else {
            orderBLL.deleteOrder(orders);
            ordersTable.getItems().remove(orders);
        }
    }

    private void showAlertWithHeaderText(String p) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WARNING");
        alert.setContentText(p);
        alert.showAndWait();
    }

    private boolean validateNumber(String s) {
        for (int i = 0; i < s.length(); ++i)
            if (!Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }

    public void addOrderOnEvent(ActionEvent actionEvent) {
        Product product = (Product) productsTable.getSelectionModel().getSelectedItem();
        Client client = (Client) clientsTable.getSelectionModel().getSelectedItem();
        int quantity = 0;
        if (quantityTextField.getText().length() > 0 && validateNumber(quantityTextField.getText()))
            quantity = Integer.parseInt(quantityTextField.getText());
        else showAlertWithHeaderText("cantitate introdusa eronat");

        if (product == null)
            showAlertWithHeaderText("niciun produs selectat spre comanda");
        else if (client == null) {
            showAlertWithHeaderText("niciun client selectat spre a face o comanda");
        } else if (quantity > product.getStoc() )
            showAlertWithHeaderText("indisponibila o asa mare cantitate");
        else if(quantity>0){
            orderBLL.insert(new Orders(1, client.getId_client(), product.getId_product(), quantity));
            product.setStoc(product.getStoc() - quantity);
            productBLL.updateProduct(product);
            ArrayList<Orders> orders = orderBLL.getOrders();
            ordersTable.setItems(FXCollections.observableArrayList(orders));
            ArrayList<Product> products = productBLL.getProducts();
            productsTable.setItems(FXCollections.observableArrayList(products));
            String facturi=client.getName()+" a comandat ";
            facturi+=quantity+ " din "+product.toString()+"\n";
            try {

                fileWriter.append(facturi);
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
