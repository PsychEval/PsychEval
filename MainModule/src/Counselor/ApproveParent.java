package Counselor;

import Account.Account;
import Utils.Firebase;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.sun.org.apache.xpath.internal.operations.Bool;

public class ApproveParent {

    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<ParentStudentPair> table;
    private Account user;

    private VBox createFormPane() {
        TableColumn<ParentStudentPair, String> parentNames = new TableColumn<>("Parent Email");
        parentNames.setMinWidth(200);
        parentNames.setCellValueFactory(new PropertyValueFactory<>("parentEmail"));

        TableColumn<ParentStudentPair, String> studentNames = new TableColumn<>("Student Name");
        studentNames.setMinWidth(200);
        studentNames.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        table = new TableView<>();
        table.setItems(getParentStudentPairs());
        table.getColumns().addAll(parentNames, studentNames);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.setPadding(new Insets(40, 40, 40, 40));
        return vBox;
    }

    private void AddUI(VBox tableView) {
        // Add Header
        Button approve = new Button("Approve");
        approve.setOnAction(e -> buttonClick(0));
        Button deny = new Button("Deny");
        deny.setOnAction(e -> buttonClick(1));
        Button goBack = new Button("Back");
        goBack.setOnAction(e -> returnToMainView());
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(30);
        hBox.getChildren().addAll(approve, deny, region1, goBack);

        tableView.getChildren().addAll(hBox);


    }

    public void buttonClick(int status) {
        // if status == 0, means approved, if status == 1, means denied
        ObservableList<ParentStudentPair> selectedPair, allPairs;
        allPairs = table.getItems();

        selectedPair = table.getSelectionModel().getSelectedItems();
        if (status == 0) {
            for (ParentStudentPair p : selectedPair) {
                Firebase.setParentsApproved(user.getEmail(), p.getParentEmail());
                // TODO add counselor, parent, empty list of messages JSON to new database that holds messages
            }
        } else {
            // TODO make sure function below works
            //Firebase.deleteParent(user.getEmail(), p.getParentName());
        }
        selectedPair.forEach(allPairs::remove);
    }

    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }

    private ObservableList<ParentStudentPair> getParentStudentPairs() {
        ObservableList<ParentStudentPair> pairs = FXCollections.observableArrayList();
        Map<String, Object> map = new HashMap<>();
        map = Firebase.getParents(user.getEmail());
        if (map == null)
            return pairs;

        for (int i = 0; i < map.size(); i++) {
            // map.get(String.valueOf(i));
            ArrayList<Object> temp = (ArrayList<Object>) map.get(String.valueOf(i));
            if (temp.get(1).equals(false)) {
                pairs.add(new ParentStudentPair((String) temp.get(0), false, (String) temp.get(2)));
            }

        }
        return pairs;
    }

    public Scene getScene() {
        VBox vb = createFormPane();
        AddUI(vb);
        Scene scene = new Scene(vb, 800, 500);
        return scene;
    }

    public ApproveParent(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }

}
