package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.entity.model.Person;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PersonSearchController {



    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextField personNameTextField;
    @FXML
    private TextField personSurnameTextField;

    public void pearsonSearch(){
        String searchedName = personNameTextField.getText();
        String searchedSurname = personSurnameTextField.getText();

        List<Person<LocalDate>> personList = Database.getPersons();

        List<Person<LocalDate>> filteredList = personList.stream()
                .filter(person -> person.getName().toLowerCase().contains(searchedName.toLowerCase()) &&
                        person.getSurname().toLowerCase().contains(searchedSurname.toLowerCase()))
                .collect(Collectors.toList());


        ObservableList<Person<LocalDate>> addressObservableList = FXCollections.observableList(filteredList);
        personTableView.setItems(addressObservableList);

    }

    @FXML
    private TableView<Person<LocalDate>> personTableView;
    @FXML
    private TableColumn<Person<LocalDate>, String> personIdTableColumn;
    @FXML
    private TableColumn<Person<LocalDate>, String> personClubIdTableColumn;
    @FXML
    private TableColumn<Person<LocalDate>, String> personNameTableColumn;
    @FXML
    private TableColumn<Person<LocalDate>, String> personSurnameTableColumn;
    @FXML
    private TableColumn<Person<LocalDate>, String> personMembershipDateTableColumn;


    public void initialize(){

        personIdTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getId().toString()));

        personClubIdTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getClubId().toString()));

        personNameTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getName()));

        personSurnameTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getSurname()));

        personMembershipDateTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getMembershipDate().toString()));


    }


}
