package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.changes.ChangesMade;
import hr.java.projekt.entity.login.User;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.fileUtils.FileUtils;
import hr.java.projekt.threads.EditExerciseThread;
import hr.java.projekt.threads.ReadExercisesFromBinaryFileThread;
import hr.java.projekt.threads.WriteExerciseToBinaryFileThread;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EditExerciseController {


    @FXML
    private HBox menuBarHbox;
    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets()
                .add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextArea lastModificationTextArea;
    @FXML
    private ChoiceBox<Exercise> exerciseNameChoiceBox;
    @FXML
    private ListView<String> attributeToChangeListView;
    @FXML
    private Label editExerciseNameLabel;
    @FXML
    private Label editExerciseBodyEffectLabel;
    @FXML
    private Label editExerciseDescriptionLabel;
    @FXML
    private Label editExerciseVideoLinkLabel;
    @FXML
    private TextField editExerciseNameTextField;
    @FXML
    private TextField editExerciseBodyEffectTextField;
    @FXML
    private TextArea editExerciseDescriptionTextArea;
    @FXML
    private TextField editExerciseVideoLinkTextField;

    private final List<String> exerciseAttributesToChangeList = new ArrayList<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);



    public void initialize(){

        List<Exercise> sortedExercise = DeleteExerciseController.getAndSortExercises();

        ObservableList<Exercise> observableExerciseNameList = FXCollections.observableList(sortedExercise);

        exerciseNameChoiceBox.setItems(observableExerciseNameList);


        attributeToChangeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<String> attibutesToChangeList = List.of("Name", "Body Effect", "Description", "Video link");
        ObservableList<String> attributesToChangeObservableList = FXCollections.observableList(attibutesToChangeList);
        attributeToChangeListView.setItems(attributesToChangeObservableList);

        hideLabelAndTextField();

        //za svaki selectani item/items u list view-u odradi updateTextFieldsVisibility()
        attributeToChangeListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> updateTextFieldsVisibility());


        scheduler.scheduleAtFixedRate(this::writeChangesToTextArea, 0, 10, TimeUnit.SECONDS);

    }

    private void writeChangesToTextArea() {
        ReadExercisesFromBinaryFileThread readExerciseThread = new ReadExercisesFromBinaryFileThread();
        Thread thread = new Thread(readExerciseThread);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        List<ChangesMade<?>> changesMadeList = readExerciseThread.getChangesMadeList();

        //List<ChangesMade<?>> changesMadeList = FileUtils.readExerciseChangesFromBinaryFile();

        StringBuilder textAreaText = new StringBuilder();
        for(ChangesMade<?> change : changesMadeList) {
            textAreaText.append(change.toCustomString()).append("\n");
        }

        Platform.runLater(() -> lastModificationTextArea.setText("Last change:\n" + textAreaText));
    }

    private void updateTextFieldsVisibility(){

        if(Optional.ofNullable(exerciseNameChoiceBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ChoiceBox ", "Exercise attributes");
        }
        else{
        ObservableList<String> selectedItems = attributeToChangeListView.getSelectionModel().getSelectedItems();
        exerciseAttributesToChangeList.clear();

        hideLabelAndTextField();

        //za multiple selectanih itema u list view-u, tj za svaki selectani radi switch-case
        selectedItems.forEach(selectedItem -> {
                    switch (selectedItem){
                        case "Name":
                            editExerciseNameLabel.setVisible(true);
                            editExerciseNameTextField.setVisible(true);
                            editExerciseNameTextField.setText(exerciseNameChoiceBox.getValue().getName());
                            exerciseAttributesToChangeList.add(selectedItem);
                            break;

                        case "Body Effect":
                            editExerciseBodyEffectLabel.setVisible(true);
                            editExerciseBodyEffectTextField.setVisible(true);
                            editExerciseBodyEffectTextField.setText(exerciseNameChoiceBox.getValue()
                                    .getBodyEffect().toString());
                            exerciseAttributesToChangeList.add(selectedItem);
                            break;

                        case "Description":
                            editExerciseDescriptionLabel.setVisible(true);
                            editExerciseDescriptionTextArea.setVisible(true);
                            editExerciseDescriptionTextArea.setText(exerciseNameChoiceBox.getValue().getDescription());
                            exerciseAttributesToChangeList.add(selectedItem);
                            break;

                        case "Video link":
                            editExerciseVideoLinkLabel.setVisible(true);
                            editExerciseVideoLinkTextField.setVisible(true);
                            editExerciseVideoLinkTextField.setText(exerciseNameChoiceBox.getValue().getVideoLink());
                            exerciseAttributesToChangeList.add(selectedItem);
                            break;

                        default:
                            break;
                    }
                });

        }
    }


        public void checkExerciseAttributes(){
            if(Optional.ofNullable(exerciseNameChoiceBox.getValue()).isEmpty()){
                NewExerciseController.emptyField("ChoiceBox ", "Exercise attributes");
            }
            else{
                Alert exerciseInfoAlert = new Alert(Alert.AlertType.INFORMATION);
                exerciseInfoAlert.setTitle("Info");
                exerciseInfoAlert.setHeaderText("Current exercise attributes");
                exerciseInfoAlert.setContentText("Name: " + exerciseNameChoiceBox.getValue().getName()
                        +" \nBody effect: " + exerciseNameChoiceBox.getValue().getBodyEffect()
                        + " \nDescription: " + exerciseNameChoiceBox.getValue().getDescription()
                        + " \nVideo link: " + exerciseNameChoiceBox.getValue().getVideoLink());
                exerciseInfoAlert.showAndWait();
            }
        }

    public void editAnExercise(){

        if(Optional.ofNullable(exerciseNameChoiceBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ChoiceBox ", "Exercise attributes");
        }

        else{
            Alert confirmationAlert = getConfirmationAlert();
            Optional<ButtonType> alertValueOptional = confirmationAlert.showAndWait();

            if(alertValueOptional.isPresent()){
                if(!alertValueOptional.get().getButtonData().isCancelButton()){
                    String chosenExerciseName = exerciseNameChoiceBox.getValue().getName();
                    String newExerciseName = editExerciseNameTextField.getText();
                    String newExerciseBodyEffect = editExerciseBodyEffectTextField.getText();
                    String newExerciseDescription = editExerciseDescriptionTextArea.getText();
                    String newExerciseVideoLink = editExerciseVideoLinkTextField.getText();
                    Integer chosenExerciseId = exerciseNameChoiceBox.getValue().getId();

                    /*Database.editExercise(chosenExerciseName, chosenExerciseId,
                            exerciseAttributesToChangeList, newExerciseName, newExerciseBodyEffect,
                            newExerciseDescription, newExerciseVideoLink);*/

                    EditExerciseThread editExerciseThread = new EditExerciseThread(chosenExerciseName,
                            chosenExerciseId, exerciseAttributesToChangeList, newExerciseName,
                            newExerciseBodyEffect, newExerciseDescription, newExerciseVideoLink);
                    Thread editExercise = new Thread(editExerciseThread);
                    editExercise.start();



                    Exercise exerciseFromChoiceBox = exerciseNameChoiceBox.getValue();
                    /* FileUtils.writeExerciseChangesToBinaryFile(exerciseAttributesToChangeList, exerciseFromChoiceBox,
                            newExerciseName, newExerciseBodyEffect, newExerciseDescription, newExerciseVideoLink);*/
                    WriteExerciseToBinaryFileThread writeExerciseThread = new WriteExerciseToBinaryFileThread(
                            exerciseAttributesToChangeList, exerciseFromChoiceBox,
                            newExerciseName, newExerciseBodyEffect, newExerciseDescription, newExerciseVideoLink);
                    Thread writeThread = new Thread(writeExerciseThread);
                    writeThread.start();



                }
            }
        }

    }

    private Alert getConfirmationAlert() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("Are you sure you want to Edit an " +
                exerciseNameChoiceBox.getValue().getName() + " exercise?");
        return confirmationAlert;
    }


    private void hideLabelAndTextField() {
        editExerciseNameLabel.setVisible(false);
        editExerciseBodyEffectLabel.setVisible(false);
        editExerciseDescriptionLabel.setVisible(false);
        editExerciseVideoLinkLabel.setVisible(false);
        editExerciseNameTextField.setVisible(false);
        editExerciseBodyEffectTextField.setVisible(false);
        editExerciseDescriptionTextArea.setVisible(false);
        editExerciseVideoLinkTextField.setVisible(false);
    }

}
