package Counselor;

import Account.Account;
import Utils.Firebase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.*;

public class StudentScoreView {
    private Stage mainStage;
    private Scene mainViewScene;
    private Scene createAccountScene;
    private Account currentUser;
    private TableView<StudentScore> table;

    private VBox createFormPane() {
        TableColumn<StudentScore, String> parentNames = new TableColumn<>("Parent Name");
        parentNames.setMinWidth(200);
        parentNames.setCellValueFactory(new PropertyValueFactory<>("parentName"));

        TableColumn<StudentScore, String> studentNames = new TableColumn<>("Student Name");
        studentNames.setMinWidth(200);
        studentNames.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        TableColumn<StudentScore, Double> studentScore = new TableColumn<>("Student Score");
        studentScore.setMinWidth(200);
        studentScore.setCellValueFactory(new PropertyValueFactory<>("studentScore"));

        table = new TableView<>();
        table.setItems(getStudentScore());
        table.getColumns().addAll(parentNames, studentNames, studentScore);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.setPadding(new Insets(40, 40, 40, 40));
        return vBox;
    }

    private void addUI(VBox tableView) {
        Button goBack = new Button("Back");
        goBack.setOnAction(e -> returnToMainView());
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(30);
        hBox.getChildren().addAll(region1, goBack);

        tableView.getChildren().addAll(hBox);

    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public Scene getScene() {
        VBox cfp = createFormPane();
        addUI(cfp);
        Scene scene = new Scene(cfp, 800, 500);
        return scene;
    }

    public StudentScoreView(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.currentUser = currentUser;
    }

    public void returnToMainView() {
        mainStage.setScene(mainViewScene);
    }

    private ObservableList<StudentScore> getStudentScore() {
        ObservableList<StudentScore> pairs = FXCollections.observableArrayList();
        List<List<String>> finalList = new ArrayList<>();
        String cEmail = currentUser.getEmail();
        Map<String, Object> hm = Firebase.getParents(cEmail);
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            List<String> l = (List<String>) pair.getValue();
            List<String> stuName = Firebase.getStuNameSM(l.get(0));
            if (stuName == null)
                continue;
            for (String each: stuName) {
                long score = Firebase.getRiskFactor(l.get(0), each);
                if (score == -10)
                    continue;
                if (score > 70) {
                    Firebase.setScoreIsBad(l.get(0), each);
                }

                String pName = Firebase.getName(l.get(0));
                List<String> list = new ArrayList<>();
                Collections.addAll(list, pName, each, String.valueOf(score));
                finalList.add(list);
            }
        }
        for (int i = 0; i < finalList.size(); i++) {
            pairs.add(new StudentScore(finalList.get(i).get(0), finalList.get(i).get(1), Integer.parseInt(finalList.get(i).get(2))));
        }

        if (pairs.isEmpty() && currentUser.getAccountType() == Account.AccountType.PARENT) {
            System.out.println("GOT NULL");
            showAlert(Alert.AlertType.ERROR, mainStage, "Error", "Please submit evaluation for your student");
        }
        return pairs;
    }

}
