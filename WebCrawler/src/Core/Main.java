package Core;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    private GridPane createGridPane(){
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(40,40,40,40));
        gp.setHgap(10);
        gp.setVgap(10);
        return gp;
    }

    private void AddUi(GridPane gp){
        Button button = new Button("Run Crawler");
        button.setPrefHeight(40);
        button.setPrefWidth(100);
        button.setDefaultButton(true);
        GridPane.setHalignment(button, HPos.CENTER);
        gp.add(button, 0,0);

        button.setOnAction(event -> {
            try {
                mainLoop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        firebase.init();
        primaryStage.setTitle("Server Test");
        GridPane gp = createGridPane();
        AddUi(gp);
        Scene scene = new Scene(gp, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //controlling function
    void mainLoop() throws IOException {
        TweetPuller tweetPuller = new TweetPuller();
        tweetPuller.pullTweets();


    }
}
