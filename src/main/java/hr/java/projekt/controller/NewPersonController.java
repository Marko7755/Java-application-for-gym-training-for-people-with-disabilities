package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.entity.model.ExerciseSetAndRepForEachPerson;
import hr.java.projekt.entity.model.Person;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class NewPersonController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController) {
        menuBarController.getMenuBar().getStylesheets().
                add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }


    @FXML
    private TextField personNameTextField;
    @FXML
    private TextField personSurnameTextField;
    @FXML
    private TextField personClubIdTextField;
    @FXML
    private DatePicker personMembershipDateDatePicker;

    private final Map<String, List<ExerciseSetAndRepForEachPerson>> exerciseMap = new TreeMap<>();

    @FXML
    private ComboBox<Exercise> mondayExerciseComboBox;
    @FXML
    private TextField mondayRepsTextField;
    @FXML
    private TextField mondaySetsTextField;

    private final List<ExerciseSetAndRepForEachPerson> mondayExerciseList = new ArrayList<>();

    public void saveExerciseForMonday() {

        boolean isEmpty;
        isEmpty = checkIfEmpty(mondayExerciseComboBox, mondayRepsTextField, mondaySetsTextField);

        if (isEmpty) {
            return;
        }
        Exercise chosenExercise = mondayExerciseComboBox.getValue();

        if (chosenExercise.getName().equals("Rest")) {
            ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                    .setExercise(chosenExercise)
                    .build();
            mondayExerciseList.add(exerciseWithSetsAndReps);
            exerciseMap.put("Monday", mondayExerciseList);
        } else {
            try {
                Integer rep = Integer.valueOf(mondayRepsTextField.getText());
                Integer set = Integer.valueOf(mondaySetsTextField.getText());


                ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                        .setExercise(chosenExercise)
                        .setSet(set)
                        .setRep(rep)
                        .build();
                mondayExerciseList.add(exerciseWithSetsAndReps);
                exerciseMap.put("Monday", mondayExerciseList);
            } catch (NumberFormatException e) {
                EnterPersonClubIdController.invalidStringInput();
            }
        }

    }

    @FXML
    private ComboBox<Exercise> tuesdayExerciseComboBox;
    @FXML
    private TextField tuesdayRepsTextField;
    @FXML
    private TextField tuesdaySetsTextField;

    private final List<ExerciseSetAndRepForEachPerson> tuesdayExerciseList = new ArrayList<>();

    public void saveExerciseForTuesday() {

        boolean isEmpty = checkIfEmpty(tuesdayExerciseComboBox, tuesdayRepsTextField, tuesdaySetsTextField);

        if (!isEmpty) {
            Exercise chosenExercise = tuesdayExerciseComboBox.getValue();

            if (chosenExercise.getName().equals("Rest")) {
                ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                        .setExercise(chosenExercise)
                        .build();
                tuesdayExerciseList.add(exerciseWithSetsAndReps);
                exerciseMap.put("Tuesday", mondayExerciseList);
            } else {
                try {
                    Integer rep = Integer.valueOf(tuesdayRepsTextField.getText());
                    Integer set = Integer.valueOf(tuesdaySetsTextField.getText());


                    ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                            .setExercise(chosenExercise)
                            .setSet(set)
                            .setRep(rep)
                            .build();
                    tuesdayExerciseList.add(exerciseWithSetsAndReps);
                    exerciseMap.put("Tuesday", tuesdayExerciseList);
                } catch (NumberFormatException e) {
                    EnterPersonClubIdController.invalidStringInput();
                }
            }
        }

    }


    @FXML
    private ComboBox<Exercise> wednesdayExerciseComboBox;
    @FXML
    private TextField wednesdayRepsTextField;
    @FXML
    private TextField wednesdaySetsTextField;

    private final List<ExerciseSetAndRepForEachPerson> wednesdayExerciseList = new ArrayList<>();

    public void saveExerciseForWednesday() {

        boolean isEmpty = checkIfEmpty(wednesdayExerciseComboBox, wednesdayRepsTextField, wednesdaySetsTextField);

        if (!isEmpty) {

            Exercise chosenExercise = wednesdayExerciseComboBox.getValue();

            if (chosenExercise.getName().equals("Rest")) {
                ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                        .setExercise(chosenExercise)
                        .build();
                wednesdayExerciseList.add(exerciseWithSetsAndReps);
                exerciseMap.put("Wednesday", wednesdayExerciseList);
            } else {
                try {
                    Integer rep = Integer.valueOf(wednesdayRepsTextField.getText());
                    Integer set = Integer.valueOf(wednesdaySetsTextField.getText());


                    ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                            .setExercise(chosenExercise)
                            .setSet(set)
                            .setRep(rep)
                            .build();
                    wednesdayExerciseList.add(exerciseWithSetsAndReps);
                    exerciseMap.put("Wednesday", wednesdayExerciseList);
                } catch (NumberFormatException e) {
                    EnterPersonClubIdController.invalidStringInput();
                }
            }
        }
    }


    @FXML
    private ComboBox<Exercise> thursdayExerciseComboBox;
    @FXML
    private TextField thursdayRepsTextField;
    @FXML
    private TextField thursdaySetsTextField;

    private final List<ExerciseSetAndRepForEachPerson> thursdayExerciseList = new ArrayList<>();

    public void saveExerciseForThursday() {

        boolean isEmpty = checkIfEmpty(thursdayExerciseComboBox, thursdayRepsTextField, thursdaySetsTextField);

        if (!isEmpty) {
            Exercise chosenExercise = thursdayExerciseComboBox.getValue();

            if (chosenExercise.getName().equals("Rest")) {
                ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                        .setExercise(chosenExercise)
                        .build();
                thursdayExerciseList.add(exerciseWithSetsAndReps);
                exerciseMap.put("Thursday", thursdayExerciseList);
            } else {
                try {
                    Integer rep = Integer.valueOf(thursdayRepsTextField.getText());
                    Integer set = Integer.valueOf(thursdaySetsTextField.getText());


                    ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                            .setExercise(chosenExercise)
                            .setSet(set)
                            .setRep(rep)
                            .build();
                    thursdayExerciseList.add(exerciseWithSetsAndReps);
                    exerciseMap.put("Thursday", thursdayExerciseList);
                } catch (NumberFormatException e) {
                    EnterPersonClubIdController.invalidStringInput();
                }
            }
        }
    }


    @FXML
    private ComboBox<Exercise> fridayExerciseComboBox;
    @FXML
    private TextField fridayRepsTextField;
    @FXML
    private TextField fridaySetsTextField;

    private final List<ExerciseSetAndRepForEachPerson> fridayExerciseList = new ArrayList<>();

    public void saveExerciseForFriday() {

        boolean isEmpty = checkIfEmpty(fridayExerciseComboBox, fridayRepsTextField, fridaySetsTextField);

        if (!isEmpty) {
            Exercise chosenExercise = fridayExerciseComboBox.getValue();

            if (chosenExercise.getName().equals("Rest")) {
                ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                        .setExercise(chosenExercise)
                        .build();
                fridayExerciseList.add(exerciseWithSetsAndReps);
                exerciseMap.put("Friday", fridayExerciseList);
            } else {
                try {
                    Integer rep = Integer.valueOf(fridayRepsTextField.getText());
                    Integer set = Integer.valueOf(fridaySetsTextField.getText());


                    ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                            .setExercise(chosenExercise)
                            .setSet(set)
                            .setRep(rep)
                            .build();
                    fridayExerciseList.add(exerciseWithSetsAndReps);
                    exerciseMap.put("Friday", fridayExerciseList);
                } catch (NumberFormatException e) {
                    EnterPersonClubIdController.invalidStringInput();
                }
            }
        }
    }

    @FXML
    private ComboBox<Exercise> saturdayExerciseComboBox;
    @FXML
    private TextField saturdayRepsTextField;
    @FXML
    private TextField saturdaySetsTextField;

    private final List<ExerciseSetAndRepForEachPerson> saturdayExerciseList = new ArrayList<>();

    public void saveExerciseForSaturday() {

        boolean isEmpty = checkIfEmpty(saturdayExerciseComboBox, saturdayRepsTextField, saturdaySetsTextField);

        if (!isEmpty) {
            Exercise chosenExercise = saturdayExerciseComboBox.getValue();

            if (chosenExercise.getName().equals("Rest")) {
                ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                        .setExercise(chosenExercise)
                        .build();
                saturdayExerciseList.add(exerciseWithSetsAndReps);
                exerciseMap.put("Saturday", saturdayExerciseList);
            } else {
                try {
                    Integer rep = Integer.valueOf(saturdayRepsTextField.getText());
                    Integer set = Integer.valueOf(saturdaySetsTextField.getText());


                    ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                            .setExercise(chosenExercise)
                            .setSet(set)
                            .setRep(rep)
                            .build();
                    saturdayExerciseList.add(exerciseWithSetsAndReps);
                    exerciseMap.put("Saturday", saturdayExerciseList);
                } catch (NumberFormatException e) {
                    EnterPersonClubIdController.invalidStringInput();
                }
            }

        }
    }

    @FXML
    private ComboBox<Exercise> sundayExerciseComboBox;
    @FXML
    private TextField sundayRepsTextField;
    @FXML
    private TextField sundaySetsTextField;

    private final List<ExerciseSetAndRepForEachPerson> sundayExerciseList = new ArrayList<>();

    public void saveExerciseForSunday() {

        boolean isEmpty = checkIfEmpty(sundayExerciseComboBox, sundayRepsTextField, sundaySetsTextField);

        if (!isEmpty) {


            Exercise chosenExercise = sundayExerciseComboBox.getValue();

            if (chosenExercise.getName().equals("Rest")) {
                ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                        .setExercise(chosenExercise)
                        .build();
                sundayExerciseList.add(exerciseWithSetsAndReps);
                exerciseMap.put("Sunday", sundayExerciseList);
            } else {
                try {
                    Integer rep = Integer.valueOf(sundayRepsTextField.getText());
                    Integer set = Integer.valueOf(sundaySetsTextField.getText());


                    ExerciseSetAndRepForEachPerson exerciseWithSetsAndReps = new ExerciseSetAndRepForEachPerson.Builder()
                            .setExercise(chosenExercise)
                            .setSet(set)
                            .setRep(rep)
                            .build();
                    sundayExerciseList.add(exerciseWithSetsAndReps);
                    exerciseMap.put("Sunday", sundayExerciseList);
                } catch (NumberFormatException e) {
                    EnterPersonClubIdController.invalidStringInput();
                }
            }
        }

    }

    public void saveNewPerson() {

        boolean isEmpty = false;
        if (personNameTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("TextField", "Person Name");
            isEmpty = true;
        }
        if (personSurnameTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("TextField", "Person Surname");
            isEmpty = true;
        }
        if (personClubIdTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("TextField", "Person Club ID");
            isEmpty = true;
        }
        if (personMembershipDateDatePicker.getValue() == null) {
            NewExerciseController.emptyField("Datepicker", "Membership Date");
            isEmpty = true;
        }

        if (!isEmpty) {
            boolean isDayEmpty = false;
            if (!exerciseMap.containsKey("Monday")) {
                NewExerciseController.emptyField("Exercise Day", "Monday");
                isDayEmpty = true;
            }
            if (!exerciseMap.containsKey("Tuesday")) {
                NewExerciseController.emptyField("Exercise Day", "Tuesday");
                isDayEmpty = true;
            }
            if (!exerciseMap.containsKey("Wednesday")) {
                NewExerciseController.emptyField("Exercise Day", "Wednesday");
                isDayEmpty = true;
            }
            if (!exerciseMap.containsKey("Thursday")) {
                NewExerciseController.emptyField("Exercise Day", "Thursday");
                isDayEmpty = true;
            }
            if (!exerciseMap.containsKey("Friday")) {
                NewExerciseController.emptyField("Exercise Day", "Friday");
                isDayEmpty = true;
            }
            if (!exerciseMap.containsKey("Saturday")) {
                NewExerciseController.emptyField("Exercise Day", "Saturday");
                isDayEmpty = true;
            }
            if (!exerciseMap.containsKey("Sunday")) {
                NewExerciseController.emptyField("Exercise Day", "Sunday");
                isDayEmpty = true;
            }

            if (!isDayEmpty) {
                Optional<Integer> id = Person.getNextPersonId();
                if (id.isPresent()) {
                    try {
                        Integer personId = id.get();
                        String name = personNameTextField.getText();
                        String surname = personSurnameTextField.getText();
                        Long clubId = Long.valueOf(personClubIdTextField.getText());
                        LocalDate membershipDate = personMembershipDateDatePicker.getValue();
                        Person<LocalDate> newPerson = new Person<>(personId, name, surname, clubId, membershipDate, exerciseMap);

                        Database.saveNewPerson(newPerson);

                    } catch (NumberFormatException e) {
                        EnterPersonClubIdController.invalidStringInput();
                        personClubIdTextField.clear();
                    }
                }
            }
        }
    }

    private Boolean checkIfEmpty(ComboBox<Exercise> tuesdayExerciseComboBox, TextField tuesdayRepsTextField,
                                 TextField tuesdaySetsTextField) {
        boolean isEmpty = false;

        if (Optional.ofNullable(tuesdayExerciseComboBox.getValue()).isEmpty()) {
            NewExerciseController.emptyField("ComboBox", "Exercise");
            isEmpty = true;
        }

        if(!isEmpty && tuesdayExerciseComboBox.getValue().getName().equals("Rest")){
            return false;
        }

        if (tuesdayRepsTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("TextField", "Exercise Reps");
            isEmpty = true;
        }
        if (tuesdaySetsTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("TextField", "Exercise Sets");
            isEmpty = true;
        }
        return isEmpty;
    }


    public void initialize() {
        List<Exercise> exerciseList = Database.getExercisesFromDatabase();

        mondayExerciseComboBox.setItems(FXCollections.observableList(exerciseList));
        tuesdayExerciseComboBox.setItems(FXCollections.observableList(exerciseList));
        wednesdayExerciseComboBox.setItems(FXCollections.observableList(exerciseList));
        thursdayExerciseComboBox.setItems(FXCollections.observableList(exerciseList));
        fridayExerciseComboBox.setItems(FXCollections.observableList(exerciseList));
        saturdayExerciseComboBox.setItems(FXCollections.observableList(exerciseList));
        sundayExerciseComboBox.setItems(FXCollections.observableList(exerciseList));

    }


}
