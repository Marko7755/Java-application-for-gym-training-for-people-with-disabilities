package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.entity.model.Person;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DeletePersonController {

    @FXML
    private HBox menuBarHbox;
    private static final Logger logger = LoggerFactory.getLogger(DeletePersonController.class);
    public void setMenuBar(MenuBarController menuBarController) {
        menuBarController.getMenuBar().getStylesheets().add(getClass()
                .getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }


    private Person<LocalDate> person;

    @FXML
    private TextField personTextField;
    @FXML
    private Label personInfoLabel;


    public void checkPerson() {
        if (personTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("ComboBox", "Person");
        } else {
            try {
                Optional<Person<LocalDate>> chosenPersonOpt =
                        Database.getPersonById(Long.valueOf(personTextField.getText()));

                if (chosenPersonOpt.isPresent()) {
                    person = chosenPersonOpt.get();

                    personInfoLabel.setVisible(true);
                    personInfoLabel.setText("Name: " + person.getName() + "\n" +
                            "Surname: " + person.getSurname() + "\n" +
                            "Membership Date: " + person.getMembershipDate());
                } else {
                    NoteATrainingController.noExistingUserFoundAlert(Long.valueOf(personTextField.getText()));
                }
            } catch (NumberFormatException ex) {
                String message = ex.getMessage();
                logger.error(message, ex);
                EnterPersonClubIdController.invalidStringInput();
            }
        }
    }

    public void deletePerson() {
        if (personTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("ComboBox", "Person");
        } else {
            Alert getConfirmationAlert =
                    DeleteExerciseController.getConfirmationAlert(person.getName() + " " +
                            person.getSurname(), "person", "delete");
            Optional<ButtonType> buttonClicked = getConfirmationAlert.showAndWait();
            if (buttonClicked.isPresent()) {
                if (!buttonClicked.get().getButtonData().isCancelButton()) {
                    Database.deleteChosenPerson(person);
                }
            }


        }
    }


}
