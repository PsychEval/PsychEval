package Parent;

import Account.Account;
import Utils.Firebase;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;

public class LinkWithAStudent{

    private String counselorEmail;
    private String studentName;
    private Stage mainStage;
    private Scene mainViewScene;
    private Account user;

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
        Label headerLabel = new Label("Link With Your Child");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label counselorEmailLabel = new Label("Counselor Email : ");
        counselorEmailLabel.setPrefWidth(300);
        gridPane.add(counselorEmailLabel, 0,2);

        TextField counselorEmailField = new TextField();
        counselorEmailField.setPrefHeight(40);
        gridPane.add(counselorEmailField, 1,2);

        Label childNameLabel = new Label("Your Child's Name : ");
        childNameLabel.setPrefWidth(300);
        gridPane.add(childNameLabel, 0, 3);

        TextField childNameField = new TextField();
        childNameField.setPrefHeight(40);
        gridPane.add(childNameField, 1, 3);

        // Add Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);


        //Add back button
        Button goBack = new Button("Back");
        goBack.setPrefHeight(40);
        goBack.setPrefWidth(100);
        gridPane.add(goBack, 0, 6, 2, 1);

        goBack.setOnAction(e -> mainStage.setScene(mainViewScene));

        submitButton.setOnAction(event -> {
            counselorEmail = counselorEmailField.getText();
            studentName = childNameField.getText();
            Map<Integer, Object> m;
            m = new HashMap<>();
            m.put(0, user.getEmail());
            m.put(1, false);
            m.put(2, studentName);
            m.put(3, false);
            Firebase.setParents(counselorEmail, m);
            showAlert(Alert.AlertType.INFORMATION, mainStage, "Success", "Counselor will review your request");
            mainStage.setScene(mainViewScene);
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
        GridPane gp = createFormPane();
        AddUI(gp);
        Scene scene = new Scene(gp, 800,500);
        return scene;
    }

    public LinkWithAStudent(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }

//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Add Your Student's Profile");
//        GridPane gp = createFormPane();
//        AddUI(gp);
//        Scene scene = new Scene(gp, 800,500);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
}