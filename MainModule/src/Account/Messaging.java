package Account;

import Account.Account;
import Parent.ParentInfo;
import Utils.Firebase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
import java.util.List;
import java.util.Map;

public class Messaging {
    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<Message> table;
    private Account user;
    private String parentEmail;
    private String counselorEmail;

    private VBox createFormPaneAllMessages() {
        TableColumn<Message, String> message = new TableColumn<>("Message");
        message.setMinWidth(200);
        message.setCellValueFactory(new PropertyValueFactory<>("message"));

        TableColumn<Message, String> sentBy = new TableColumn<>("Sent By");
        sentBy.setMinWidth(200);
        sentBy.setCellValueFactory(new PropertyValueFactory<>("sentByWho"));
        table = new TableView<>();
        table.setItems(getAllMessages());
        table.getColumns().addAll(message, sentBy);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.setPadding(new Insets(40,40,40,40));
        return vBox;
    }

    private void populateMessageHistory(VBox tableView){
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

        if (user.getAccountType() == Account.AccountType.COUNSELOR) {
            Firebase.newMessage(user.getEmail(), parentEmail, 0, messageString);
        }
        else
            Firebase.newMessage(counselorEmail, user.getEmail(),1, messageString);
//            Firebase.newMessage(Firebase.getCounselorEmail(parentEmail), parentEmail,1, messageString);


        Messaging m = new Messaging(mainStage, mainViewScene, user, parentEmail);
        Scene messaging = m.getScene();
        mainStage.setScene(messaging);
    }
    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }

    private ObservableList<Message> getAllMessages() {
        ObservableList<Message> pairs = FXCollections.observableArrayList();
        Map<String, Object> map;
//        String counselorEmail;
        List<List<Object>> listOfMessages;
        if (user.getAccountType() == Account.AccountType.PARENT)
            listOfMessages = Firebase.getMessages(counselorEmail, user.getEmail());
//            counselorEmail = Firebase.getCounselorEmail(user.getEmail());
        else
            listOfMessages = Firebase.getMessages(user.getEmail(), parentEmail);
//            counselorEmail = user.getEmail();
//        List<List<Object>> listOfMessages = Firebase.getMessages(counselorEmail, parentEmail);

        if (listOfMessages == null)
            return pairs;
        for (List<Object> e : listOfMessages) {
            if((long)e.get(1) == 1)
                pairs.add(new Message((String)e.get(0), "Sent By Parent"));
            else
                pairs.add(new Message((String)e.get(0), "Sent By Counselor"));
        }

        return pairs;

    }
    public Scene getScene() {
        VBox vBox = createFormPaneAllMessages();
        populateMessageHistory(vBox);
        Scene scene = new Scene(vBox, 800,500);
        return scene;
    }

    public Messaging(Stage primaryStage, Scene mainViewScene, Account currentUser, String parentEmail) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
//        this.parentEmail = parentEmail;
        if (currentUser.getAccountType() == Account.AccountType.PARENT) {
            this.counselorEmail = parentEmail;
            this.parentEmail = "";
        }
        else {
            this.counselorEmail = "";
            this.parentEmail = parentEmail;
        }
    }
}

