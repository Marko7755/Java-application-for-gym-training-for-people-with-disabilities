package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CheckCompetitionConditions {

    @FXML
    private HBox menuBarHbox;
    private static final Logger logger = LoggerFactory.getLogger(NoteATrainingController.class);

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().
                add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }


    @FXML
    private TextField personClubIdTextField;

    public void checkCompetitionConditions(){
        //List<Person<LocalDate>> persons = Database.setAndGetPersonsWithTheirCompletedTrainings();

        if(personClubIdTextField.getText().isEmpty()){
            NewExerciseController.emptyField("TextField", "Club ID");
        }
        else{
            try{
                Long clubId = Long.valueOf(personClubIdTextField.getText());
                /*Optional<Person<LocalDate>> correspondingPersonOptional = persons.stream()
                        .filter(person -> person.getClubId().equals(clubId))
                        .findFirst();*/
                /*Optional<Person<LocalDate>> correspondingPersonOptional = Database.getPersonById(clubId);*/


                List<Person<LocalDate>> persons = Database.getPersonsWithEverything();

                Optional<Person<LocalDate>> correspondingPersonOptional = persons.stream()
                        .filter(person -> person.getClubId().equals(clubId))
                        .findFirst();

                if(correspondingPersonOptional.isPresent()){
                    Person<LocalDate> correspondingPerson = correspondingPersonOptional.get();
                    Database.checkIfCompetitionFinished(correspondingPerson);

                    if(correspondingPerson.getConditionsFulfilled().equals("NO")){
                        competitionConditionsNotFulfilledAlert(correspondingPerson);
                    }
                    else if(correspondingPerson.getConditionsFulfilled().equals("YES")){
                        Database.checkIfCompetitionAlreadyExists(correspondingPerson);
                    }

                }
                else{
                    NoteATrainingController.noExistingUserFoundAlert(clubId);
                }


            }
            catch (NumberFormatException ex){
                logger.error(ex.getMessage());
                EnterPersonClubIdController.invalidStringInput();
                personClubIdTextField.clear();
            }
        }
    }


    private void competitionConditionsNotFulfilledAlert(Person<LocalDate> person){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Competition Conditions not fulfilled");
        alert.setContentText("Competition conditions for " +
                person.getName() + " " + person.getSurname() + " are not fulfilled! \n" +
                "Conditions are minimum 15 trainings, but " + person.getName() + " " +
                person.getSurname() + " has "+  person.getCompletedTrainings() + "!");
        alert.showAndWait();
    }



}
