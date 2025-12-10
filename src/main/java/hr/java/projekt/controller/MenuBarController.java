package hr.java.projekt.controller;

import hr.java.projekt.HelloApplication;
import hr.java.projekt.entity.login.User;
import hr.java.projekt.entity.model.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class MenuBarController {

    @FXML
    private MenuBar menuBar;
    @FXML
    private Button iconButton;

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Button getIconButton() {
        return iconButton;
    }


    private final User enteredUser = LoginOrSignUpController.enteredUser;

    public static MenuBarController openAndUpdateMenuBar() throws IOException {
        FXMLLoader menuBarFxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menuBar.fxml"));
        menuBarFxmlLoader.load();
        MenuBarController menuBarController = menuBarFxmlLoader.getController();
        menuBarController.updateMenuItemsVisibility();
        return menuBarController;
    }

    public void updateMenuItemsVisibility() {
        Optional<User> enteredUserOptional = Optional.ofNullable(enteredUser);
        if(enteredUserOptional.isPresent()) {
            User user = enteredUserOptional.get();
            for(Menu menu : menuBar.getMenus()){
                for(MenuItem menuItem : menu.getItems()){
                    if("admin".equals(user.getRole())){
                        menuItem.setVisible(true);
                    } else{
                        if("user".equals(user.getRole())){
                            if(menu.getText().equals("User") || menu.getText().equals("Changes")){
                                menu.setVisible(false);
                            }
                            if(menuItem.getText().equals("Edit") || menuItem.getText().equals("Delete")){
                                menuItem.setVisible(false);
                            }
                        }
                        else {
                            if(menu.getText().equals("User") || menu.getText().equals("Changes")) {
                                menu.setVisible(false);
                            }
                        }
                    }
                }
            }
        }
    }


    public static void showMainMenuScreen() {
        try {
            MenuBarController menuBarController = openAndUpdateMenuBar();
            //promjene su vidjlive u menuBaru ali nakon pokretana mainMenu.fxmla - promjene nisu vidljive
            // fix je bio ukloniti fx:id i dinamicki postaviti menuBar
            FXMLLoader mainMenuFxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainMenu.fxml"));
            AnchorPane mainMenuRoot = mainMenuFxmlLoader.load();
            MainMenuController mainMenuController = mainMenuFxmlLoader.getController();
            mainMenuController.setMenuBar(menuBarController);

            Scene mainMenuScene = new Scene(mainMenuRoot, 628, 378);
            centerScreen(mainMenuScene);
            HelloApplication.getMainStage().setScene(mainMenuScene);
            HelloApplication.getMainStage().show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showAddNewPersonScreen() {
        try {
            MenuBarController menuBarController = openAndUpdateMenuBar();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newPerson.fxml"));
            ScrollPane newPersonRoot = fxmlLoader.load();
            NewPersonController newPersonController = fxmlLoader.getController();
            newPersonController.setMenuBar(menuBarController);
            Scene scene = new Scene(newPersonRoot, 1083, 600);

            centerScreen(scene);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void centerScreen(Scene scene) {
        // Center the new stage on the primary screen
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        HelloApplication.getMainStage().setX(bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) / 2);
        HelloApplication.getMainStage().setY(bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) / 2);
    }

    public void showSearchExerciseScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("exerciseSearch.fxml"));
            Parent exerciseSearchRoot = fxmlLoader.load();
            ExerciseSearchController exerciseSearchController = fxmlLoader.getController();
            exerciseSearchController.setMenuBar(menuBarController);

            Scene scene = new Scene(exerciseSearchRoot, 1169, 666);
            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void showAddNewExerciseScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newExercise.fxml"));
            AnchorPane newExerciseRoot = fxmlLoader.load();
            NewExerciseController newExerciseController = fxmlLoader.getController();
            newExerciseController.setMenuBar(menuBarController);
            Scene scene = new Scene(newExerciseRoot, 732, 509);

            centerScreen(scene);
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }

        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void iconButtonClicked(){
        showMainMenuScreen();
    }

    public void showEditExerciseScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader editScreenFxml = new FXMLLoader(HelloApplication.class.getResource("editExercise.fxml"));
            Parent parentRoot = editScreenFxml.load();
            EditExerciseController editExerciseController = editScreenFxml.getController();
            editExerciseController.setMenuBar(menuBarController);
            Scene scene = new Scene(parentRoot, 732, 714);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showDeleteExerciseScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader deleteScreenFxml =
                    new FXMLLoader(HelloApplication.class.getResource("deleteExercise.fxml"));
            Parent parentRoot = deleteScreenFxml.load();
            DeleteExerciseController deleteScreenController = deleteScreenFxml.getController();
            deleteScreenController.setMenuBar(menuBarController);
            Scene scene = new Scene(parentRoot, 607, 366);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void showAddNewAddressScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader addNewAddressFxml =
                    new FXMLLoader(HelloApplication.class.getResource("addNewAddress.fxml"));
            Parent parentRoot = addNewAddressFxml.load();
            AddNewAddressController addNewAddressController = addNewAddressFxml.getController();
            addNewAddressController.setMenuBar(menuBarController);
            Scene scene = new Scene(parentRoot, 600, 516);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showAddressSearchScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader addressSearchFxml =
                    new FXMLLoader(HelloApplication.class.getResource("addressSearch.fxml"));
            Parent parentRoot = addressSearchFxml.load();
            AddressSearchController addNewExerciseController = addressSearchFxml.getController();
            addNewExerciseController.setMenuBar(menuBarController);
            Scene scene = new Scene(parentRoot, 568, 666);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showEditAddressScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader editAddressFxml = new FXMLLoader(HelloApplication.class.getResource("editAddress.fxml"));
            Parent parent = editAddressFxml.load();
            EditAddressController editAddressController = editAddressFxml.getController();
            editAddressController.setMenuBar(menuBarController);

            Scene scene = new Scene(parent, 732, 611);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deleteAnAddress(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader deleteAnAddressFxml =
                    new FXMLLoader(HelloApplication.class.getResource("deleteAddress.fxml"));
            Parent parentRoot = deleteAnAddressFxml.load();
            DeleteAddressController deleteAddressController = deleteAnAddressFxml.getController();
            deleteAddressController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 607, 366);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void noteATrainingScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader noteATrainingFxml =
                    new FXMLLoader(HelloApplication.class.getResource("noteATraining.fxml"));
            Parent parentRoot = noteATrainingFxml.load();
            NoteATrainingController noteATrainingController = noteATrainingFxml.getController();
            noteATrainingController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 600, 475);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void checkCompetitionConditionsScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader checkCompetitionFxml =
                    new FXMLLoader(HelloApplication.class.getResource("checkCompetitionConditions.fxml"));
            Parent parentRoot = checkCompetitionFxml.load();
            CheckCompetitionConditions checkCompetitionConditions = checkCompetitionFxml.getController();
            checkCompetitionConditions.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 600, 400);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void newCompetitionScreen(Person<LocalDate> person){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader newCompetitionFxml =
                    new FXMLLoader(HelloApplication.class.getResource("newCompetition.fxml"));
            Parent parentRoot = newCompetitionFxml.load();
            NewCompetitionController newCompetitionController = newCompetitionFxml.getController();
            newCompetitionController.setMenuBar(menuBarController);
            newCompetitionController.setPerson(person);

            Scene scene = new Scene(parentRoot, 600, 527);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void allProjectChangesMadeScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader allChangedMadeFxml =
                    new FXMLLoader(HelloApplication.class.getResource("allChangesMade.fxml"));
            Parent parentRoot = allChangedMadeFxml.load();
            AllChangesMadeController allChangesMadeController = allChangedMadeFxml.getController();
            allChangesMadeController.setMenuBar(menuBarController);

                Scene scene = new Scene(parentRoot, 929, 511);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void editPersonScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader editPersonFxml =
                    new FXMLLoader(HelloApplication.class.getResource("editPerson.fxml"));
            Parent parentRoot = editPersonFxml.load();
            EditPersonController editPersonController = editPersonFxml.getController();
            editPersonController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 732, 693);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void deletePersonScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader deletePersonFxml =
                    new FXMLLoader(HelloApplication.class.getResource("deletePerson.fxml"));
            Parent parentRoot = deletePersonFxml.load();
            DeletePersonController deletePersonController = deletePersonFxml.getController();
            deletePersonController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 607, 534);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void enterPersonClubIdScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader personClubIdFxml =
                    new FXMLLoader(HelloApplication.class.getResource("enterPersonClubId.fxml"));
            Parent parentRoot = personClubIdFxml.load();
            EnterPersonClubIdController enterPersonClubIdController = personClubIdFxml.getController();
            enterPersonClubIdController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 684, 572);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void checkPersonScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader checkPersonFxml = new FXMLLoader(HelloApplication.class.getResource("checkPerson.fxml"));
            Parent parentRoot = checkPersonFxml.load();
            CheckPersonController checkPersonController = checkPersonFxml.getController();
            checkPersonController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 600, 400);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();


        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void personSearchScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader personSearchFxml = new FXMLLoader(HelloApplication.class.getResource("personSearch.fxml"));
            Parent parentRoot = personSearchFxml.load();
            PersonSearchController personSearchController = personSearchFxml.getController();
            personSearchController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 568, 666);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();


        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void personEachDayExerciseScreen() {
        try {
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader personExerciseFxml =
                    new FXMLLoader(HelloApplication.class.getResource("personEachDayExercises.fxml"));
            Parent parentRoot = personExerciseFxml.load();
            PersonEachDayExercisesController personEachDayExercisesController = personExerciseFxml.getController();
            personEachDayExercisesController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 1360, 623);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void membershipRenewalScreen(){
        try {
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader membershipRenewalFxml =
                    new FXMLLoader(HelloApplication.class.getResource("membershipRenewal.fxml"));
            Parent parentRoot = membershipRenewalFxml.load();
            MembershipRenewalController membershipRenewalController = membershipRenewalFxml.getController();
            membershipRenewalController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 600, 400);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void personCompetitionEdit(){
        try {
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader personCompetitionEditFxml =
                    new FXMLLoader(HelloApplication.class.getResource("personCompetitionEdit.fxml"));
            Parent parentRoot = personCompetitionEditFxml.load();
            PersonCompetitionEditController personCompetitionEditController = personCompetitionEditFxml.getController();
            personCompetitionEditController.setMenuBar(menuBarController);

            Scene scene = new Scene(parentRoot, 732, 611);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPersonExerciseChangeScreen(){
        try{
            MenuBarController menuBarController = openAndUpdateMenuBar();
            FXMLLoader exerciseChangeFxml =
                    new FXMLLoader(HelloApplication.class.getResource("changeOrAddPersonExercise.fxml"));
            Parent root = exerciseChangeFxml.load();
            ChangeOrAddPersonExerciseController controller = exerciseChangeFxml.getController();
            controller.setMenuBar(menuBarController);

            Scene scene = new Scene(root, 600, 650);

            centerScreen(scene);

            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
