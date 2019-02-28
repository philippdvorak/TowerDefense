package sample;

import Enemy.BaseEnemy;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreen(true);
        primaryStage.show();

        root.getChildren().add(new BaseEnemy(200 ,0, primaryStage));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
