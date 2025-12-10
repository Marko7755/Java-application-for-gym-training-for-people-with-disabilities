package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Day;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.entity.model.ExerciseSetAndRepForEachPerson;
import hr.java.projekt.entity.model.Person;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChangeOrAddPersonExerciseController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController) {
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextField personClubIdTextField;
    @FXML
    private ComboBox<Day> daysComboBox;
    @FXML
    private RadioButton changeExerciseRadioButton;
    @FXML
    private RadioButton addExerciseRadioButton;
    @FXML
    private Label exerciseToChangeLabel;
    @FXML
    private Label exerciseListLabel;
    @FXML
    private ComboBox<ExerciseSetAndRepForEachPerson> exerciseToChangeComboBox;
    @FXML
    private ComboBox<Exercise> exerciseListComboBox;
    @FXML
    private Label newExerciseLabel;
    @FXML
    private ComboBox<Exercise> newExerciseComboBox;
    @FXML
    private Label newSetsLabel;
    @FXML
    private TextField newSetsTextField;
    @FXML
    private Label newRepsLabel;
    @FXML
    private TextField newRepsTextField;
    @FXML
    private Button changeOrAddExerciseButton;

    private Person<LocalDate> correspondingPerson = null;
    private Long personClubId;

    public void initialize() {

        ToggleGroup group = new ToggleGroup();
        changeExerciseRadioButton.setToggleGroup(group);
        addExerciseRadioButton.setToggleGroup(group);


        exerciseListLabel.setVisible(false);
        exerciseListComboBox.setVisible(false);
        hideElements(false);

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == changeExerciseRadioButton) {
                changeOrAddExerciseButton.setText("Change");
                hideElements(true);
                exerciseListLabel.setVisible(false);
                exerciseListComboBox.setVisible(false);

                try {
                    if (personClubIdTextField.getText().isEmpty()) {
                        NewExerciseController.emptyField("Person Club ID", "TextField");
                        changeExerciseRadioButton.setSelected(false);
                    } else {
                        personClubId = Long.valueOf(personClubIdTextField.getText());
                        Optional<Person<LocalDate>> personByIdOptional = Database.getPersonById(personClubId);
                        if (personByIdOptional.isPresent()) {
                            correspondingPerson = personByIdOptional.get();

                            daysComboBox.setItems(FXCollections.observableList(Database.getDays()));
                            daysComboBox.getSelectionModel().selectedItemProperty()
                                    .addListener((selectedDay, oldDay, newDay) -> {
                                        if (Optional.ofNullable(newDay).isPresent()) {
                                            exerciseToChangeComboBox.setItems(FXCollections.observableList(
                                                    correspondingPerson.getExerciseSetAndRepMap().get(newDay.getDayName())));
                                        }
                                    });


                            exerciseToChangeComboBox.getSelectionModel().selectedItemProperty()
                                    .addListener((selectedExercise, oldExercise, newExercise) -> {
                                        if (exerciseToChangeComboBox != null && newExercise != null) {
                                            if (exerciseToChangeComboBox.getValue().getExercise().getName().equals("Rest")) {
                                                newSetsTextField.setText("-");
                                                newRepsTextField.setText("-");
                                            } else {
                                                newSetsTextField.setText(newExercise.getSet().toString());
                                                newRepsTextField.setText(newExercise.getRep().toString());
                                            }

                                        }
                                    });


                            newExerciseComboBox.setItems(FXCollections.observableList(Database.getExercisesFromDatabase()));
                            //prije sam koristio listener(ako se odabere Rest onda se sakrije sets i reps
                            // text fieldovi i labelovi, al se sve to moze skratit koristenjem onAction u SceneBuilderu,
                            //stavio sam na ovaj comboBox onAction metodu za sakrivanje tih elemenata i radi
                           /* newExerciseComboBox.getSelectionModel().selectedItemProperty()
                                    .addListener((selectedExercise, oldExercise, newExercise) -> {
                                        if (Optional.ofNullable(newExerciseComboBox.getValue()).isPresent()) {
                                            if (newExerciseComboBox.getValue().getName().equals("Rest")) {
                                                hideSetsAndRepsComponents();
                                            }

                                        }
                                    });*/

                        } else {
                            NoteATrainingController.noExistingUserFoundAlert(personClubId);
                            changeExerciseRadioButton.setSelected(false);
                        }
                    }

                } catch (NumberFormatException ex) {
                    EnterPersonClubIdController.invalidStringInput();
                    personClubIdTextField.clear();
                    changeExerciseRadioButton.setSelected(false);
                }


            } else if (newValue == addExerciseRadioButton) {
                changeOrAddExerciseButton.setText("Add");
                exerciseListLabel.setVisible(true);
                exerciseListComboBox.setVisible(true);

                try {
                    if (personClubIdTextField.getText().isEmpty()) {
                        NewExerciseController.emptyField("Person Club ID", "TextField");
                        addExerciseRadioButton.setSelected(false);
                    } else {
                        personClubId = Long.valueOf(personClubIdTextField.getText());
                        Optional<Person<LocalDate>> personByIdOptional = Database.getPersonById(personClubId);
                        if (personByIdOptional.isPresent()) {
                            correspondingPerson = personByIdOptional.get();
                            daysComboBox.setItems(FXCollections.observableList(Database.getDays()));
                            daysComboBox.getSelectionModel().selectedItemProperty()
                                    .addListener((selectedDay, oldDay, newDay) -> {
                                        if (Optional.ofNullable(newDay).isPresent()) {
                                            List<ExerciseSetAndRepForEachPerson> exerciseSetAndRepForEachPeople =
                                                    correspondingPerson.getExerciseSetAndRepMap()
                                                            .get(newDay.getDayName());

                                            List<Exercise> exercises = exerciseSetAndRepForEachPeople.stream()
                                                    .map(ExerciseSetAndRepForEachPerson::getExercise)
                                                    .collect(Collectors.toList());

                                            exerciseListComboBox.setItems(FXCollections.observableList(exercises));
                                            exerciseListComboBox.setEditable(false);
                                        }
                                    });
                        } else {
                            NoteATrainingController.noExistingUserFoundAlert(personClubId);
                            addExerciseRadioButton.setSelected(false);
                        }

                        newExerciseComboBox.setItems(FXCollections.observableList(Database.getExercisesFromDatabase()));

                        newExerciseLabel.setVisible(true);
                        newExerciseComboBox.setVisible(true);
                        newSetsLabel.setVisible(true);
                        newSetsTextField.setVisible(true);
                        newRepsLabel.setVisible(true);
                        newRepsTextField.setVisible(true);
                        exerciseToChangeLabel.setVisible(false);
                        exerciseToChangeComboBox.setVisible(false);
                        newSetsTextField.clear();
                        newRepsTextField.clear();
                    }

                } catch (NumberFormatException ex) {
                    EnterPersonClubIdController.invalidStringInput();
                    personClubIdTextField.clear();
                    addExerciseRadioButton.setSelected(false);
                }


            }
        });


    }

    private void hideElements(boolean b) {
        exerciseToChangeLabel.setVisible(b);
        exerciseToChangeComboBox.setVisible(b);
        newExerciseLabel.setVisible(b);
        newExerciseComboBox.setVisible(b);
        newSetsLabel.setVisible(b);
        newSetsTextField.setVisible(b);
        newRepsLabel.setVisible(b);
        newRepsTextField.setVisible(b);
    }

    public void hideSetsAndRepsComponents() {
        if (Optional.ofNullable(newExerciseComboBox.getValue()).isPresent()) {

                if (newExerciseComboBox.getValue().getName().equals("Rest")) {
                    newSetsLabel.setVisible(false);
                    newSetsTextField.setVisible(false);
                    newRepsLabel.setVisible(false);
                    newRepsTextField.setVisible(false);
                } else {
                    newSetsLabel.setVisible(true);
                    newSetsTextField.setVisible(true);
                    newRepsLabel.setVisible(true);
                    newRepsTextField.setVisible(true);
                }



        }

    }

    public void changeExercise() {

        if (!changeExerciseRadioButton.isSelected() && !addExerciseRadioButton.isSelected()) {
            Alert noRadioButtonPressedAlert = new Alert(Alert.AlertType.WARNING);
            noRadioButtonPressedAlert.setTitle("Warning");
            noRadioButtonPressedAlert.setHeaderText("No Button clicked");
            noRadioButtonPressedAlert.setContentText("Select either the \"Change\" or \"Add\" Button!");
            noRadioButtonPressedAlert.showAndWait();
        } else {
            Optional<Person<LocalDate>> chosenPersonOptional = Database.getPersonById(personClubId);
            if (chosenPersonOptional.isPresent()) {

                if (changeExerciseRadioButton.isSelected()) {
                    Person<LocalDate> chosenPerson = chosenPersonOptional.get();

                    if (Optional.ofNullable(daysComboBox.getValue()).isEmpty()) {
                        NewExerciseController.emptyField("Day", "ComboBox");
                        return;
                    }
                    if (Optional.ofNullable(exerciseToChangeComboBox.getValue()).isEmpty()) {
                        NewExerciseController.emptyField("Exercise To Change", "ComboBox");
                        return;
                    }
                    if (newSetsTextField.getText().isEmpty()) {
                        NewExerciseController.emptyField("Exercise Sets", "TextField");
                        return;
                    }
                    if (newRepsTextField.getText().isEmpty()) {
                        NewExerciseController.emptyField("Exercise Reps", "TextField");
                        return;
                    }

                    if(exerciseToChangeComboBox.getValue().getExercise().getName().equals("Rest") &&
                            Optional.ofNullable(newExerciseComboBox.getValue()).isEmpty()){
                        NewExerciseController.emptyField("New Exercise", "ComboBox");
                        return;
                    }

                    //if (checkIfEmptyField()) return;
                    Integer sets;
                    Integer reps;
                    Alert getConfirmationAlert = DeleteExerciseController.getConfirmationAlert("Exercise",
                            exerciseToChangeComboBox.getValue().getExercise().getName(), "edit");
                    Optional<ButtonType> buttonTypeOptional = getConfirmationAlert.showAndWait();
                    if (buttonTypeOptional.isPresent()) {
                        if (!buttonTypeOptional.get().getButtonData().isCancelButton()) {
                            Day chosenDay = daysComboBox.getValue();
                            ExerciseSetAndRepForEachPerson oldExerciseWithSetsAndReps =
                                    exerciseToChangeComboBox.getValue();
                            if(Optional.ofNullable(newExerciseComboBox.getValue()).isEmpty()){
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Warning");
                                alert.setHeaderText("New Exercise not selected");
                                alert.setContentText("You are about to change Sets and Reps only," +
                                        " are you sure?");
                                Optional<ButtonType> buttonOptional = alert.showAndWait();

                                if(buttonOptional.isPresent()){
                                    if(!buttonOptional.get().getButtonData().isCancelButton()){

                                        try{
                                            sets = Integer.valueOf(newSetsTextField.getText());
                                            reps = Integer.valueOf(newRepsTextField.getText());

                                            Database.changeOnlySetsAndRepsForExercise(chosenPerson,
                                                    exerciseToChangeComboBox.getValue(), sets, reps,
                                                    daysComboBox.getValue().getDayId());

                                        }
                                        catch (NumberFormatException ex){
                                            EnterPersonClubIdController.invalidStringInput();
                                            newSetsTextField.clear();
                                            newRepsTextField.clear();
                                        }

                                    }
                                   else{
                                        normallyChangeExercise(chosenPerson, chosenDay, oldExerciseWithSetsAndReps);
                                    }
                                }

                            }
                            else{
                                normallyChangeExercise(chosenPerson, chosenDay, oldExerciseWithSetsAndReps);
                            }


                        }
                    }
                }

                if (addExerciseRadioButton.isSelected()) {
                    
                    Person<LocalDate> chosenPerson = chosenPersonOptional.get();

                    if (checkIfEmptyField()) return;

                    Alert getConfirmationAlert = DeleteExerciseController.getConfirmationAlert("Exercise",
                            newExerciseComboBox.getValue().getName(), "add");
                    Optional<ButtonType> buttonTypeOptional = getConfirmationAlert.showAndWait();
                    if (buttonTypeOptional.isPresent()) {
                        if (!buttonTypeOptional.get().getButtonData().isCancelButton()) {
                            Day chosenDay = daysComboBox.getValue();
                            Exercise newExercise = newExerciseComboBox.getValue();
                            Integer sets;
                            Integer reps;
                            try{
                                 sets = Integer.valueOf(newSetsTextField.getText());
                                 reps = Integer.valueOf(newRepsTextField.getText());

                                Database.addNewExerciseToAlreadyExistingExercises(chosenPerson, chosenDay,
                                        newExercise, sets, reps);
                            }
                            catch (NumberFormatException ex){
                                EnterPersonClubIdController.invalidStringInput();
                                newSetsTextField.clear();
                                newRepsTextField.clear();
                            }
                        }
                    }
                }
            } else {
                NoteATrainingController.noExistingUserFoundAlert(personClubId);
            }
        }


    }

    private void normallyChangeExercise(Person<LocalDate> chosenPerson, Day chosenDay, ExerciseSetAndRepForEachPerson oldExerciseWithSetsAndReps) {
        Integer sets;
        Integer reps;
        if(Optional.ofNullable(newExerciseComboBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("NewExercise",
                    "ComboBox");
        }
        else{
            Exercise newExercise = newExerciseComboBox.getValue();
            try{
                sets = Integer.valueOf(newSetsTextField.getText());
                reps = Integer.valueOf(newRepsTextField.getText());

                Database.changeExercise(chosenPerson, chosenDay, oldExerciseWithSetsAndReps,
                        newExercise, sets, reps);
            }
            catch (NumberFormatException ex){
                EnterPersonClubIdController.invalidStringInput();
                newSetsTextField.clear();
                newRepsTextField.clear();
            }
        }
    }

    private boolean checkIfEmptyField() {
        if (Optional.ofNullable(daysComboBox.getValue()).isEmpty()) {
            NewExerciseController.emptyField("Day", "ComboBox");
            return true;
        }

        if (Optional.ofNullable(newExerciseComboBox.getValue()).isEmpty()) {
            NewExerciseController.emptyField("New Exercise", "ComboBox");
            return true;
        }
        if (newSetsTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("Exercise Sets", "TextField");
            return true;
        }
        if (newRepsTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("Exercise Reps", "TextField");
            return true;
        }
        return false;
    }


}
