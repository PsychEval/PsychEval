package Counselor;

import Account.Account;
import Account.MainView;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StudentScoreView{
    //TODO: make this functional
    private Stage mainStage;
    private Scene mainViewScene;
    private Scene createAccountScene;

    private GridPane createGP() {
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

    private void addFields(GridPane gp){
        int i = 0;
        try {
            ArrayList<String> sNames = Firebase.getStudentNames(MainView.currentUser.getEmail());
            if(sNames == null){
                System.out.println("GOT NULL");
                return;
            }
            for (String name : sNames) {
                Label nameLabel = new Label(name);
                gp.add(nameLabel, 0, i);
                Label rf = new Label(Firebase.getRiskFactor(name));
                gp.add(rf, 1, i);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button back = new Button("Go Back");
        back.setPrefHeight(40);
        back.setPrefWidth(100);
        gp.add(back, 0, i, 2, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        GridPane.setMargin(back, new Insets(20, 0,20,0));

        back.setOnAction(event -> {
            MainView mv = new MainView(mainStage, mainViewScene, MainView.currentUser);
            Scene mvs = mv.getScene();
            mainStage.setScene(mvs);
        });

    }

    public Scene getScene(){
        GridPane gp = createGP();
        addFields(gp);
        Scene scene = new Scene(gp, 800, 500);
        return scene;
    }

    public StudentScoreView(Stage primaryStage, Scene createAccountScene) {
        mainStage = primaryStage;
        this.createAccountScene = createAccountScene;
    }

}
