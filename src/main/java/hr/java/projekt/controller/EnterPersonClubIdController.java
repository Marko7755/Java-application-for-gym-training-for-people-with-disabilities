package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EnterPersonClubIdController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass()
                .getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextField personClubIdTextField;

    public static Person<LocalDate> person;
    public void enterPersonClubId(){

        if(personClubIdTextField.getText().isEmpty()){
            NewExerciseController.emptyField("TextBox", "Person Club ID");
        }
        else{
            try{
                Long clubId = Long.valueOf(personClubIdTextField.getText());
                List<Person<LocalDate>> persons = Database.getPersonsWithEverything();

                Optional<Person<LocalDate>> correspondingPersonOptional = persons.stream()
                        .filter(person -> person.getClubId().equals(clubId))
                        .findFirst();

                if(correspondingPersonOptional.isPresent()){
                    person = correspondingPersonOptional.get();
                    MenuBarController.checkPersonScreen();
                }
                else{
                    NoteATrainingController.noExistingUserFoundAlert(clubId);
                }
            }
            catch (NumberFormatException ex){
                invalidStringInput();
                personClubIdTextField.clear();
            }


        }

    }



    public static void invalidStringInput(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Wrong input");
        alert.setContentText("Text format may not be inputted, numerical format is needed!");
        alert.showAndWait();
    }

}
