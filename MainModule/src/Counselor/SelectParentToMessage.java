package Counselor;

import Account.Account;
import Parent.ParentInfo;
import Account.Messaging;
import Utils.Firebase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class SelectParentToMessage {
    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<ParentInfo> table;
    private Account user;
    private Scene messaging;

    private ObservableList<ParentInfo> getParentInfo() {
        ObservableList<ParentInfo> pairs = FXCollections.observableArrayList();
        Map<String, Object> map;
        map = Firebase.getParents(user.getEmail());
        if (map == null)
            return pairs;

        for (String key : map.keySet()) {
            // map.get(String.valueOf(i));
            ArrayList<Object> temp = (ArrayList<Object>) map.get(key);
            if (temp.get(0) != null)
                pairs.add(new ParentInfo((String)temp.get(0)));
        }

        return pairs;

    }

    private VBox createFormPaneCounselor() {

        TableColumn<ParentInfo, String> parentEmails = new TableColumn<>("Parent Email");
        parentEmails.setMinWidth(200);
        parentEmails.setCellValueFactory(new PropertyValueFactory<>("parentEmail"));

        table = new TableView<>();
        table.setItems(getParentInfo());
        table.getColumns().addAll(parentEmails);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.setPadding(new Insets(40,40,40,40));
        return vBox;
    }

    private void AddUI(VBox tableView) {
        Button selectParent = new Button("Select");
        selectParent.setOnAction(e -> moveToMessageHistory());
        Button goBack = new Button("Back");
        goBack.setOnAction(e -> returnToMainView());
        Region region1 = new Region();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(30);
        hBox.getChildren().addAll(region1, selectParent, goBack);

        tableView.getChildren().addAll(hBox);
    }

    private void moveToMessageHistory(){
        // if selected parent is null, don't do anything
        ObservableList<ParentInfo> selectedPair;
        selectedPair = table.getSelectionModel().getSelectedItems();
        String parentEmail = null;
        for (ParentInfo p : selectedPair) {
            parentEmail = p.getParentEmail();
        }
        if (parentEmail == null) {
            return;
        }
        Messaging m = new Messaging(mainStage, mainViewScene, user, parentEmail);
        messaging = m.getScene();
        mainStage.setScene(messaging);
    }

    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }


    public Scene getScene() {
        VBox vBox = createFormPaneCounselor();
        AddUI(vBox);
        Scene scene = new Scene(vBox, 800,500);
        return scene;
    }

    public SelectParentToMessage(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }
}
