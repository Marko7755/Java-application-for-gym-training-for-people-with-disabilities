package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Address;
import hr.java.projekt.entity.model.Competition;
import hr.java.projekt.entity.model.Person;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewCompetitionController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass().
                getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    private Person<LocalDate> person;
    public void setPerson(Person<LocalDate> person){
        this.person = person;
    }


    @FXML
    private ComboBox<Address> addressComboBox;
    @FXML
    private DatePicker competitionDatePicker;
    @FXML
    private TextField competitionTimeTextField;

    public void checkAddressAttributes(){

        if(Optional.ofNullable(addressComboBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ComboBox", "Address");
        }
        else{
            EditAddressController.addressInfoAlert(addressComboBox);
        }

    }

    public void saveCompetition(){

        if(Optional.ofNullable(addressComboBox.getValue()).isEmpty()){
            NewExerciseController.emptyField("ComboBox", "Address");
        }
        if(Optional.ofNullable(competitionDatePicker.getValue()).isEmpty()){
            NewExerciseController.emptyField("DatePicker", "Competition Date");
        }
        if(competitionTimeTextField.getText().isEmpty()) {
            NewExerciseController.emptyField("Time", "Competition Time");
        }
        if(!isTimeFormatValid(competitionTimeTextField.getText())) {
            invalidTimeFormatAlert();
        }
        else{
            Address address = addressComboBox.getValue();
            LocalDate date = competitionDatePicker.getValue();
            LocalTime time = LocalTime.parse(competitionTimeTextField.getText());
            String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

            Competition<Address, LocalDate> newCompetition = new Competition<>(address, date, formattedTime);
            Database.saveNewCompetition(person, newCompetition);
            MenuBarController menuBarController = new MenuBarController();
            menuBarController.checkCompetitionConditionsScreen();

        }

    }


    public static boolean isTimeFormatValid(String time) {
        String timeRegex = "^([01]\\d|2[0-3]):([0-5]\\d)$";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    public static void invalidTimeFormatAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Time Format");
        alert.setContentText("HH:mm format is required");
        alert.showAndWait();
    }

    public void initialize(){
        List<Address> addressList = Database.getAddresses();
        addressComboBox.setItems(FXCollections.observableList(addressList));
    }

}
