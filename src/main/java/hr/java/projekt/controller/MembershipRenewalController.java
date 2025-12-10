package hr.java.projekt.controller;

import hr.java.projekt.database.Database;
import hr.java.projekt.entity.model.Person;
import hr.java.projekt.fileUtils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.Optional;

public class MembershipRenewalController {

    @FXML
    private HBox menuBarHbox;

    public void setMenuBar(MenuBarController menuBarController){
        menuBarController.getMenuBar().getStylesheets().add(getClass()
                .getResource("/style/centerMenus.css").toExternalForm());
        menuBarHbox.getChildren().addAll(menuBarController.getIconButton(), menuBarController.getMenuBar());
    }

    @FXML
    private DatePicker membershipDatePicker;

    public void setNewMembership(){
        if(Optional.ofNullable(membershipDatePicker.getValue()).isEmpty()){
            NewExerciseController.emptyField("Datepicker", "Membership");
        }
        else{
            Person<LocalDate> person = EnterPersonClubIdController.person;
            LocalDate renewalDate = membershipDatePicker.getValue();
            Database.membershipRenewal(renewalDate, person.getId(), person.getClubId());
            FileUtils.membershipRenewal(person.getMembershipDate().toString(), renewalDate.toString(),
                    person.getName(), person.getSurname());
        }
    }

}
