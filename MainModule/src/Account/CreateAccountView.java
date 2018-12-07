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
import javafx.stage.Window;
import org.apache.commons.validator.routines.EmailValidator;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import java.util.regex.Pattern;

//import sun.applet.Main;

public class CreateAccountView extends Application {

    private Stage mainStage;
    private Scene loginScene;
    private Scene createAccountScene;
    private static final Pattern hasUppercase = Pattern.compile("[A-Z]");
    private static final Pattern hasLowercase = Pattern.compile("[a-z]");
    private static final Pattern hasNumber = Pattern.compile("\\d");
    private static final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");

    public static String validateNewPass(String pass1) {
        if (pass1 == null) {
            return "Please enter a password!";
        }

        StringBuilder retVal = new StringBuilder();

        if (pass1.isEmpty()) {
            return "Please enter a password!";
        }

        if (pass1.length() < 4)
            retVal.append("Password is too short. Needs to have 4 characters!\n");

        if (!hasUppercase.matcher(pass1).find())
            retVal.append("Password needs an upper case!\n");

        if (!hasLowercase.matcher(pass1).find())
            retVal.append("Password needs a lowercase!\n");

        if (!hasNumber.matcher(pass1).find())
            retVal.append("Password needs a number!\n");

        if (!hasSpecialChar.matcher(pass1).find())
            retVal.append("Password needs a special character i.e. !,@,#, etc.\n");

        return retVal.toString();
    }

    private GridPane createFormPane() {
        //n rows 2 colums
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(40, 40, 40, 40));
        gp.setHgap(10);
        gp.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gp;
    }

    private void AddUI(GridPane gridPane) { //positioning is important when adding, treat like an array
        // Add Header
        Label headerLabel = new Label("Create Account");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 0, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        //Add account selection label
        Label accSelectLabel = new Label("Type : ");
        gridPane.add(accSelectLabel, 0, 1);

        //Add Account Selection Dropdown
        // check if admin accoutn already exists
        ObservableList<String> accOptions;
        if (Firebase.checkIfAdminExists()) {
            accOptions = FXCollections.observableArrayList(
                    "Counselor", "Parent"
            );
        } else {
            accOptions = FXCollections.observableArrayList(
                    "Admin", "Counselor", "Parent"
            );
        }
        ComboBox<String> comboBox = new ComboBox<>(accOptions);
        comboBox.setPrefHeight(40);
        gridPane.add(comboBox, 1, 1);

        // Add Name Label
        Label nameLabel = new Label("Name : ");
        gridPane.add(nameLabel, 0, 2);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1, 2);


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
//        logInButton.setDefaultButton(true);
        logInButton.setPrefWidth(100);
        gridPane.add(logInButton, 0, 6, 2, 1);

        logInButton.setOnAction(event -> {
            mainStage.setScene(loginScene);
        });

        submitButton.setOnAction(event -> {
            String selection = comboBox.getValue();
            if (selection == null)
                showAlert(Alert.AlertType.ERROR, mainStage, "Error", "Please Select Account Type!");
            Account.AccountType temp;
            if (selection.equals("Admin")) {
                temp = Account.AccountType.ADMIN;
            } else if (selection.equals("Counselor")) {
                temp = Account.AccountType.COUNSELOR;
            } else {
                temp = Account.AccountType.PARENT;
            }
            try {
                // check if all fields are filled
                if (emailField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty()
                        || emailField.getText() == null || passwordField.getText() == null || nameField.getText() == null)
                    showAlert(Alert.AlertType.ERROR, mainStage, "Error", "Please Enter All Fields!");

                    // check if email is invalid
                else if (!EmailValidator.getInstance().isValid(emailField.getText())) {
                    emailField.setText("");
                    showAlert(Alert.AlertType.ERROR, mainStage, "Error", "Invalid Email!");
                }

                // check if password is valid
                else if (!validateNewPass(passwordField.getText()).isEmpty()) {
                    String message = validateNewPass(passwordField.getText());
                    passwordField.setText("");
                    showAlert(Alert.AlertType.ERROR, mainStage, "Error", message);
                }

                //check if admin already added this counselor
                else if (Firebase.isAddedAlready(emailField.getText())) {
                    emailField.setText("");
                    passwordField.setText("");
                    nameField.setText("");
                    showAlert(Alert.AlertType.ERROR, mainStage, "Error", "Counselor Email Is Already Added!");
                } else {
                    Firebase.createAccount(selection, emailField.getText(), nameField.getText(), passwordField.getText());
                    Account currentUser = new Account(emailField.getText(), passwordField.getText(), nameField.getText(), temp);
                    MainView mainView = new MainView(mainStage, createAccountScene, currentUser);
                    Scene mainViewScene = mainView.getScene();

                    emailField.setText("");
                    passwordField.setText("");
                    nameField.setText("");

                    mainStage.setScene(mainViewScene);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    public Scene getScene() {
        GridPane gp = createFormPane();
        AddUI(gp);
        Scene scene = new Scene(gp, 800, 500);
        return scene;
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void grabScenes() {
        loginForm login = new loginForm(mainStage, createAccountScene);
        loginScene = login.getScene();
    }

    static Twitter twitter;
    static RequestToken requestToken;
    public void start(Stage primaryStage) {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("KrKj0MnihSR5cUCXix2aS8aJV", "aaJY6emW1hwjmXPqrQMStjwGWGAcXpuNPvx849PUjBzijSfFVR");
        try {
            requestToken = twitter.getOAuthRequestToken();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Create Account");
        GridPane gp = createFormPane();
        AddUI(gp);
        createAccountScene = new Scene(gp, 800, 500);
        mainStage = primaryStage;
        grabScenes();
        primaryStage.setScene(createAccountScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            Firebase.init(); //intializes the static firebase class
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }


}
