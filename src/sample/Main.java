package sample;

import Enemy.BaseEnemy;
import Tower.FirstTower;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        root.getChildren().add(new BaseEnemy(200 ,0, primaryStage));
        root.getChildren().add(new FirstTower(primaryStage,root));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
