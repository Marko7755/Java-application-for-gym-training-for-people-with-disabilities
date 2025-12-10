package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.entity.enums.ExerciseBodyEffect;
import hr.java.projekt.exceptions.DuplicateExerciseException;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NewExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(NewExerciseController.class);

   @FXML
   private HBox menuBarHbox;

   public void setMenuBar(MenuBarController menuBarController){
       menuBarController.getMenuBar().getStylesheets().
               add(getClass().getResource("/style/centerMenus.css").toExternalForm());
       menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
   }

    @FXML
    private TextField exerciseNameTextField;
    @FXML
    private ComboBox<ExerciseBodyEffect> exerciseBodyEffectComboBox;
    @FXML
    private TextArea exerciseDescriptionTextArea;
    @FXML
    private TextField exerciseVideoLink;

    public void saveExercise(){

        boolean isEmpty = false;
        if(exerciseNameTextField.getText().isEmpty()){
            emptyField("TextField", "Exercise");
            isEmpty = true;
        }
        if(exerciseBodyEffectComboBox.getSelectionModel().isEmpty()){
            emptyField("ComboBox", "Exercise Body");
            isEmpty = true;
        }
        if(exerciseDescriptionTextArea.getText().isEmpty()){
            emptyField("TextArea", "Exercise Description");
            isEmpty = true;
        }
        if(exerciseVideoLink.getText().isEmpty()){
            emptyField("TextField", "Exercise Video Link");
            isEmpty = true;
        }

        if(!isEmpty){
            Optional<Integer> id = Exercise.getNextExerciseId();
            String name = exerciseNameTextField.getText();
            ExerciseBodyEffect bodyEffect = exerciseBodyEffectComboBox.getValue();
            String description = exerciseDescriptionTextArea.getText();
            String videoLink = exerciseVideoLink.getText();

            if(id.isPresent()){
                try{
                Database.checkDuplicateExerciseFromDatabase(name);

                Exercise newExercise = new Exercise(id.get(), name, bodyEffect, description, videoLink);
                Database.saveExercise(newExercise);
                    FileUtils.writeANewAttribute("Exercise", newExercise.getName());
                exerciseNameTextField.clear();
                exerciseBodyEffectComboBox.getSelectionModel().clearSelection();
                exerciseDescriptionTextArea.clear();
                exerciseVideoLink.clear();

                }
                catch (DuplicateExerciseException ex){
                    duplicateElement("Exercise" , ex);
                    String message = ex.getMessage();
                    logger.error(message, ex);
                }

            }
        }
    }

    public static void duplicateElement(String duplicateElement, RuntimeException ex) {
        Alert duplicateExerciseAlert = new Alert(Alert.AlertType.ERROR);
        duplicateExerciseAlert.setTitle("Error");
        duplicateExerciseAlert.setHeaderText("Duplicate " + duplicateElement + "!");
        duplicateExerciseAlert.setContentText(ex.getMessage());
        duplicateExerciseAlert.showAndWait();
    }

    public void initialize(){
        List<ExerciseBodyEffect> exerciseBodyEffects = Arrays.asList(ExerciseBodyEffect.values());
        exerciseBodyEffects = exerciseBodyEffects.stream()
                .filter(exerciseBodyEffect -> !exerciseBodyEffect.name().equals("EveryBodyEffect"))
                .collect(Collectors.toList());

        ObservableList<ExerciseBodyEffect> exerciseBodyEffectObservableList =
                FXCollections.observableList(exerciseBodyEffects);
        exerciseBodyEffectComboBox.setItems(exerciseBodyEffectObservableList);
    }

    public static void emptyField(String emptyFieldName, String emptyTextField) {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setTitle("Invalid input");
        warningAlert.setHeaderText("Empty " + emptyFieldName);
        warningAlert.setContentText(emptyFieldName + " " + emptyTextField + " must not be empty!");
        warningAlert.showAndWait();
    }

}
