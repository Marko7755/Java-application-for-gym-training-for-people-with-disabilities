package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;

public class DeleteAddressController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass()
                .getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private ComboBox<Address> addressComboBox;


    public void initialize(){
        List<Address> addressList = Database.getAddresses();
        addressComboBox.setItems(FXCollections.observableList(addressList));
    }

    public void deleteAnAddress(){
        if(Optional.ofNullable(addressComboBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ComboBox", "Address ComboBox");
        }
        else{
            Address addressToDelete = addressComboBox.getValue();
            Alert areYouSureAlert = DeleteExerciseController.
                    getConfirmationAlert("Address", addressToDelete.getStreet(), "delete");
            Optional<ButtonType> buttonType = areYouSureAlert.showAndWait();
            if(buttonType.isPresent()){
                if(!buttonType.get().getButtonData().isCancelButton()){
                    Database.deleteAddress(addressToDelete);
                }
            }

        }
    }


}
