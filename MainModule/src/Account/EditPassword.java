package Account;

import Utils.Firebase;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class EditPassword {

    private Stage mainStage;
    private Scene mainViewScene;

    private GridPane createFormPane(){
        //n rows 2 colums
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(40,40,40,40));
        gp.setHgap(10);
        gp.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gp;
    }

    private void AddUI(GridPane gridPane){ //positioning is important when adding, treat like an array
        // Add Header
        Label headerLabel = new Label("Change Password");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Name Label
        Label oldPassLabel = new Label("Enter Old Password: ");
        gridPane.add(oldPassLabel, 0,1);

        // Add Name Text Field
        PasswordField oldPassField = new PasswordField();
        oldPassField.setPrefHeight(40);
        gridPane.add(oldPassField, 1,1);


        // Add Email Label
        Label newPassLabel = new Label("Enter New Password: ");
        gridPane.add(newPassLabel, 0, 2);

        // Add Email Text Field
        PasswordField newPassField = new PasswordField();
        newPassField.setPrefHeight(40);
        gridPane.add(newPassField, 1, 2);

        // Add Password Label
        Label newPass2Label = new Label("Re-enter New Password:");
        gridPane.add(newPass2Label, 0, 3);

        // Add Password Field
        PasswordField newPass2Field = new PasswordField();
        newPass2Field.setPrefHeight(40);
        gridPane.add(newPass2Field, 1, 3);

        // Add Submit Button
        Button submitButton = new Button("Change Password");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(200);
        gridPane.add(submitButton, 0, 4, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        Text errorMessage = new Text("");
        errorMessage.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gridPane.add(errorMessage, 0 ,5, 2,1);
        GridPane.setHalignment(errorMessage, HPos.CENTER);

        submitButton.setOnAction(event -> {
            //code for handling button clicks go here
            //checkInput()
            //TODO: SHOW SUCCESS ALERT
            String passToChange = newPass2Field.getText();
            String message = CreateAccountView.validateNewPass(passToChange);
            if (!message.isEmpty()) {
                errorMessage.setFill(Color.RED);
                errorMessage.setText(message);
            }
            boolean validInput = checkInput(oldPassField, newPassField, newPass2Field);
            if (!validInput) {
                errorMessage.setFill(Color.RED);
                errorMessage.setText("Please Provide Valid Input!");
            }
            else {
                // change in db()
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("You did it!");
                alert.show();
                Firebase.setPassword(MainView.currentUser.getEmail(), passToChange);
                System.out.println("here it works");
                mainStage.setScene(mainViewScene);
                // return to main

            }
        });
    }

    public boolean checkInput(TextField oldPassField, PasswordField newPassField, PasswordField newPass2Field) {
        String oldPass = oldPassField.getText();
        oldPassField.setText("");
        String newPass = newPassField.getText();
        newPassField.setText("");
        String newPass2 = newPass2Field.getText();
        newPass2Field.setText("");
        // check if old pass the same as account's old pass
        if(!oldPass.equals(MainView.currentUser.getPassword()))
        if (!newPass.equals(newPass2)) {
            return false;
        }
        return true;
    }

    public Scene getScene() {
        GridPane gp = createFormPane();
        AddUI(gp);
        Scene scene = new Scene(gp, 800,500);
        return scene;
    }

    public EditPassword(Stage primaryStage, Scene mainViewScene) {
        mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
    }
}
