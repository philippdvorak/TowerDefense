package sample;

import Enemy.BaseEnemy;
import Tower.FirstTower;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Vector;

public class Main extends Application {

    private Vector<BaseEnemy> enemyVector= new Vector<>();

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Tower Defense");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreen(true);
        primaryStage.show();

        root.getChildren().add(new BaseEnemy(primaryStage));
        root.getChildren().add(new FirstTower(primaryStage,root));
        root.getChildren().add(new BaseEnemy(primaryStage));
        for(Node tmp : root.getChildren()){
            enemyVector.add((BaseEnemy) tmp);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
