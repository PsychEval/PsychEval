package Account;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class oauthform {

    private Stage mainstage;
    private Scene mainviewscene;
    private Account currentuser;

    public oauthform(Stage primary, Scene mainviewscene, Account currentuser){
        this.mainstage = primary;
        this.mainviewscene = mainviewscene;
        this.currentuser= currentuser;
    }

    public Scene getScene(){
        GridPane gp = createPane();
        addUI(gp);
        Scene scene = new Scene(gp, 800, 500);
        return scene;
    }

    private GridPane createPane(){
        GridPane gp = new GridPane();

        gp.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gp.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gp.setHgap(10);

        // Set the vertical gap between rows
        gp.setVgap(10);

        return gp;
    }

    private void addUI(GridPane gp){
        Button showOauth = new Button("Link Twitter");
        showOauth.setPrefWidth(100);
        showOauth.setPrefHeight(40);
        gp.add(showOauth,0,0);
        GridPane.setHalignment(showOauth, HPos.CENTER);


        showOauth.setOnAction(event -> {
            //TODO: add functionality
        });

        TextField rishCode = new TextField();
        rishCode.setPrefHeight(40);
        gp.add(rishCode, 0, 1);

        Button submitButton = new Button("Submit");
        submitButton.setPrefWidth(100);
        submitButton.setPrefHeight(40);
        gp.add(submitButton,0,2);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        submitButton.setOnAction(event -> {
            //TODO: add functionality
        });

    }
}
