package Account;

import Utils.Firebase;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.concurrent.ExecutionException;


public class loginForm{

    private Stage mainStage;
    private Scene mainViewScene;
    private Account currentUser;
    private Scene createAccountScene;
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setTitle("Login Form JavaFX Application");
//
//        // Create the registration form grid pane
//        GridPane gridPane = createRegistrationFormPane();
//        // Add UI controls to the registration form grid pane
//        addUIControls(gridPane);
//        // Create a scene with registration form grid pane as the root node
//        Scene scene = new Scene(gridPane, 800, 500);
//        // Set the scene in primary stage
//        primaryStage.setScene(scene);
//
//        primaryStage.show();
//    }


    private GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Login");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Email Label
        Label emailLabel = new Label("Email ID : ");
        gridPane.add(emailLabel, 0, 2);

        // Add Email Text Field
        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 2);

        // Add Password Label
        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 3);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 3);

        // Add Submit Button
        Button submitButton = new Button("Login");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        Button editPassButton = new Button("Change Password");
        editPassButton.setPrefHeight(40);
        editPassButton.setPrefWidth(100);
        gridPane.add(editPassButton,0,5,2,1);
        GridPane.setHalignment(editPassButton, HPos.CENTER);
        GridPane.setMargin(editPassButton, new Insets(20, 0,20,0));


        editPassButton.setOnAction(event -> {
            EditPassword ep = new EditPassword(mainStage, mainViewScene);
            Scene eps = ep.getScene();
            mainStage.setScene(eps);
        });

        submitButton.setOnAction(event -> {
                try {
                if(Firebase.login(emailField.getText(), passwordField.getText())){
                    Account.AccountType tempA;
                    String type = Firebase.getType(emailField.getText());
                    if(type.equals("Admin")){
                        tempA = Account.AccountType.ADMIN;
                    }else if(type.equals("Counselor")){
                        tempA = Account.AccountType.COUNSELOR;
                    }else{
                        tempA  = Account.AccountType.PARENT;
                    }
                    String name = Firebase.getName(emailField.getText());
                    String pass = passwordField.getText();
                    System.out.println(type + " "  + name);
                    currentUser = new Account(emailField.getText(), pass, name, tempA);
                    MainView mv = new MainView(mainStage, createAccountScene, currentUser);
                    mainViewScene = mv.getScene();

                    emailField.setText("");
                    passwordField.setText("");

                    mainStage.setScene(mainViewScene);
                }else{
                    showAlert(Alert.AlertType.ERROR, mainStage, "Error", "Incorrect Username or Password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //code for handling button clicks go here

        });
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public Scene getScene() {
        GridPane gp = createRegistrationFormPane();
        addUIControls(gp);
        Scene scene = new Scene(gp, 800,500);
        return scene;
    }

    public loginForm(Stage primaryStage, Scene createAccountScene) {
        mainStage = primaryStage;
        this.createAccountScene = createAccountScene;
    }

}