package hr.java.projekt.controller;


import hr.java.projekt.entity.login.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class MainMenuController {


    @FXML
    private VBox menuBarVbox;

    public void setMenuBar(MenuBarController menuBarController){

        MenuBar leftBar = menuBarController.getMenuBar();
        Region spacer = new Region();
        Button btn = new Button("Sign out");

        btn.setOnAction(actionEvent -> signOutClicked());
        btn.getStylesheets().add(getClass().getResource("/style/signOutButton.css").toExternalForm());
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        HBox menuBars = new HBox(leftBar, spacer, btn);
        menuBarVbox.getChildren().add(menuBars);


        //menuBarVbox.getChildren().add(menuBarController.getMenuBar());
    }

    public void signOutClicked(){
        Alert areYouSureAlert = new Alert(Alert.AlertType.CONFIRMATION);
        areYouSureAlert.setTitle("Confirmation");
        areYouSureAlert.setHeaderText("Are you sure?");
        areYouSureAlert.setContentText("Are you sure you want to Sign out?");

       /* Button yesButton = (Button) areYouSureAlert.getDialogPane().lookupButton(ButtonType.OK);
        yesButton.setText("Yes");*/
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        areYouSureAlert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = areYouSureAlert.showAndWait();
        if(result.isPresent() && result.get() == yesButton){
            /*LoginOrSignUpController.enteredUser = new User("Not logged in", "-", "-",
                    "-", "admin");*/
            LoginOrSignUpController.enteredUser = null;
            LoginOrSignUpController.showLoginScreen();
        }

    }

}