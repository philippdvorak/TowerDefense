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
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {

    static private Queue<BaseEnemy> enemyVector = new ConcurrentLinkedQueue<>();
    private Vector<MagierTower> towerVector = new Vector<>();
    private Group root;
    private Image magier = new Image(new FileInputStream("./src/img/Magier.png"));
    private ImageView Tower = null;
    private MagierTowerMenu ft, ft2;
    private double tempX, tempY;
    private VBox Menu = new VBox();
    static private Integer sync = new Integer(0);

    public Main() throws FileNotFoundException {
    }

    static public Queue<BaseEnemy> getEnemyVector() {
        return enemyVector;
    }

    public static Integer getSync() {
        return sync;
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

        root.getChildren().add(Menu);

        //Constant spawn of enemies
        Thread t = new Thread(()->{
           while (true) {
               BaseEnemy enemy = new BaseEnemy(primaryStage);
               enemyVector.add(enemy);
               Platform.runLater(()->root.getChildren().add(enemy));

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
    }

    //Adds all listeners needed for the first Button
    private void addListener(Stage primaryStage) {
    //Listens to the click of the Button
        Thread listenersThread = new Thread(()->{
            ft.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
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
            primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                if (Tower != null) {
                    towerVector.add(new MagierTower(tempX,tempY, root));

                    root.getChildren().add(towerVector.lastElement());
                    root.getChildren().add(towerVector.lastElement().getHitBox());

                    towerVector.lastElement().calcHitBox();

                    root.getChildren().remove(Tower);
                    Tower = null;

                    updateZIndex(root);

                }
            });
        });
        listenersThread.setDaemon(true);
        listenersThread.start();
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
