package mypack.bll;

import javafx.scene.control.Alert;
import mypack.bll.validators.EmailValidator;
import mypack.bll.validators.Validator;
import mypack.dao.ClientDAO;
import mypack.model.Client;
import mypack.model.Product;

import java.util.ArrayList;

public class ClientBLL {
    private ClientDAO clientDAO;
    private Validator<Client> validators;

    public ClientBLL() {
        clientDAO=new ClientDAO();
        validators=new EmailValidator();
    }
    public ArrayList<Client> getClients(){
        return clientDAO.findAll();
    }
    public void deleteClient(Client client){
        clientDAO.delete(client);
    }
    public void insertClient(Client client){
        try {
            validators.validate(client);
            clientDAO.insert(client);
        }catch (Exception e){
            showAlertWithHeaderText("email invalid");
        }
    }
    private void showAlertWithHeaderText(String p) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("WARNING");
        alert.setContentText(p);
        alert.showAndWait();
    }
    public boolean updateClient(Client client){
        try {
            validators.validate(client);
            clientDAO.update(client);
            return true;
        }catch (Exception e){
            showAlertWithHeaderText("email invalid");
            return false;
        }
    }
}
