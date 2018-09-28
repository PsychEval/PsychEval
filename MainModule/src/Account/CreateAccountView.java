package Account;

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

public class CreateAccountView extends Application{

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

        //TODO: For bryan to add
        logInButton.setOnAction(event -> {

        });

        submitButton.setOnAction(event -> {
            //TODO: code for handling button clicks go here
        });
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Account");
        GridPane gp = createFormPane();
        AddUI(gp);
        Scene scene = new Scene(gp, 800,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}