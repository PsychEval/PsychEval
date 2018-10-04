package Account;

import Utils.Firebase;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//import sun.applet.Main;

import java.io.IOException;

public class CreateAccountView extends Application{

    private Stage mainStage;
    private Scene loginScene;
    private Scene createAccountScene;

    private GridPane createFormPane(){
        //n rows 2 colums
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(40,40,40,40));
        gp.setHgap(10);
        gp.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gp;
    }

    private void AddUI(GridPane gridPane){ //positioning is important when adding, treat like an array
        // Add Header
        Label headerLabel = new Label("Create Account");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        //Add account selection label
        Label accSelectLabel = new Label("Type : ");
        gridPane.add(accSelectLabel, 0,1);

        //Add Account Selection Dropdown
        ObservableList<String> accOptions = FXCollections.observableArrayList(
                "Admin", "Counselor", "Parent"
        );
        ComboBox<String> comboBox = new ComboBox<>(accOptions);
        comboBox.setPrefHeight(40);
        gridPane.add(comboBox, 1, 1);

        // Add Name Label
        Label nameLabel = new Label("Name : ");
        gridPane.add(nameLabel, 0,2);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1,2);


        // Add Email Label
        Label emailLabel = new Label("Email : ");
        gridPane.add(emailLabel, 0, 3);

        // Add Email Text Field
        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 3);

        // Add Password Label
        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 4);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 4);

        // Add Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        //Add Login Button
        Button logInButton = new Button("Login");
        logInButton.setPrefHeight(40);
        logInButton.setDefaultButton(true);
        logInButton.setPrefWidth(100);
        gridPane.add(logInButton, 0,6,2,1);

        logInButton.setOnAction(event -> {
            mainStage.setScene(loginScene);
        });

        submitButton.setOnAction(event -> {
            //TODO: error checking for password and email
            String selection = comboBox.getValue();
            String type;
            Account.AccountType temp;
            if(selection.equals("Admin")){
                temp = Account.AccountType.ADMIN;
            }else if(selection.equals("Counselor")){
                temp = Account.AccountType.COUNSELOR;
            }else{
                temp = Account.AccountType.PARENT;
            }
            try {
                Firebase.createAccount(selection, emailField.getText(), nameField.getText(), passwordField.getText());
                Account currentUser = new Account(emailField.getText(), passwordField.getText(), nameField.getText(), temp);
                MainView mainView = new MainView(mainStage, createAccountScene, currentUser);
                Scene mainViewScene = mainView.getScene();
                mainStage.setScene(mainViewScene);
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    public Scene getScene() {
        GridPane gp = createFormPane();
        AddUI(gp);
        Scene scene = new Scene(gp, 800,500);
        return scene;
    }

//    public CreateAccountView(Stage primaryStage, Scene mainViewScene, Scene loginScene) {
//        mainStage = primaryStage;
//        this.mainViewScene = mainViewScene;
//        this.loginScene = loginScene;
//    }

    public void grabScenes() {
        loginForm login = new loginForm(mainStage, createAccountScene);
        loginScene = login.getScene();
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Account");
        GridPane gp = createFormPane();
        AddUI(gp);
        createAccountScene = new Scene(gp, 800,500);
        mainStage = primaryStage;
        grabScenes();
        primaryStage.setScene(createAccountScene);
        primaryStage.show();
    }
    public static void main(String [] args) {
        try {
            Firebase.init(); //intializes the static firebase class
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }


}
