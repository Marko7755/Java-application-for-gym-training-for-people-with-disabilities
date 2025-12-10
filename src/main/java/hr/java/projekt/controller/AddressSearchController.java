package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Address;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.List;


public class AddressSearchController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private TextField addressStreetTextField;
    @FXML
    private TextField addressCityTextField;

    public void addressSearch(){
        String searchedAddress = addressStreetTextField.getText();
        String searchedAddressCity = addressCityTextField.getText();

        List<Address> addressList = Database.getSearchedAddresses(searchedAddress, searchedAddressCity);

        ObservableList<Address> addressObservableList = FXCollections.observableList(addressList);
        addressTableView.setItems(addressObservableList);

    }

    @FXML
    private TableView<Address> addressTableView;
    @FXML
    private TableColumn<Address, String> addressIdTableColumn;
    @FXML
    private TableColumn<Address, String> addressStreetTableColumn;
    @FXML
    private TableColumn<Address, String> addressCityTableColumn;
    @FXML
    private TableColumn<Address, String> addressHallTableColumn;


    public void initialize(){
        addressIdTableColumn.setCellValueFactory(addressStringCellDataFeatures ->
                new ReadOnlyStringWrapper(addressStringCellDataFeatures.getValue().getId().toString()));

        addressStreetTableColumn.setCellValueFactory(addressStringCellDataFeatures ->
                new ReadOnlyStringWrapper(addressStringCellDataFeatures.getValue().getStreet()));

        addressCityTableColumn.setCellValueFactory(addressStringCellDataFeatures ->
                new ReadOnlyStringWrapper(addressStringCellDataFeatures.getValue().getCity()));

        addressHallTableColumn.setCellValueFactory(addressStringCellDataFeatures ->
                new ReadOnlyStringWrapper(addressStringCellDataFeatures.getValue().getHall().toString()));

    }


}
