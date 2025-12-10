package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Exercise;
import hr.java.projekt.entity.model.ExerciseSetAndRepForEachPerson;
import hr.java.projekt.entity.model.Person;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PersonEachDayExercisesController {


    private static final Logger logger = LoggerFactory.getLogger(PersonEachDayExercisesController.class);
    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }


    @FXML
    private TableView<ExerciseSetAndRepForEachPerson> mondayPersonTableView;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> mondayExerciseNameTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> mondayExerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> mondayExerciseDescriptionTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> mondayExerciseRepsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> mondayExerciseSetsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, Hyperlink> mondayExerciseVideoTableColumn;


    @FXML
    private TableView<ExerciseSetAndRepForEachPerson> tuesdayPersonTableView;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> tuesdayExerciseNameTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> tuesdayExerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> tuesdayExerciseDescriptionTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> tuesdayExerciseRepsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> tuesdayExerciseSetsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, Hyperlink> tuesdayExerciseVideoTableColumn;


    @FXML
    private TableView<ExerciseSetAndRepForEachPerson> wednesdayPersonTableView;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> wednesdayExerciseNameTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> wednesdayExerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> wednesdayExerciseDescriptionTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> wednesdayExerciseRepsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> wednesdayExerciseSetsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, Hyperlink> wednesdayExerciseVideoTableColumn;


    @FXML
    private TableView<ExerciseSetAndRepForEachPerson> thursdayPersonTableView;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> thursdayExerciseNameTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> thursdayExerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> thursdayExerciseDescriptionTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> thursdayExerciseRepsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> thursdayExerciseSetsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, Hyperlink> thursdayExerciseVideoTableColumn;


    @FXML
    private TableView<ExerciseSetAndRepForEachPerson> fridayPersonTableView;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> fridayExerciseNameTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> fridayExerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> fridayExerciseDescriptionTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> fridayExerciseRepsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> fridayExerciseSetsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, Hyperlink> fridayExerciseVideoTableColumn;


    @FXML
    private TableView<ExerciseSetAndRepForEachPerson> saturdayPersonTableView;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> saturdayExerciseNameTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> saturdayExerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> saturdayExerciseDescriptionTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> saturdayExerciseRepsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> saturdayExerciseSetsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, Hyperlink> saturdayExerciseVideoTableColumn;


    @FXML
    private TableView<ExerciseSetAndRepForEachPerson> sundayPersonTableView;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> sundayExerciseNameTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> sundayExerciseBodyEffectTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> sundayExerciseDescriptionTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> sundayExerciseRepsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, String> sundayExerciseSetsTableColumn;
    @FXML
    private TableColumn<ExerciseSetAndRepForEachPerson, Hyperlink> sundayExerciseVideoTableColumn;


    public void initialize(){

        Person<LocalDate> enteredPerson = EnterPersonClubIdController.person;

        Map<String, List<ExerciseSetAndRepForEachPerson>> exerciseMap =
                Database.getExercisesForEachDay(enteredPerson.getClubId());

        List<Person<LocalDate>> persons = Database.getPersonsWithEverything();


        /*List<ExerciseSetAndRepForEachPerson> mondayExerciseList = persons.stream()
                .filter(person -> person.getClubId().equals(enteredPerson.getClubId())
                        && person.getExerciseSetAndRepMap().containsKey("Monday"))
                .flatMap(person -> person.getExerciseSetAndRepMap().get("Monday").stream())
                        .collect(Collectors.toList());*/

        List<ExerciseSetAndRepForEachPerson> mondayExerciseList = exerciseMap.get("Monday");//tu sam dobio
        // vjezbe preko baze
        setTableViewItems(mondayExerciseList, mondayPersonTableView, mondayExerciseNameTableColumn,
                mondayExerciseBodyEffectTableColumn, mondayExerciseDescriptionTableColumn,
                mondayExerciseRepsTableColumn, mondayExerciseSetsTableColumn, mondayExerciseVideoTableColumn);


        //flatMap se ovdje koristi kako bi dobio Stream<ExerciseSetAndRepForEachPerson> i tj pojedinu vjezbu i onda
        //ih se spremio u jednu listu
        //da sam koristio Map, dobio bi Stream<List<ExerciseSetAndRepForEachPerson>>, tj. listu u listi
        List<ExerciseSetAndRepForEachPerson> tuesdayExerciseList = persons.stream()// a tu preko lambde
                // (sto se tice brzine otvaranja prozora, identicno je)
                .filter(person -> person.getClubId().equals(enteredPerson.getClubId())
                        && person.getExerciseSetAndRepMap().containsKey("Tuesday"))
                        .flatMap(person -> person.getExerciseSetAndRepMap().get("Tuesday").stream())
                                .collect(Collectors.toList());

        //List<ExerciseSetAndRepForEachPerson> tuesdayExerciseList = exerciseMap.get("Tuesday");

        setTableViewItems(tuesdayExerciseList, tuesdayPersonTableView, tuesdayExerciseNameTableColumn,
                tuesdayExerciseBodyEffectTableColumn, tuesdayExerciseDescriptionTableColumn,
                tuesdayExerciseRepsTableColumn, tuesdayExerciseSetsTableColumn, tuesdayExerciseVideoTableColumn);



        List<ExerciseSetAndRepForEachPerson> wednesdayExerciseList = exerciseMap.get("Wednesday");
        setTableViewItems(wednesdayExerciseList, wednesdayPersonTableView, wednesdayExerciseNameTableColumn,
                wednesdayExerciseBodyEffectTableColumn, wednesdayExerciseDescriptionTableColumn,
                wednesdayExerciseRepsTableColumn, wednesdayExerciseSetsTableColumn, wednesdayExerciseVideoTableColumn);


        List<ExerciseSetAndRepForEachPerson> thursdayExerciseList = exerciseMap.get("Thursday");
        setTableViewItems(thursdayExerciseList, thursdayPersonTableView, thursdayExerciseNameTableColumn,
                thursdayExerciseBodyEffectTableColumn, thursdayExerciseDescriptionTableColumn,
                thursdayExerciseRepsTableColumn, thursdayExerciseSetsTableColumn, thursdayExerciseVideoTableColumn);


        List<ExerciseSetAndRepForEachPerson> fridayExerciseList = exerciseMap.get("Friday");
        setTableViewItems(fridayExerciseList, fridayPersonTableView, fridayExerciseNameTableColumn,
                fridayExerciseBodyEffectTableColumn, fridayExerciseDescriptionTableColumn,
                fridayExerciseRepsTableColumn, fridayExerciseSetsTableColumn, fridayExerciseVideoTableColumn);


        List<ExerciseSetAndRepForEachPerson> saturdayExerciseList = exerciseMap.get("Saturday");
        setTableViewItems(saturdayExerciseList, saturdayPersonTableView, saturdayExerciseNameTableColumn,
                saturdayExerciseBodyEffectTableColumn, saturdayExerciseDescriptionTableColumn,
                saturdayExerciseRepsTableColumn, saturdayExerciseSetsTableColumn, saturdayExerciseVideoTableColumn);


        List<ExerciseSetAndRepForEachPerson> sundayExerciseList = exerciseMap.get("Sunday");
        setTableViewItems(sundayExerciseList, sundayPersonTableView, sundayExerciseNameTableColumn,
                sundayExerciseBodyEffectTableColumn, sundayExerciseDescriptionTableColumn,
                sundayExerciseRepsTableColumn, sundayExerciseSetsTableColumn, sundayExerciseVideoTableColumn);



    }

    private void setTableViewItems(List<ExerciseSetAndRepForEachPerson> dayExerciseList,
                                   TableView<ExerciseSetAndRepForEachPerson> dayTableView,
                                   TableColumn<ExerciseSetAndRepForEachPerson, String> dayExerciseNameTableColumn,
                                   TableColumn<ExerciseSetAndRepForEachPerson,
                                           String> dayExerciseBodyEffectTableColumn,
                                   TableColumn<ExerciseSetAndRepForEachPerson,
                                           String> dayExerciseDescriptionTableColumn,
                                   TableColumn<ExerciseSetAndRepForEachPerson, String> dayExerciseRepsTableColumn,
                                   TableColumn<ExerciseSetAndRepForEachPerson, String> dayExerciseSetsTableColumn,
                                   TableColumn<ExerciseSetAndRepForEachPerson,
                                           Hyperlink> dayExerciseVideoTableColumn) {

        dayTableView.setItems(FXCollections.observableList(dayExerciseList));

        dayExerciseNameTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getExercise().getName()));

        dayExerciseBodyEffectTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getExercise().getBodyEffect().toString()));

        dayExerciseDescriptionTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getExercise().getDescription()));

        dayExerciseRepsTableColumn.setCellValueFactory(value ->
                Optional.ofNullable(value.getValue().getRep())
                        .map(rep -> new ReadOnlyStringWrapper(rep.toString()))
                        .orElse(new ReadOnlyStringWrapper("-")));

        dayExerciseSetsTableColumn.setCellValueFactory(value ->
                Optional.ofNullable(value.getValue().getSet())
                        .map(set -> new ReadOnlyStringWrapper(set.toString()))
                        .orElse(new ReadOnlyStringWrapper("-")));

        dayExerciseVideoTableColumn.setCellValueFactory(value ->{
            Exercise exercise = value.getValue().getExercise();
            return ExerciseSearchController.getHyperlinkObservableValue(exercise, logger);
        });
    }


    public void back(){
        MenuBarController.checkPersonScreen();
    }



}
