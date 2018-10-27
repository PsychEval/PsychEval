package Account;

import Admin.addCounselorByID;
import Counselor.ApproveParent;
import Counselor.Notifications;
import Counselor.StudentScoreView;
import Parent.LinkWithAStudent;
import Utils.Firebase;
import Utils.Oauth;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//import sun.applet.Main;

/*
IMPORTANT READ: The UI layout of this main menu is a border layout which consists of
                top, center, bottom, left, and right. In those sections I have a HBox
                layout in the top and a Gridlayout in the center
                ---------------------------------------------------------------------
*/
public class MainView{
    public static Account currentUser;
    private Stage window;
    private Scene editPass;
    private Scene mainScene;
    private Scene adminAddsCounselor;
    private Scene createAccount;
    private Scene scoreView;
    private Scene login;
    private Scene approveParent;
    private Scene linkStudent;
    private Notifications notif;
    private Thread t1;

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
                        if (currentUser.getAccountType() == Account.AccountType.COUNSELOR) {
//                            notif.terminate();
                        }
                        MainView.currentUser = null;
                        window.setScene(createAccount);

                    });
                }
                Button editPasswordButton = (Button) getByUserData(pane, "password");
                if (editPasswordButton == null) {
                    System.out.println("could not set editpassword button correctly");
                } else {
                    editPasswordButton.setOnAction(event -> window.setScene(editPass));
                }

            }
            if (pane.getUserData().equals("Middle")) {
                Button addCounselorButton = (Button) getByUserData(pane, "addCounselor");
                if (addCounselorButton == null) {
                    System.out.println("addCounselor button was not found");
                } else {
                    addCounselorButton.setOnAction(event -> {
                        System.out.println("addCounselor button");
                        window.setScene(adminAddsCounselor);
                    });
                }
                Button viewScoreButton = (Button) getByUserData(pane, "viewScore");
                if (viewScoreButton == null) {
                    System.out.println("view score button was not found");
                } else {
                    viewScoreButton.setOnAction(event -> {
                        ChangeSceneToScoreView();
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
                Button approveParentButton = (Button)getByUserData(pane, "approveParent");
                if (approveParentButton == null) {
                    System.out.println("Approve parent button was not found");
                } else {
                    approveParentButton.setOnAction(event -> {
                        System.out.println("approve parent button");
                        ChangeSceneToApproveView();
                    });
                }
                Button addStudent = (Button)getByUserData(pane, "addStudent");
                if (addStudent == null) {
                    System.out.println("add student button was not found");
                } else {
                    addStudent.setOnAction(event -> {
                        System.out.println("add student button");
                        window.setScene(linkStudent);
                    });
                }
                Button oauth = (Button)getByUserData(pane, "oauth");
                if (oauth == null) {
                    System.out.println("oauth button was not found");
                } else {
                    oauth.setOnAction(event -> {
                        System.out.println("oauth button");
                        Oauth oauth1 = new Oauth();
                        try {
                            oauth1.launchOauth();
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
                Button approvedYetButton = (Button)getByUserData(pane, "approvedYet");
                if (approvedYetButton == null) {
                    System.out.println("approved yet button was not found");
                } else {
                    approvedYetButton.setOnAction(event -> {
                        System.out.println("approved yet button");
                        // TODO get form db
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Counselor Email");
                        dialog.setHeaderText("Enter email");
                        dialog.setContentText("Email:");
                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()){
                            //System.out.println("AHHHHHHH   " + result.get() + " " + currentUser.getName());
                            boolean temp = Firebase.isApproved(result.get(), currentUser.getName());
                            if(temp){
                                showAlert(Alert.AlertType.INFORMATION, window, "Approved", "You are approved");
                            }else{
                                showAlert(Alert.AlertType.INFORMATION, window, "Not Approved", "You are not approved");
                            }
                        }


                        //showAlert(Alert.AlertType.INFORMATION, window, "Pending", "Counselor is still reviewing your request.");
                    });
                }
            }
        }
    }
    private void AddUI(BorderPane borderPane){ //positioning is important when adding, treat like an array

        if (borderPane != null) {
            Button addCounselor, viewScores, viewProfileRequests, addStudent, oauth, approveParent, approvedYet;

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

                approveParent = new Button("Approve Parent");
                approveParent.setPrefHeight(40);
                approveParent.setPrefWidth(200);
                approveParent.setUserData("approveParent");
                gridPane.add(approveParent, 4, 0, 2, 1);


            } else {
                //account type is parent
                addStudent = new Button("Link With Child");
                addStudent.setPrefHeight(40);
                addStudent.setPrefWidth(200);
                addStudent.setUserData("addStudent");
                gridPane.add(addStudent, 0, 0, 2, 1);

                oauth = new Button("oauth");
                oauth.setPrefHeight(40);
                oauth.setPrefWidth(200);
                oauth.setUserData("oauth");
                gridPane.add(oauth, 2, 0, 2, 1);

                approvedYet = new Button("Am I Approved?");
                approvedYet.setPrefHeight(40);
                approvedYet.setPrefWidth(200);
                approvedYet.setUserData("approvedYet");
                gridPane.add(approvedYet, 4, 0, 2, 1);

                viewScores = new Button("View Scores");
                viewScores.setPrefHeight(40);
                viewScores.setPrefWidth(200);
                viewScores.setUserData("viewScore");
                gridPane.add(viewScores, 2, 3, 2, 1);
            }

            borderPane.setCenter(gridPane);
            setButtonListeners(gridPane);
        }
    }

    public Scene getScene() {
        return this.mainScene;
    }

    public void grabScenes() {
        //CreateAccountView cav = new CreateAccountView(window, mainScene, login);
        // will crash if login is null / if there was a problem creating login scene
        //createAccount = cav.getScene();
        EditPassword ep = new EditPassword(window, mainScene);
        editPass = ep.getScene();
        addCounselorByID acbID = new addCounselorByID(window, mainScene);
        adminAddsCounselor = acbID.getScene();
        //StudentScoreView ssv = new StudentScoreView(window, mainScene);
        //scoreView = ssv.getScene();

        LinkWithAStudent lwas = new LinkWithAStudent(window, mainScene, currentUser);
        linkStudent = lwas.getScene();
    }

    public void ChangeSceneToScoreView(){
        StudentScoreView ssv = new StudentScoreView(window, mainScene, currentUser);
        scoreView = ssv.getScene();
        window.setScene(scoreView);
    }

    public void ChangeSceneToApproveView(){
        ApproveParent ap = new ApproveParent(window, mainScene, currentUser);
        approveParent = ap.getScene();
        window.setScene(approveParent);
    }

    public MainView(Stage primaryStage, Scene createAccount, Account currUser) {
        this.window = primaryStage;
        this.currentUser = currUser;
        this.createAccount = createAccount;
        BorderPane bp = createFormPane();
        AddUI(bp);
        this.mainScene = new Scene(bp, 800,500);
        grabScenes();
        startNotificationThreads();
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public void startNotificationThreads() {
        if (currentUser.getAccountType() == Account.AccountType.COUNSELOR) {
            // notify if there is a new parent approval request
//            notif = new Notifications(window);
//            t1 = new Thread(notif);
//            t1.start();
            Firebase.checkForNewParents("aaa", window);
            Map<Integer, Object> m = new HashMap<>();
            m.put(0, "QWERTYUYTREWQ");
            m.put(1, false);
            m.put(2, "1234567890");
            Firebase.setParents("aaa", m);
        }
    }
}
