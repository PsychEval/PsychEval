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
import java.util.Map;

public class Messaging {
    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<Message> table;
    private Account user;
    private String parentEmail;

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
            //ObservableList<ParentInfo> selectedPair;
            //selectedPair = table.getSelectionModel().getSelectedItems();
            // TODO send the message to parent based on the parentemail
            // Firebase.addMessage(user.getEmail(), selectedPair.get(0).getParentEmail, messageString);
        }

    }
    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }

    private ObservableList<Message> getAllMessages() {
        ObservableList<Message> pairs = FXCollections.observableArrayList();
        Map<String, Object> map;
        String counselorEmail;
        if (user.getAccountType() == Account.AccountType.PARENT)
            counselorEmail = Firebase.getCounselorEmail(user.getEmail());

        //TODO get messages given counselor email and parent email

//        if (map == null)
//            return pairs;

//        for (String key : map.keySet()) {
//            // map.get(String.valueOf(i));
//            ArrayList<Object> temp = (ArrayList<Object>) map.get(key);
//            if (temp.get(0) != null)
//                pairs.add(new Message((String)temp.get(0), (String)temp.get(1)));
//        }
        pairs.add(new Message("message 0 ", "sent by counselor"));
        pairs.add(new Message("message 1" , "sent by parent"));
        pairs.add(new Message("message 2", "sent by counselor"));
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
        this.parentEmail = parentEmail;
    }
}

