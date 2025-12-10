package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.entity.enums.ExerciseBodyEffect;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExerciseSearchController {
    private static final Logger logger = LoggerFactory.getLogger(ExerciseSearchController.class);

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets()
                .add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextField exerciseNameTextField;
    @FXML
    private ComboBox<ExerciseBodyEffect> exerciseBodyEffectComboBox;
    @FXML
    private TableView<Exercise> exerciseTableView;
    @FXML
    private TableColumn<Exercise, String> exerciseIdTableColumn;
    @FXML
    private TableColumn<Exercise, String> exerciseNameTableColumn;
    @FXML
    private TableColumn<Exercise, String> exerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<Exercise, Hyperlink> exerciseVideoLinkTableColumn;
    @FXML
    private TableColumn<Exercise, String> exerciseDescriptionTableColumn;

    public void searchExercise(){
        List<Exercise> searchedExercisesFromDatabase =
                Database.getSearchedExercise(exerciseNameTextField.getText(),
                        exerciseBodyEffectComboBox.getValue());
        searchedExercisesFromDatabase = searchedExercisesFromDatabase.stream()
                .sorted((s1, s2) -> s2.getId().compareTo(s1.getId()))
                .collect(Collectors.toList());

        ObservableList<Exercise> searchedExercsisesFromDatabaseObservableList =
                FXCollections.observableList(searchedExercisesFromDatabase);

        exerciseTableView.setItems(searchedExercsisesFromDatabaseObservableList);

    }

    public void initialize(){
        exerciseIdTableColumn.setCellValueFactory(exerciseStringCellDataFeatures ->
                new ReadOnlyStringWrapper(exerciseStringCellDataFeatures.getValue().getId().toString()));

        exerciseNameTableColumn.setCellValueFactory(exerciseStringCellDataFeatures ->
                new ReadOnlyStringWrapper(exerciseStringCellDataFeatures.getValue().getName()));

       exerciseBodyEffectTableColumn.setCellValueFactory(exerciseStringCellDataFeatures ->
               new ReadOnlyStringWrapper(exerciseStringCellDataFeatures.getValue().getBodyEffect().name()));

        exerciseDescriptionTableColumn.setCellValueFactory(exerciseStringCellDataFeatures ->
                new ReadOnlyStringWrapper(exerciseStringCellDataFeatures.getValue().getDescription()));

        exerciseVideoLinkTableColumn.setCellValueFactory(exerciseHyperlinkCellDataFeatures ->{
            Exercise exercise = exerciseHyperlinkCellDataFeatures.getValue();
            return getHyperlinkObservableValue(exercise, logger);
        });


        ObservableList<ExerciseBodyEffect> exerciseBodyEffectObservableList =
                FXCollections.observableList(Arrays.asList(ExerciseBodyEffect.values()));
        exerciseBodyEffectComboBox.setItems(exerciseBodyEffectObservableList);

    }

    public static ObservableValue<Hyperlink> getHyperlinkObservableValue(Exercise exercise, Logger logger) {
        Hyperlink hyperlink = new Hyperlink(exercise.getName());

        hyperlink.setOnAction(event -> {
            // Kad se stisne na link, video se otvori
            String videoLink = exercise.getVideoLink();
            try {
                // Osigurati da je link validan
                java.awt.Desktop.getDesktop().browse(new java.net.URI(videoLink));
            } catch (java.io.IOException | java.net.URISyntaxException ex) {
                String message = "Error while trying to open a link!";
                logger.error(message, ex);
                System.out.println(message);
            }
        });

        return new ReadOnlyObjectWrapper<>(hyperlink);
    }



}
