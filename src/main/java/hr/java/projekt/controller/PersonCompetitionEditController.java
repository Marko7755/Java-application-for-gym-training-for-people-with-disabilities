package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.entity.model.Competition;
import hr.java.projekt.entity.model.Person;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonCompetitionEditController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }


    @FXML
    private TextField personClubIdTextField;
    private Person<LocalDate> correspondingPerson = null;
    public void checkAttributes(){

        if(personClubIdTextField.getText().isEmpty()){
            NewExerciseController.emptyField("TextField", "Person Club ID");
        }
        else{
            Long clubId = Long.valueOf(personClubIdTextField.getText());
            List<Person<LocalDate>> personList = Database.getPersonsWithEverything();
            Optional<Person<LocalDate>> correspondingPersonOptional = personList.stream()
                    .filter(person -> person.getClubId().equals(clubId))
                    .findFirst();

            if(correspondingPersonOptional.isPresent()){
                correspondingPerson = correspondingPersonOptional.get();
                Database.checkIfCompetitionFinished(correspondingPerson);

                if(correspondingPerson.getConditionsFulfilled().equals("NO")){
                    CheckPersonController.conditionsNotFulfilledAlert(correspondingPerson);
                }
                else if(correspondingPerson.getConditionsFulfilled().equals("YES")){
                    if(Optional.ofNullable(correspondingPerson.getCompetition()).isEmpty()){
                        Database.showAddCompetitionIfNotAlreadySetScreen(correspondingPerson);
                    }
                    else{
                        CheckPersonController.competitionInfoAlert(correspondingPerson);
                        chooseAttributeLabel.setVisible(true);
                        attributesToChangeListView.setVisible(true);
                    }

                }
            }
            else{
                NoteATrainingController.noExistingUserFoundAlert(clubId);
            }

        }
    }


    @FXML
    private Label chooseAttributeLabel;
    @FXML
    private ListView<String> attributesToChangeListView;
    @FXML
    private Label addressLabel;
    @FXML
    private ComboBox<Address> addressComboBox;
    @FXML
    private Label dateLabel;
    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private Label timeLabel;
    @FXML
    private TextField timeTextField;

    private final List<String> attributesToChange = new ArrayList<>();

    public void initialize(){
        chooseAttributeLabel.setVisible(false);
        attributesToChangeListView.setVisible(false);
        attributesToChangeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<String> competitionAttributes = List.of("Address", "Date", "Time");
        attributesToChangeListView.setItems(FXCollections.observableList(competitionAttributes));

        hideElements();

        attributesToChangeListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateElementsVisibility());

        List<Address> addressList = Database.getAddresses();
        addressComboBox.setItems(FXCollections.observableList(addressList));
        addressComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            EditAddressController.addressInfoAlert(addressComboBox);
        });
    }

    private void hideElements() {
        addressLabel.setVisible(false);
        dateLabel.setVisible(false);
        timeLabel.setVisible(false);
        addressComboBox.setVisible(false);
        dateDatePicker.setVisible(false);
        timeTextField.setVisible(false);
    }

    public void updateElementsVisibility(){
        if(personClubIdTextField.getText().isEmpty()){
            NewExerciseController.emptyField("TextField", "Club ID");
        }
        else{
            ObservableList<String> selectedItemsObservable =
                    attributesToChangeListView.getSelectionModel().getSelectedItems();
            attributesToChange.clear();

            hideElements();

            selectedItemsObservable.forEach(selectedItem -> {
                switch (selectedItem){
                    case "Address":
                        addressLabel.setVisible(true);
                        addressComboBox.setVisible(true);
                        attributesToChange.add(selectedItem);
                        break;

                    case "Date":
                        dateLabel.setVisible(true);
                        dateDatePicker.setVisible(true);
                        attributesToChange.add(selectedItem);
                        break;

                    case "Time":
                        timeLabel.setVisible(true);
                        timeTextField.setVisible(true);
                        attributesToChange.add(selectedItem);
                        break;

                    default:
                        break;

                }
            });

        }
    }

    public void editPersonCompetition(){
        if(personClubIdTextField.getText().isEmpty()){
            NewExerciseController.emptyField("TextField", "Club ID");
        }
        if(!NewCompetitionController.isTimeFormatValid(timeTextField.getText())) {
            NewCompetitionController.invalidTimeFormatAlert();
        }
        else{
            Alert confirmationAlert = getConfirmationAlert();
            Optional<ButtonType> buttonType = confirmationAlert.showAndWait();
            if(buttonType.isPresent()){
                if(!buttonType.get().getButtonData().isCancelButton()){
                    Integer addressId = null;
                    if(Optional.ofNullable(addressComboBox.getValue()).isPresent()){
                        addressId = addressComboBox.getValue().getId();
                    }
                    LocalDate chosenDate = dateDatePicker.getValue();
                    String chosenTime = timeTextField.getText();
                    String personName = correspondingPerson.getName();
                    String personSurname = correspondingPerson.getSurname();
                    Integer personId = correspondingPerson.getId();
                    Long personClubId = correspondingPerson.getClubId();
                    Database.editPersonCompetition(attributesToChange, addressId, chosenDate, chosenTime,
                            personName, personSurname, personId, personClubId);

                    Address newAddress = null;
                    if(Optional.ofNullable(addressComboBox.getValue()).isPresent()){
                        newAddress = addressComboBox.getValue();
                    }
                    FileUtils.writePersonCompetitionChangeToBinaryFile(attributesToChange, correspondingPerson, newAddress,
                            chosenDate, chosenTime);
                }
            }
        }
    }

    private Alert getConfirmationAlert() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("Are you sure you want to Edit a Competition?");
        return confirmationAlert;
    }


}
