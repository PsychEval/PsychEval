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
import java.util.Objects;

public class MainView extends Application {
    public static Account currentUser;

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

    private GridPane createMiddle() {
        GridPane gp = new GridPane();
        //gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(40,40,40,40));
        gp.setHgap(9);
        gp.setVgap(9);
        gp.setPrefSize(400, 200);
//        ColumnConstraints columnOneConstraints = new ColumnConstraints(200, 200, Double.MAX_VALUE);
//        columnOneConstraints.setHalignment(HPos.RIGHT);
//        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
//        //columnTwoConstrains.setHgrow(Priority.ALWAYS);
//        gp.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gp;
    }

    private Node getByUserData(Pane pane, Object data) {
        for (Node n : pane.getChildren()) {
            if (data.equals(n.getUserData())) {
                return n;
            }
        }
        return null;
    }

    private void setButtonListeners(Pane pane) {
        if(pane != null) {
            if (pane.getUserData().equals("Top")) {
                Button logoutButton = (Button) getByUserData(pane, "logout");
                if (logoutButton == null) {
                    System.out.println("could not set logout button correctly");
                } else {
                    logoutButton.setOnAction(event -> {
                        System.out.println("logout button");
                    });
                }
                Button editPasswordButton = (Button) getByUserData(pane, "password");
                if (editPasswordButton == null) {
                    System.out.println("could not set editpassword button correctly");
                } else {
                    editPasswordButton.setOnAction(event -> {
                        System.out.println("password button");
                    });
                }

            }
            if (pane.getUserData().equals("Middle")) {
                Button addCounselorButton = (Button) getByUserData(pane, "addCounselor");
                if (addCounselorButton == null) {
                    System.out.println("addCounselor button was not found");
                } else {
                    addCounselorButton.setOnAction(event -> {
                        System.out.println("addCounselor button");
                    });
                }
                Button viewScoreButton = (Button) getByUserData(pane, "viewScore");
                if (viewScoreButton == null) {
                    System.out.println("view score button was not found");
                } else {
                    viewScoreButton.setOnAction(event -> {
                        System.out.println("view score button");
                    });
                }
                Button viewProfileRequests = (Button) getByUserData(pane, "viewProfileRequests");
                if (viewProfileRequests == null) {
                    System.out.println("view profile button was not found");
                } else {
                    viewProfileRequests.setOnAction(event -> {
                        System.out.println("view profile reqeusts button");
                    });
                }
                Button addStudent = (Button)getByUserData(pane, "addStudent");
                if (addStudent == null) {
                    System.out.println("add student button was not found");
                } else {
                    addStudent.setOnAction(event -> {
                        System.out.println("add student button");
                    });
                }
                Button oauth = (Button)getByUserData(pane, "oauth");
                if (oauth == null) {
                    System.out.println("oauth button was not found");
                } else {
                    oauth.setOnAction(event -> {
                        System.out.println("oauth button");
                    });
                }
            }
        }
    }
    private void AddUI(BorderPane borderPane){ //positioning is important when adding, treat like an array

        if (borderPane != null) {
            Button addCounselor, viewScores, viewProfileRequests, addStudent, oauth;

            HBox usables = createTopBar();
            usables.setUserData("Top");
            borderPane.setTop(usables);
            setButtonListeners(usables);

            GridPane gridPane = createMiddle();
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setUserData("Middle");

            if (currentUser.getAccountType() == Account.AccountType.ADMIN) {
                //add counselor button
                addCounselor = new Button("Add Counselor By ID");
                addCounselor.setPrefHeight(40);
                addCounselor.setPrefWidth(200);
                addCounselor.setUserData("addCounselor");
                gridPane.add(addCounselor, 0, 0, 2, 1);
                //gridPane.setGridLinesVisible(true);
                GridPane.setHalignment(addCounselor, HPos.CENTER);

            } else if (currentUser.getAccountType() == Account.AccountType.COUNSELOR) {
                viewScores = new Button("View Scores");
                viewScores.setPrefHeight(40);
                viewScores.setPrefWidth(200);
                viewScores.setUserData("viewScore");
                gridPane.add(viewScores, 0, 0, 2, 1);

                viewProfileRequests = new Button("View Profile Requests");
                viewProfileRequests.setPrefHeight(40);
                viewProfileRequests.setPrefWidth(200);
                viewProfileRequests.setUserData("viewProfileRequests");
                gridPane.add(viewProfileRequests, 2, 0, 2, 1);

            } else {
                //account type is parent
                addStudent = new Button("Add Student");
                addStudent.setPrefHeight(40);
                addStudent.setPrefWidth(200);
                addStudent.setUserData("addStudent");
                gridPane.add(addStudent, 0, 0, 2, 1);

                oauth = new Button("oauth");
                oauth.setPrefHeight(40);
                oauth.setPrefWidth(200);
                oauth.setUserData("oauth");
                gridPane.add(oauth, 2, 0, 2, 1);
            }

            borderPane.setCenter(gridPane);
            setButtonListeners(gridPane);
        }
    }

    public void start(Stage primaryStage) {
        currentUser = new Account("bryan@purdue.edu", "aaaaaa", "Bryan Chiou", Account.AccountType.PARENT, "aJ23MX");
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
