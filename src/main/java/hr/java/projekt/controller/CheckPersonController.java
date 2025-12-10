package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CheckPersonController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass()
                .getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private Label personLabel;

    private final Person<LocalDate> personToCheck = EnterPersonClubIdController.person;
    public void checkMembership(){

        LocalDate dateToday = LocalDate.now();
        long monthsPassed = ChronoUnit.MONTHS.between(personToCheck.getMembershipDate(), dateToday);

        if(monthsPassed < 1){
            long daysRemaining =
                    ChronoUnit.DAYS.between(dateToday, personToCheck.getMembershipDate());
            membershipValid(daysRemaining);
        }
        else{
            long daysPassed = ChronoUnit.DAYS.between(personToCheck.getMembershipDate(), dateToday);
            Alert alertResult = membershipNotValid(daysPassed);
            Optional<ButtonType> buttonType = alertResult.showAndWait();
            if(buttonType.isPresent()){
                if(!buttonType.get().getButtonData().isCancelButton()){
                    MenuBarController.membershipRenewalScreen();
                }
            }
        }
    }

    public void checkExercises(){
        MenuBarController.personEachDayExerciseScreen();
    }

    public void checkCompetition(){

        if(personToCheck.getConditionsFulfilled().equals("NO")){
            conditionsNotFulfilledAlert(personToCheck);
        }
        else if(personToCheck.getConditionsFulfilled().equals("YES")){
            if(Optional.ofNullable(personToCheck.getCompetition()).isEmpty()){
                Database.showAddCompetitionIfNotAlreadySetScreen(personToCheck);
            }
            else{
                competitionInfoAlert(personToCheck);
            }

        }

    }

    public static void conditionsNotFulfilledAlert(Person<LocalDate> personToCheck) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Competition conditions");
        alert.setHeaderText("Competition conditions not fulfilled");
        alert.setContentText(personToCheck.getName() + " " + personToCheck.getSurname() +
                " doesn't have Competition conditions fulfilled!\n" +
                personToCheck.getCompletedTrainings() + "\\20" + " trainings done");
        alert.showAndWait();
    }

    public static void competitionInfoAlert(Person<LocalDate> personToCheck) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Competition conditions");
        alert.setHeaderText("Competition conditions fulfilled");
        alert.setContentText(personToCheck.getName() + " " +
                personToCheck.getSurname() + " has a Competition: \nAddress: " +
                personToCheck.getCompetition().getAddress().getStreet() + ", " +
                personToCheck.getCompetition().getAddress().getCity() + ", Hall of the competition: " +
                personToCheck.getCompetition().getAddress().getHall() + "\n" + "Date: " +
                personToCheck.getCompetition().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                "\n" + "Time: " + personToCheck.getCompetition().getTime());
        alert.showAndWait();
    }

    private void membershipValid(long days){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Status");
        alert.setHeaderText("Membership status");
        alert.setContentText("Membership is valid for " + personToCheck.getName() + " "
                + personToCheck.getSurname() + "!" + "\n" + days + " more days till expiration!");
        alert.showAndWait();
    }

    private Alert membershipNotValid(long days){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Status");
        alert.setHeaderText("Membership status");
        alert.setContentText("Membership for " + personToCheck.getName() + " "
                + personToCheck.getSurname() + " has expired!" + "\n" + days + " since expiration!" + "\n" +
                "Surcharge is needed! " +
                "\nDo you want to do a Surcharge now?");
        ButtonType yesButton = new ButtonType("Yes", ButtonType.YES.getButtonData());
        ButtonType noButton = new ButtonType("No", ButtonType.NO.getButtonData());
        alert.getButtonTypes().setAll(yesButton, noButton);
        return alert;
    }


    public void initialize(){
        Person<LocalDate> person = EnterPersonClubIdController.person;
        personLabel.setText(person.getName() + " " + person.getSurname());
        personLabel.getStylesheets().add(getClass()
                .getResource("/style/personInfoLabel.css").toExternalForm());

        Database.checkIfCompetitionFinished(person);

    }



}
