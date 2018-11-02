package Counselor;

import Account.Account;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Notifications{
    private Stage window;
    private Scene mainViewScene;
    private Account user;

    public Notifications(Stage primaryStage, Scene mainViewScene, Account currentUser) {
        this.window = primaryStage;
        this.mainViewScene = mainViewScene;
        this.user = currentUser;
    }

    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
    public void refreshApproveList() {
        ApproveParent ap = new ApproveParent(window, mainViewScene, user);
        Scene approveParent = ap.getScene();
        window.setScene(approveParent);
    }


}
