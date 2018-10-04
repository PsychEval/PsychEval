package Parent;

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

public class addStudentProfile extends Application{

    private String twitterLink;
    private String facebookLink;
    private String name;

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
        Label headerLabel = new Label("Add Student's Profile");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add Name Label
        Label nameLabel = new Label("Name : ");
        gridPane.add(nameLabel, 0,2);

        // Add Name Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1,2);

        // Add Twitter Label
        Label emailLabel = new Label("Twitter Link : ");
        gridPane.add(emailLabel, 0, 3);

        // Add Twitter Link Text Field
        TextField twitterField = new TextField();
        twitterField.setPrefHeight(40);
        gridPane.add(twitterField, 1, 3);

        // Add Facebook Label
        Label facebookLabel = new Label("Facebook Link : ");
        gridPane.add(facebookLabel, 0, 4);

        // Add Twitter Link Text Field
        TextField facebookField = new TextField();
        facebookField.setPrefHeight(40);
        gridPane.add(facebookField, 1, 4);



        // Add Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);


        submitButton.setOnAction(event -> {
            //code for handling button clicks go here
            twitterLink = twitterField.getText();
            facebookLink = facebookField.getText();
            name = nameField.getText();
            twitterField.setText("");
            facebookField.setText("");
            nameField.setText("");
            //checkInput();
            //saveProfileToDatabase();
        });
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Add Your Student's Profile");
        GridPane gp = createFormPane();
        AddUI(gp);
        Scene scene = new Scene(gp, 800,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}