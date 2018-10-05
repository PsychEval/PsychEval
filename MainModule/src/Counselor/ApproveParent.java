package Counselor;

import Account.Account;
import Utils.Firebase;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javafx.application.Application.launch;

public class ApproveParent{

    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<ParentStudentPair> table;
    private Account user;

    private VBox createFormPane() {
        TableColumn<ParentStudentPair, String> parentNames = new TableColumn<>("Parent Name");
        parentNames.setMinWidth(200);
        parentNames.setCellValueFactory(new PropertyValueFactory<>("parentName"));

        TableColumn<ParentStudentPair, String> studentNames = new TableColumn<>("Student Name");
        studentNames.setMinWidth(200);
        studentNames.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        table = new TableView<>();
        table.setItems(getParentStudentPairs());
        table.getColumns().addAll(parentNames, studentNames);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.setPadding(new Insets(40,40,40,40));
        return vBox;
    }

    private void AddUI(VBox tableView){
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
        hBox.setPadding(new Insets(10,10,10,10));
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
                Firebase.setParentsApproved(user.getEmail(), p.getParentName());
            }
        }
        else {
            // The database doesn't update the parent's approval. It's simply removed from list
        }
        selectedPair.forEach(allPairs::remove);
    }
    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }

    private ObservableList<ParentStudentPair> getParentStudentPairs() {
        ObservableList<ParentStudentPair> pairs = FXCollections.observableArrayList();
        // TODO get parents list from db
        Map<String, Object> map = new HashMap<>();
        map = Firebase.getParents(user.getEmail());

        for (int i = 0; i < map.size(); i++) {
           // map.get(String.valueOf(i));
            ArrayList<Object> temp = (ArrayList<Object>) map.get(String.valueOf(i));
            if(temp.get(1).equals(false)){
                pairs.add(new ParentStudentPair((String)temp.get(0), false, (String)temp.get(2)));
            }

        }
        //pairs.add(new ParentStudentPair("parent0", false, "student0"));
        //pairs.add(new ParentStudentPair("parent1", false,"student1"));
        //pairs.add(new ParentStudentPair("parent2", false,"student2"));
        //pairs.add(new ParentStudentPair("parent3", false, "student3"));
        return pairs;
    }
    public Scene getScene() {
        VBox vb = createFormPane();
        AddUI(vb);
        Scene scene = new Scene(vb, 800,500);
        return scene;
    }

    public ApproveParent(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }

}
