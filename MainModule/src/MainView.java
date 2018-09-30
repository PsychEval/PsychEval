import Account.*;
import Admin.*;
import Counselor.*;
import Parent.*;
import Utils.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MainView extends Application {
    public static Account currentUser;

//    private GridPane createFormPane() {
//        //n rows 2 colums
//        GridPane gp = new GridPane();
//        gp.setAlignment(Pos.CENTER);
//        gp.setPadding(new Insets(40,40,40,40));
//        gp.setHgap(10);
//        gp.setVgap(10);
//        ColumnConstraints columnOneConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
//        columnOneConstraints.setHalignment(HPos.RIGHT);
//        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
//        columnTwoConstrains.setHgrow(Priority.ALWAYS);
//        gp.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
//        return gp;
//    }

    private BorderPane createFormPane() {
        BorderPane bp = new BorderPane();
        //bp.setPadding(new Insets(40,40,40,40));
        return bp;
    }

    private HBox createTopBar() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button passwordButton = new Button("Edit Password");
        passwordButton.setPrefSize(100, 20);
        passwordButton.setUserData("password");

        Button logoutButton = new Button("Logout");
        logoutButton.setPrefSize(100, 20);
        logoutButton.setUserData("logout");
        hbox.getChildren().addAll(passwordButton, logoutButton);

        return hbox;
    }

    private Node getByUserData(Pane pane, Object data) {
        for (Node n : pane.getChildren()) {
            if (data.equals(n.getUserData())) {
                return n;
            }
        }
        return null;
    }

    private void AddUI(BorderPane borderPane){ //positioning is important when adding, treat like an array

        Button addCounselor;

        HBox usables = createTopBar();
        borderPane.setTop(usables);
        Button logoutButton = (Button)getByUserData(usables, "logout");
        if(logoutButton == null) {
            System.out.println("could not set button correctly");
        }
        else {
            logoutButton.setOnAction(event -> {
                System.out.println("logout button");
            });
        }
        Button editPasswordButton = (Button)getByUserData(usables, "password");
        if(editPasswordButton == null) {
            System.out.println("could not set button correctly");
        }
        else {
            editPasswordButton.setOnAction(event -> {
                System.out.println("password button");
            });
        }

        if (currentUser.getAccountType() == Account.AccountType.ADMIN) {
            //add counselor button
            addCounselor = new Button("Add Counselor By ID");
            addCounselor.setPrefHeight(40);
            addCounselor.setPrefWidth(200);
            borderPane.setCenter(addCounselor);

        }
        else if (currentUser.getAccountType() == Account.AccountType.COUNSELOR) {

        }
        else {

        }

    }

    public void start(Stage primaryStage) {
        currentUser = new Account("bryan@purdue.edu", "aaaaaa", "Bryan Chiou", Account.AccountType.ADMIN, "aJ23MX");
        primaryStage.setTitle("Main Menu");
        //GridPane gp = createFormPane();
        BorderPane bp = createFormPane();
        AddUI(bp);
        Scene scene = new Scene(bp, 800,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//    public static void main(String[]args) {
//        //TODO get account info from login
//        // set account info
//        //hard coding account for testing purposes
//        currentUser = new Account("bryan@purdue.edu", "aaaaaa", "Bryan Chiou", Account.AccountType.ADMIN, "aJ23MX");
//
//    }
}
