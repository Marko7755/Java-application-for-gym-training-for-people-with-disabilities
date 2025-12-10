package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditAddressController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets()
                .add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private ComboBox<Address> addressComboBox;
    @FXML
    private ListView<String> attributesToChangeListView;
    @FXML
    private Label streetLabel;
    @FXML
    private TextField streetTextField;
    @FXML
    private Label cityLabel;
    @FXML
    private TextField cityTextField;
    @FXML
    private Label hallLabel;
    @FXML
    private TextField hallTextField;

    private final List<String> attributesToChangeList = new ArrayList<>();


    public void initialize(){
        List<Address> addressList = Database.getAddresses();
        addressComboBox.setItems(FXCollections.observableList(addressList));

        attributesToChangeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<String> attributes = List.of("Street", "City", "Hall");
        attributesToChangeListView.setItems(FXCollections.observableList(attributes));

        hideLabelsAndTextFields();

        attributesToChangeListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateTextFieldsVisibility());


    }

    public void checkAddressAttributes(){
        if(Optional.ofNullable(addressComboBox.getValue()).isEmpty()) {
            NewExerciseController.emptyField("ComboBox ", "Address attributes");
            }
            else{
            addressInfoAlert(addressComboBox);
        }
        }

    public static void addressInfoAlert(ComboBox<Address> addressComboBox) {
        Alert exerciseInfoAlert = new Alert(Alert.AlertType.INFORMATION);
        exerciseInfoAlert.setTitle("Info");
        exerciseInfoAlert.setHeaderText("Current address attributes");
        exerciseInfoAlert.setContentText("Street: " + addressComboBox.getValue().getStreet()
                +" \nCity: " + addressComboBox.getValue().getCity()
                + " \nHall: " + addressComboBox.getValue().getHall());
        exerciseInfoAlert.showAndWait();
    }

    public void updateTextFieldsVisibility(){
        if(Optional.ofNullable(addressComboBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ComboBox", "Address ComboBox");
        }
        else{
            ObservableList<String> selectedItemsObservable =
                    attributesToChangeListView.getSelectionModel().getSelectedItems();
            attributesToChangeList.clear();

            hideLabelsAndTextFields();

            selectedItemsObservable.forEach(selectedItem -> {
                switch (selectedItem){
                    case "Street":
                        streetLabel.setVisible(true);
                        streetTextField.setVisible(true);
                        streetTextField.setText(addressComboBox.getValue().getStreet());
                        attributesToChangeList.add(selectedItem);
                        break;

                    case "City":
                        cityLabel.setVisible(true);
                        cityTextField.setVisible(true);
                        cityTextField.setText(addressComboBox.getValue().getCity());
                        attributesToChangeList.add(selectedItem);
                        break;

                    case "Hall":
                        hallLabel.setVisible(true);
                        hallTextField.setVisible(true);
                        hallTextField.setText(addressComboBox.getValue().getHall().toString());
                        attributesToChangeList.add(selectedItem);
                        break;

                    default:
                        break;

                }
            });

        }
    }



        public void editAddress(){
            if(Optional.ofNullable(addressComboBox.getValue()).isEmpty()){
                NewExerciseController.emptyField("ComboBox", "Address");
            }
            else{
                Alert confirmationAlert = getConfirmationAlert();
                Optional<ButtonType> buttonType = confirmationAlert.showAndWait();
                if(buttonType.isPresent()){
                    if(!buttonType.get().getButtonData().isCancelButton()){
                        String street = addressComboBox.getValue().getStreet();
                        Integer addressId = addressComboBox.getValue().getId();
                        String newStreet = streetTextField.getText();
                        String newCity = cityTextField.getText();
                        String newHall = hallTextField.getText();
                        Database.editAnAddress(attributesToChangeList, street, addressId, newStreet, newCity, newHall);

                        Address oldAddress = addressComboBox.getValue();
                        FileUtils.writeAddressEditChangeToBinaryFile(attributesToChangeList, oldAddress, newStreet,
                                newCity, newHall);

                    }
                }
            }
        }



        public void hideLabelsAndTextFields(){
            streetLabel.setVisible(false);
            streetTextField.setVisible(false);
            cityLabel.setVisible(false);
            cityTextField.setVisible(false);
            hallLabel.setVisible(false);
            hallTextField.setVisible(false);
        }


    private Alert getConfirmationAlert() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("Are you sure you want to Edit an " +
                addressComboBox.getValue().getStreet() + " address?");
        return confirmationAlert;
    }

    }


