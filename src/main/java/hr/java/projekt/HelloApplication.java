package hr.java.projekt;


import hr.java.projekt.controller.LoginOrSignUpController;
import hr.java.projekt.controller.MenuBarController;
import hr.java.projekt.entity.login.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HelloApplication extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginOrRegister.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 359);
        stage.setTitle("Welcome!");
        stage.setScene(scene);
        stage.show();

        /*LoginOrSignUpController.enteredUser = new User("Admin", "Adminic",
                "adminAdminic", "admin123", "admin");
        MenuBarController.showMainMenuScreen();*/

    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }
}