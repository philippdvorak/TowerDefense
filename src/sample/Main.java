package sample;

import Enemy.BaseEnemy;
import Tower.FirstTower;
import Tower.FirstTowerMenu;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Vector;

public class Main extends Application {

    private Vector<BaseEnemy> enemyVector= new Vector<>();
    private Vector<FirstTower> towerVector = new Vector<>();
    Group root;
    Rectangle Tower = null;
    FirstTowerMenu ft;
    private double tempX, tempY;

    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        primaryStage.setTitle("Tower Defense");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreen(true);
        primaryStage.show();
        ft = new FirstTowerMenu(primaryStage);

        root.getChildren().add(new BaseEnemy(primaryStage));
        root.getChildren().add(ft);
        root.getChildren().add(new BaseEnemy(primaryStage));

        addlistener(primaryStage);

        for(Node tmp : root.getChildren()){
            if(tmp instanceof BaseEnemy) {
                enemyVector.add((BaseEnemy) tmp);
            }
        }

    }



    private void addlistener(Stage primaryStage)
    {
        ft.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (Tower == null) {
                Tower = new Rectangle();
                Tower.setWidth(50);
                Tower.setHeight(50);
                root.getChildren().add(Tower);
            }
        });

        primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED,e -> {
            if (Tower != null) {
                Tower.setVisible(true);
                tempX = MouseInfo.getPointerInfo().getLocation().x-(Tower.getWidth()/2);
                tempY = MouseInfo.getPointerInfo().getLocation().y-(Tower.getHeight()/2);
                Tower.setX(MouseInfo.getPointerInfo().getLocation().x-(Tower.getWidth()/2));
                Tower.setY(MouseInfo.getPointerInfo().getLocation().y-(Tower.getHeight()/2));
            }
        });


        primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (Tower != null)
            {
                towerVector.add(new FirstTower(tempX,tempY));
                root.getChildren().add(towerVector.lastElement());
                root.getChildren().remove(Tower);
                Tower = null;
            }
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}
