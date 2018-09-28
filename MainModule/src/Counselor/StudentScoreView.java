package Counselor;

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

public class StudentScoreView extends Application {
    //TODO: make this functional
    public void start(Stage primaryStage) {
       primaryStage.setTitle("View Scores");
       GridPane gp = new GridPane();
       Scene scene = new Scene(gp, 800,400);
       primaryStage.setScene(scene);
       primaryStage.show();

    }
}
