package Counselor;

import Account.Account;
import Parent.ParentInfo;
import Utils.Firebase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageParent {
    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<ParentInfo> table;
    private Account user;

    private VBox createFormPane() {
        TableColumn<ParentInfo, String> parentNames = new TableColumn<>("Parent Name");
        parentNames.setMinWidth(200);
        parentNames.setCellValueFactory(new PropertyValueFactory<>("parentName"));

        TableColumn<ParentInfo, String> parentEmails = new TableColumn<>("Parent Email");
        parentEmails.setMinWidth(200);
        parentEmails.setCellValueFactory(new PropertyValueFactory<>("parentEmail"));

        table = new TableView<>();
        table.setItems(getParentInfo());
        table.getColumns().addAll(parentNames, parentEmails);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.setPadding(new Insets(40,40,40,40));
        return vBox;
    }

    private void AddUI(VBox tableView){
        // Add Header
        Label messageLabel = new Label("Message: ");

        // Add Name Text Field
        TextField messageBox = new TextField();
        messageBox.setPrefHeight(40);
        HBox.setHgrow(messageBox, Priority.ALWAYS);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage(messageBox));
        Button goBack = new Button("Back");
        goBack.setOnAction(e -> returnToMainView());
        Region region1 = new Region();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(30);
        hBox.getChildren().addAll(messageLabel, messageBox, sendButton, goBack);

        tableView.getChildren().addAll(hBox);


    }
    public void sendMessage(TextField messageField) {
        // if status == 0, means approved, if status == 1, means denied
        String messageString = messageField.getText();
        messageField.setText("");

        ObservableList<ParentInfo> selectedPair;

        selectedPair = table.getSelectionModel().getSelectedItems();
        // TODO send the message to parent based on the parentemail

    }
    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }

    private ObservableList<ParentInfo> getParentInfo() {
        ObservableList<ParentInfo> pairs = FXCollections.observableArrayList();
        // TODO get all parents associated to a counselor
//        Map<String, Object> map = new HashMap<>();
//        map = Firebase.getParents(user.getEmail());
//
//        for (int i = 0; i < map.size(); i++) {
//            // map.get(String.valueOf(i));
//            ArrayList<Object> temp = (ArrayList<Object>) map.get(String.valueOf(i));
//            if(temp.get(1).equals(false)){
//                pairs.add(new ParentStudentPair((String)temp.get(0), false, (String)temp.get(2)));
//            }
//
//        }
        pairs.add(new ParentInfo("parent0", "parent0@gmail.com"));
        pairs.add(new ParentInfo("parent1", "parent1@gmail.com"));
        pairs.add(new ParentInfo("parent2", "parent2@gmail.com"));
        pairs.add(new ParentInfo("parent3", "parent3@gmail.com"));
        return pairs;
    }
    public Scene getScene() {
        VBox vb = createFormPane();
        AddUI(vb);
        Scene scene = new Scene(vb, 800,500);
        return scene;
    }

    public MessageParent(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }
}
