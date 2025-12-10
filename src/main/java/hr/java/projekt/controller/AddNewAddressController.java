package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.enums.Hall;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.exceptions.DuplicateAddressException;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

public class AddNewAddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddNewAddressController.class);

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextField addressStreetTextField;
    @FXML
    private TextField addressCityTextField;
    @FXML
    private ChoiceBox<Hall> addressHallChoiceBox;

    public void initialize(){
        Hall[] hallValues = Hall.values();
        ObservableList<Hall> hallObservableList = FXCollections.observableList(Arrays.asList(hallValues));
        addressHallChoiceBox.setItems(hallObservableList);
    }

    public void addNewAddress(){

        boolean isEmpty = false;
        if(addressStreetTextField.getText().isEmpty()){
            NewExerciseController.emptyField("TextField", "Address Street");
            isEmpty = true;
        }
        if(addressCityTextField.getText().isEmpty()){
            NewExerciseController.emptyField("TextField", "Address City");
            isEmpty = true;
        }
        if(Optional.ofNullable(addressHallChoiceBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ChoiceBox", "Hall");
            isEmpty = true;
        }



        if(!isEmpty){
        String street = addressStreetTextField.getText();
        String city = addressCityTextField.getText();
        Hall hall = addressHallChoiceBox.getValue();

        Optional<Integer> nextAddressIdOptional = Address.getNextAddressId();
        Integer nextAddressId;
        if(nextAddressIdOptional.isPresent()){

            try{
                Database.checkDuplicateAddress(street, city);
                nextAddressId = nextAddressIdOptional.get();
                Address newAddress = new Address.Builder()
                        .setId(nextAddressId)
                        .setStreet(street)
                        .setCity(city)
                        .setHall(hall)
                        .build();
                Database.saveNewAddress(newAddress);
                FileUtils.writeANewAttribute("Address", newAddress.getStreet());
            }
            catch (DuplicateAddressException e){
                Alert duplicateAddressAlert = new Alert(Alert.AlertType.ERROR);
                duplicateAddressAlert.setTitle("Error");
                duplicateAddressAlert.setHeaderText("Duplicate address!");
                duplicateAddressAlert.setContentText(e.getMessage());
                duplicateAddressAlert.showAndWait();
                String message = e.getMessage();
                logger.error(message, e);
            }
        }
        }
    }


}
