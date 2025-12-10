package hr.java.projekt.controller;

import hr.java.projekt.controller.MenuBarController;
import hr.java.projekt.entity.changes.ChangesMade;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AllChangesMadeController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets()
                .add(getClass().getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TableView<ChangesMade<?>> changesMadeTableView;
    @FXML
    private TableColumn<ChangesMade<?>, String> attributesChangedTableColumn;
    @FXML
    private TableColumn<ChangesMade<?>, String> oldValueTableColumn;
    @FXML
    private TableColumn<ChangesMade<?>, String> newValueTableColumn;
    @FXML
    private TableColumn<ChangesMade<?>, String> roleTableColumn;
    @FXML
    private TableColumn<ChangesMade<?>, String> dateAndTimeTableColumn;


    public void initialize(){
        attributesChangedTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getAttributeName()));

        oldValueTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper((String) value.getValue().getOldValue()));

        newValueTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper((String) value.getValue().getNewValue()));

        roleTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getUser()));

        dateAndTimeTableColumn.setCellValueFactory(value ->
                new ReadOnlyStringWrapper(value.getValue().getDateAndTimeOfEdition()));


        DateTimeFormatter formatter = ChangesMade.DATE_TIME_FORMATTER;
        List<ChangesMade<?>> changesMadeList = FileUtils.readAllChangesFromBinaryFile();

        List<ChangesMade<?>> sortedChanges = changesMadeList.stream()
                        .sorted((s1, s2)->LocalDateTime.parse(s2.getDateAndTimeOfEdition(), formatter)
                                .compareTo(LocalDateTime.parse(s1.getDateAndTimeOfEdition(), formatter)))
                                .collect(Collectors.toList());
        changesMadeTableView.setItems(FXCollections.observableList(sortedChanges));

    }


}
