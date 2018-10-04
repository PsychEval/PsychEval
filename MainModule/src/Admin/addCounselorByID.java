package Admin;

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

import javax.swing.text.html.Option;
import java.util.Optional;

public class addCounselorByID{

    private String counselorID;
    private Stage mainStage;
    private Scene mainViewScene;

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
        Label headerLabel = new Label("Add Counselor by ID");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Name Label
        Label nameLabel = new Label("Counselor ID ");
        gridPane.add(nameLabel, 0,1);

        // Add Name Text Field
        TextField idField = new TextField();
        idField.setPrefHeight(40);
        gridPane.add(idField, 1,1);


        // Add Name Label
        Label nameLabel2 = new Label("Counselor Name ");
        gridPane.add(nameLabel2, 0,2);

        // Add Name Text Field
        TextField nameField = new TextField();
        idField.setPrefHeight(40);
        gridPane.add(nameField, 1,2);

        // Add Submit Button
        Button submitButton = new Button("Add Counselor");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);


        submitButton.setOnAction(event -> {
            //TODO: ARE YOU SURE?!?!? success/fail
            //code for handling button clicks go here
            counselorID = idField.getText();
            String name = nameField.getText();
            //verifyInput();
            //add to database();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are You Sure?");
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType button = result.orElse(ButtonType.CANCEL);

            if (button == ButtonType.OK) {
                System.out.println("Ok pressed");
                Firebase.setCounselorDB(name, counselorID);
                nameField.setText("");
                idField.setText("");
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                Optional<ButtonType> newResult =  alert1.showAndWait();
                ButtonType button1 = result.orElse((ButtonType.OK));
                if(button1 == ButtonType.OK){
                    mainStage.setScene(mainViewScene);
                }



            } else {
                System.out.println("canceled");
            }



        });
    }
    public Scene getScene() {
        GridPane gp = createFormPane();
        AddUI(gp);
        Scene scene = new Scene(gp, 800,500);
        return scene;
    }

    public addCounselorByID(Stage primaryStage, Scene mainViewScene) {
        mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
    }

//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Create Account");
//        GridPane gp = createFormPane();
//        AddUI(gp);
//        Scene scene = new Scene(gp, 800,500);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }

}