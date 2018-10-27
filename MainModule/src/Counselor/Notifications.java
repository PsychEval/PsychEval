package Counselor;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Notifications implements Runnable {
    private volatile boolean running;
    private Stage window;
    private boolean newRequest;

    public Notifications(Stage primaryStage) {
        running = true;
        window = primaryStage;
        newRequest = false;
    }

    public void terminate() {
        System.out.println("thread stopped");
        running = false;
    }

    @Override
    public void run() {/*
        while(running) {
            try {
                Thread.sleep(1* 30* 1000); //change first number to change num of mins
                //TODO quering the db every min to see if there's new request
            } catch (InterruptedException e) {
                System.out.println("thread stopped");
                running = false;
            }
            Platform.runLater(() -> {
                if (newRequest)
                    showAlert(Alert.AlertType.INFORMATION, window, "New Request", "You have a new parent request!");
            });
        }*/
    }

    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
