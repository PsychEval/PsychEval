package Parent;
import Account.Account;
import Account.Messaging;
import Counselor.CounselorInfo;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectCounselorToMessage {
    private Stage mainStage;
    private Scene mainViewScene;
    private TableView<CounselorInfo> table;
    private Account user;
    private Scene messaging;
    private ObservableList<CounselorInfo> getCounselorInfo() {
        ObservableList<CounselorInfo> pairs = FXCollections.observableArrayList();
        //Map<String, Object> map;
        List<String> cEmails = Firebase.getCounselorEmails(user.getEmail());
        if (cEmails == null)
            return pairs;
//        map = Firebase.getParents(user.getEmail());
//        if (map == null)
//            return pairs;
        //        for (String key : map.keySet()) {
//            // map.get(String.valueOf(i));
//            ArrayList<Object> temp = (ArrayList<Object>) map.get(key);
//            if (temp.get(0) != null)
//                pairs.add(new CounselorInfo((String)temp.get(0)));
//        }
        for (String each : cEmails) {
            pairs.add(new CounselorInfo(each));
        }
        return pairs;
    }
    private VBox createFormPaneCounselor() {
        TableColumn<CounselorInfo, String> counselorEmails = new TableColumn<>("Counselor Email");
        counselorEmails.setMinWidth(200);
        counselorEmails.setCellValueFactory(new PropertyValueFactory<>("counselorEmail"));
        table = new TableView<>();
        table.setItems(getCounselorInfo());
        table.getColumns().addAll(counselorEmails);
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
        ObservableList<CounselorInfo> selectedPair;
        selectedPair = table.getSelectionModel().getSelectedItems();
        String counselorEmail = null;
        for (CounselorInfo p : selectedPair) {
            counselorEmail = p.getCounselorEmail();
        }
        if (counselorEmail == null) {
            return;
        }
        Messaging m = new Messaging(mainStage, mainViewScene, user, counselorEmail);
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
    public SelectCounselorToMessage(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.mainStage = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }
}