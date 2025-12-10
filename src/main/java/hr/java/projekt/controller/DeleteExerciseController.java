package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeleteExerciseController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private ComboBox<Exercise> exerciseComboBox;

    public void initialize(){
        List<Exercise> sortedExercise = getAndSortExercises();
        ObservableList<Exercise> observableExerciseList = FXCollections.observableList(sortedExercise);
        exerciseComboBox.setItems(observableExerciseList);
    }

    public static List<Exercise> getAndSortExercises() {
        List<Exercise> exerciseList = Database.getExercisesFromDatabase();
        return exerciseList.stream()
                .sorted((e1, e2) -> e2.getId().compareTo(e1.getId()))
                .collect(Collectors.toList());
    }

    public void deleteAnExercise(){
        if(Optional.ofNullable(exerciseComboBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ComboBox ", "Exercise name");
        }
        else{
            Exercise exerciseToDelete = exerciseComboBox.getValue();
            Alert alertConfirmation = getConfirmationAlert("Exercise", exerciseToDelete.getName(),
                    "delete");
            Optional<ButtonType> buttonType = alertConfirmation.showAndWait();
            if(buttonType.isPresent()){
                if(!buttonType.get().getButtonData().isCancelButton()){
                    Database.deleteAnExercise(exerciseToDelete);
                }
            }
        }
    }


    public static Alert getConfirmationAlert(String attributeToDelete, String attributeToDeleteName, String whatToDo) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("Are you sure you want to " + whatToDo + " " +
                attributeToDeleteName + " " + attributeToDelete + "?");
        return confirmationAlert;
    }

}
