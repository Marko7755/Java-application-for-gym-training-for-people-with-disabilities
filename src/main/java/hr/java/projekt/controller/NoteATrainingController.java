package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class NoteATrainingController {

    @FXML
    private HBox menuBarHbox;

    private static final Logger logger = LoggerFactory.getLogger(NoteATrainingController.class);

    public void setMenuBar(MenuBarController menuBarController) {
        menuBarController.getMenuBar().getStylesheets().
                add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }


    @FXML
    private Label personNameLabel;

    @FXML
    private TextField personClubIdTextField;

    private Person<LocalDate> correspondingPerson;

    List<Person<LocalDate>> persons = Database.setAndGetPersonsWithTheirCompletedTrainings();

    public void checkPerson() {
            if (personClubIdTextField.getText().length() == 5) {
                try {
                    Long clubId = Long.valueOf(personClubIdTextField.getText());

                    Optional<Person<LocalDate>> correspondingPersonOptional =
                            persons.stream()
                                    .filter(person -> person.getClubId().equals(clubId))
                                    .findFirst();

                    if (correspondingPersonOptional.isPresent()) {
                        correspondingPerson = correspondingPersonOptional.get();

                        personNameLabel.setText(correspondingPerson.getName() + " " + correspondingPerson.getSurname() +
                                "\nTrainings completed: " + correspondingPerson.getCompletedTrainings());
                        personNameLabel.getStylesheets().add(getClass()
                                .getResource("/style/personInfoLabel.css").toExternalForm());
                        personNameLabel.setVisible(true);
                    } else {
                        personNameLabel.setVisible(false);
                        noExistingUserFoundAlert(clubId);



                    }
                } catch (NumberFormatException ex) {
                    String message = ex.getMessage();
                    logger.error(message, ex);
                    EnterPersonClubIdController.invalidStringInput();
                }
            }

    }

    public static void noExistingUserFoundAlert(Long clubId) {
        Alert noExistingUserAlert = new Alert(Alert.AlertType.ERROR);
        noExistingUserAlert.setTitle("Error");
        noExistingUserAlert.setHeaderText("No existing user found!");
        noExistingUserAlert.setContentText("No existing user corresponds to " + clubId + " Club ID!");
        noExistingUserAlert.showAndWait();
    }

    public void noteATraining() {

        if (personClubIdTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("TextField", "Club ID");
        } else {

            Long clubId = Long.valueOf(personClubIdTextField.getText());

            Optional<Person<LocalDate>> correspondingPersonOptional =
                    persons.stream()
                            .filter(person -> person.getClubId().equals(clubId))
                            .findFirst();

            if (correspondingPersonOptional.isPresent()) {
                correspondingPerson = correspondingPersonOptional.get();
                personNameLabel.setText(correspondingPerson.getName() + " " + correspondingPerson.getSurname() +
                        "\nTrainings completed: " + (correspondingPerson.getCompletedTrainings()+1));

                if (correspondingPerson.getCompletedTrainings() == 20) {
                    maxTrainingsAlert();
                } else {
                    correspondingPerson.addNumberOfTrainings();
                    Database.writeATraining(correspondingPerson);
                    completedTrainingsAlert();
                }
            } else {
                noExistingUserFoundAlert(clubId);
                personNameLabel.setVisible(false);
                personClubIdTextField.clear();
            }

        }
    }

    private void completedTrainingsAlert() {
        Alert completedTrainingsAlert = new Alert(Alert.AlertType.INFORMATION);
        completedTrainingsAlert.setTitle("Info");
        completedTrainingsAlert.setHeaderText("Saving a new Training and number of trainings completed");
        completedTrainingsAlert.setContentText("Saving a new Training was successful!" +
                "\nPerson " + correspondingPerson.getName() + " " + correspondingPerson.getSurname() + " has " +
                correspondingPerson.getCompletedTrainings() + " trainings completed!");
        completedTrainingsAlert.showAndWait();
    }

    private void maxTrainingsAlert() {
        Alert maxTrainingsCompletedAlert = new Alert(Alert.AlertType.WARNING);
        maxTrainingsCompletedAlert.setTitle("Warning");
        maxTrainingsCompletedAlert.setHeaderText("Max completed trainings");
        maxTrainingsCompletedAlert.setContentText("Person " + correspondingPerson.getName() + " "
                + correspondingPerson.getSurname() + " has max completed trainings for this month!");
        maxTrainingsCompletedAlert.showAndWait();
    }


    public void initialize() {
        personNameLabel.setVisible(false);
        personClubIdTextField.textProperty().addListener((observable, oldValue, newValue) -> checkPerson());
    }


}
