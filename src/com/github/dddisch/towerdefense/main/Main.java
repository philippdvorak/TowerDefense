package com.github.dddisch.towerdefense.main;

import com.github.dddisch.towerdefense.enemy.BaseEnemy;
import com.github.dddisch.towerdefense.tower.MagierTower;
import com.github.dddisch.towerdefense.tower.MagierTowerMenu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main extends Application {

    static private Queue<BaseEnemy> enemyVector = new ConcurrentLinkedQueue<>();
    private Vector<MagierTower> towerVector = new Vector<>();
    private Group root;
    private Image magier = new Image(new FileInputStream("./res/img/Magier.png"));
    private ImageView tower = null;
    private MagierTowerMenu ft, ft2;
    private double tempX, tempY;
    private VBox menu = new VBox();
    static private Integer sync = 0;


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

        primaryStage.setTitle("tower Defense");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreen(true);
        primaryStage.show();

        Image image = null;
        try {
            image = new Image(new FileInputStream("./res/img/background.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView back = new ImageView(image);
        back.setFitWidth(primaryStage.getWidth());
        back.setFitHeight(primaryStage.getHeight());
        root.getChildren().add(back);



        ft = new MagierTowerMenu(primaryStage);
        ft2 = new MagierTowerMenu(primaryStage);
        menu.getChildren().add(ft);

        root.getChildren().add(menu);

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

    /**
     * Adds all listeners needed for the first Button
     * @param primaryStage stage, which the listeners shall be registered on
     */
    private void addListener(Stage primaryStage) {
    //Listens to the click of the Button
            ft.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    if (tower == null) {
                        tower = new ImageView(magier);
                        tower.setFitWidth(56);
                        tower.setFitHeight(58);
                if (tower == null) {
                    tower = new ImageView(magier);
                    tower.setFitWidth(56);
                    tower.setFitHeight(58);

                        tower.setPreserveRatio(true);
                        root.getChildren().add(tower);
                    }
                    tower.setPreserveRatio(true);
                    root.getChildren().add(tower);
                }
            });

            //Moves the Rectangle around on the screen
            primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED,e -> {

            if (tower != null) {
                tower.setVisible(true);
                tempX = MouseInfo.getPointerInfo().getLocation().x - (tower.getFitWidth() / 2);
                tempY = MouseInfo.getPointerInfo().getLocation().y - (tower.getFitHeight() / 2);
                Platform.runLater(() -> {
                    tower.setX(MouseInfo.getPointerInfo().getLocation().x - (tower.getFitWidth() / 2));
                    tower.setY(MouseInfo.getPointerInfo().getLocation().y - (tower.getFitHeight() / 2));
                });

            }

        });

            //Places the Rectangle on the clicked position
            primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                if (tower != null) {
                    towerVector.add(new MagierTower(tempX,tempY, root));

                    root.getChildren().add(towerVector.lastElement());
                    root.getChildren().add(towerVector.lastElement().getHitBox());

                    towerVector.lastElement().calcHitBox(root);

                    root.getChildren().remove(tower);
                    tower = null;

                    updateZIndex(root);

                }
            });

    }

    /**
     * Updates the z-index of the button, so the button always stays in the front
     * Could also be used for other elements which needs to stay in the front
     *
     * @param root the group, whose elements shall be reordered
     */
    private static void updateZIndex(Group root) {
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
