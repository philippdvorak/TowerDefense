package sample;

import Enemy.BaseEnemy;
import Tower.MagierTower;
import Tower.MagierTowerMenu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

public class Main extends Application {

    private Vector<BaseEnemy> enemyVector= new Vector<>();
    private Vector<MagierTower> towerVector = new Vector<>();
    private Group root;
    Image magier = new Image(new FileInputStream("./src/img/Magier.png"));
    ImageView Tower = null;
    MagierTowerMenu ft, ft2;
    private double tempX, tempY;
    VBox Menu = new VBox();

    public Main() throws FileNotFoundException {
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Group();

        primaryStage.setTitle("Tower Defense");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreen(true);
        primaryStage.show();
        ft = new MagierTowerMenu(primaryStage);
        ft2 = new MagierTowerMenu(primaryStage);
        Menu.getChildren().add(ft);

        //Only for testing purpose,
        //is not important and does nothing when clicked
        Menu.getChildren().add(ft2);


        root.getChildren().add(Menu);

        //Constant spawn of enemies
        Thread t = new Thread(()->{
           while (true)
           {
               BaseEnemy tmp = new BaseEnemy(primaryStage);

               Platform.runLater(()->root.getChildren().add(tmp));

               synchronized (this) {
                   enemyVector.add(tmp);
               }

               try {
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
        t.setDaemon(true);
        t.start();


        addListener(primaryStage);

        Thread killing = new Thread(() -> {
            while(true) {
                synchronized(this) {
                    for (MagierTower e : towerVector) {
                        e.calcHitBox(enemyVector);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        killing.setDaemon(true);
        killing.start();

    }

    //Adds all listeners needed for the first Button
    private void addListener(Stage primaryStage)
    {

        //Listens to the click of the Button
        ft.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (Tower == null) {
                Tower = new ImageView(magier);
                Tower.setFitWidth(56);
                Tower.setFitHeight(58);

                Tower.setPreserveRatio(true);
                root.getChildren().add(Tower);
            }
        });

        //Moves the Rectangle around on the screen
        primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED,e -> {
            if (Tower != null) {
                Tower.setVisible(true);
                tempX = MouseInfo.getPointerInfo().getLocation().x-(Tower.getFitWidth()/2);
                tempY = MouseInfo.getPointerInfo().getLocation().y-(Tower.getFitHeight()/2);
                Tower.setX(MouseInfo.getPointerInfo().getLocation().x-(Tower.getFitWidth()/2));
                Tower.setY(MouseInfo.getPointerInfo().getLocation().y-(Tower.getFitHeight()/2));
            }
        });

        //Places the Rectangle on the clicked position
        primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (Tower != null)
            {
                synchronized(this) {
                    towerVector.add(new MagierTower(tempX,tempY));
                }

                root.getChildren().add(towerVector.lastElement());
                root.getChildren().add(towerVector.lastElement().getHitBox());

                root.getChildren().remove(Tower);
                Tower = null;

                updateZIndex(root);

            }
        });
    }

    //Updates the z-index of the button, so the button always stays in the front
    //Could also be used for other elements which needs to stay in the front
    public static void updateZIndex(Group root) {
        Vector<Integer> atr = new Vector<>();
        Vector<Node> b = new Vector<>();
        int i = 0;
        for (Node tmp : root.getChildren()) {
            if (tmp instanceof VBox) {
                atr.add(i);
                b.add(root.getChildren().get(i));
            }
            i++;
        }

        for(Integer y : atr) {
            root.getChildren().remove(y.intValue());
        }

        for(Node tmp : b) {
            root.getChildren().add(tmp);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
