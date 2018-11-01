package Account;

import Counselor.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewMessages {
    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<Message> table;
    private Account user;

    private VBox createFormPane() {
        TableColumn<Message, String> messages = new TableColumn<>("messages");
        messages.setMinWidth(200);
        messages.setCellValueFactory(new PropertyValueFactory<>("message"));

        TableColumn<Message, String> dateAndTime = new TableColumn<>("Date & Time");
        dateAndTime.setMinWidth(200);
        dateAndTime.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));

        table = new TableView<>();
        table.setItems(getParentInfo());
        table.getColumns().addAll(messages, dateAndTime);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.setPadding(new Insets(40,40,40,40));
        return vBox;
    }

    private void AddUI(VBox tableView){
        Button deleteMessage = new Button("Delete Message");
        deleteMessage.setOnAction(e -> deleteMessageFromDB());
        Button goBack = new Button("Back");
        goBack.setOnAction(e -> returnToMainView());
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(30);
        hBox.getChildren().addAll(region1, deleteMessage, goBack);

        tableView.getChildren().addAll(hBox);
    }

    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }

    private void deleteMessageFromDB() {
        //TODO delete message from database given message string and parent email
    }

    private ObservableList<Message> getParentInfo() {
        ObservableList<Message> pairs = FXCollections.observableArrayList();
        // TODO get all messages of a parent given parentemail
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
        pairs.add(new Message("this is a message"));
        return pairs;
    }
    public Scene getScene() {
        VBox vb = createFormPane();
        AddUI(vb);
        Scene scene = new Scene(vb, 800,500);
        return scene;
    }

    public ViewMessages(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }
}
