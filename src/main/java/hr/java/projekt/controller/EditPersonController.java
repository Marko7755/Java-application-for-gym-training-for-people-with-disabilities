package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Person;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditPersonController {

    @FXML
    private HBox menuBarHbox;

    private static final Logger logger = LoggerFactory.getLogger(EditPersonController.class);

    public void setMenuBar(MenuBarController menuBarController) {
        menuBarController.getMenuBar().getStylesheets()
                .add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextField personClubIdTextField;
    @FXML
    private Label personInfoLabel;
    @FXML
    private ListView<String> attributesToChangeListView;
    @FXML
    private Label personNameLabel;
    @FXML
    private TextField personNameTextField;
    @FXML
    private Label personSurnameLabel;
    @FXML
    private TextField personSurnameTextField;

    @FXML
    private Label membershipDateIdLabel;
    @FXML
    private DatePicker membershipDateDatePicker;


    private final List<String> attributesToChangeList = new ArrayList<>();

    private Person<LocalDate> person;

    public void initialize() {
        personInfoLabel.getStylesheets().add(getClass().getResource("/style/personInfoLabel.css").toExternalForm());
        personInfoLabel.setVisible(false);

        attributesToChangeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<String> attributes = List.of("Name", "Surname", "Membership Date");
        attributesToChangeListView.setItems(FXCollections.observableList(attributes));

        hideLabelsAndTextFields();

        attributesToChangeListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateTextFieldsVisibility());

        personClubIdTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() == 5) {
                try {
                    Optional<Person<LocalDate>> correspondingPersonOptional =
                            Database.getPersonById(Long.valueOf(newValue));
                    correspondingPersonOptional.ifPresentOrElse(
                            localDatePerson -> {
                                person = localDatePerson;
                                Platform.runLater(() -> {
                                    personInfoLabel.setVisible(true);
                                    personInfoLabel.setText("Name: " + person.getName() + "\n" +
                                            "Surname: " + person.getSurname() + "\n" +
                                            "Membership Date: " + person.getMembershipDate());
                                });
                            },
                            () -> {
                                person = null;
                                Platform.runLater(() -> {
                                    NoteATrainingController.noExistingUserFoundAlert
                                            (Long.valueOf(personClubIdTextField.getText()));
                                    personInfoLabel.setText("");
                                    personClubIdTextField.clear();
                                });
                            });
                } catch (NumberFormatException ex) {
                    String message = ex.getMessage();
                    logger.error(message, ex);
                    Platform.runLater(() -> {
                        EnterPersonClubIdController.invalidStringInput();
                        personInfoLabel.setText("");
                        personClubIdTextField.clear();
                        person = null;
                    });
                }

            }
        });

    }

    public void updateTextFieldsVisibility() {
        if (personClubIdTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("ChoiceBox", "Person ChoiceBox");
        } else {
            ObservableList<String> selectedItemsObservable =
                    attributesToChangeListView.getSelectionModel().getSelectedItems();
            attributesToChangeList.clear();

            hideLabelsAndTextFields();

            selectedItemsObservable.forEach(selectedItem -> {
                switch (selectedItem) {
                    case "Name":
                        personNameLabel.setVisible(true);
                        personNameTextField.setVisible(true);
                        personNameTextField.setText(person.getName());
                        attributesToChangeList.add(selectedItem);
                        break;

                    case "Surname":
                        personSurnameLabel.setVisible(true);
                        personSurnameTextField.setVisible(true);
                        personSurnameTextField.setText(person.getSurname());
                        attributesToChangeList.add(selectedItem);
                        break;


                    case "Membership Date":
                        membershipDateIdLabel.setVisible(true);
                        membershipDateDatePicker.setVisible(true);
                        membershipDateDatePicker.setValue(person.getMembershipDate());
                        attributesToChangeList.add(selectedItem);
                        break;

                    default:
                        break;

                }
            });

        }
    }


    public void editPerson() {
        if (personClubIdTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("TextField", "Club ID");
        }
        else if (person == null) {
            NoteATrainingController.noExistingUserFoundAlert(Long.valueOf(personClubIdTextField.getText()));
        }
        else {
            Alert confirmationAlert = getConfirmationAlert();
            Optional<ButtonType> buttonTypeOptional = confirmationAlert.showAndWait();
            if (buttonTypeOptional.isPresent()) {
                if (!buttonTypeOptional.get().getButtonData().isCancelButton()) {
                    Integer id = person.getId();
                    String name = personNameTextField.getText();
                    String surname = personSurnameTextField.getText();
                    LocalDate membershipDate = membershipDateDatePicker.getValue();
                    Database.editPerson(attributesToChangeList, id, name, surname, membershipDate);

                    String memberShipDateString = " ";
                    if (Optional.ofNullable(membershipDateDatePicker.getValue()).isPresent()) {
                        memberShipDateString = membershipDate.toString();
                    }

                    FileUtils.writeEditPersonToBinaryFile(attributesToChangeList, person,
                            name, surname, memberShipDateString);

                }
            }
            //}
        }

    }

    public void hideLabelsAndTextFields() {
        personNameLabel.setVisible(false);
        personNameTextField.setVisible(false);
        personSurnameLabel.setVisible(false);
        personSurnameTextField.setVisible(false);
        membershipDateIdLabel.setVisible(false);
        membershipDateDatePicker.setVisible(false);
    }


    private Alert getConfirmationAlert() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("Are you sure you want to Edit " +
                person.getName() + " " + person.getSurname() + "?");
        return confirmationAlert;
    }


}
