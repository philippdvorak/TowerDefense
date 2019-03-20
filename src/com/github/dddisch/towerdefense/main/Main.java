package com.github.dddisch.towerdefense.main;

import com.github.dddisch.towerdefense.enemy.BaseEnemy;
import com.github.dddisch.towerdefense.tower.MagierTower;
import com.github.dddisch.towerdefense.tower.MagierTowerMenu;
import com.github.dddisch.towerdefense.utils.imageloader.ImageLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private ImageView tower = null;
    private MagierTowerMenu ft, ft2;
    private double tempX, tempY;
    private VBox towerMenu = new VBox();
    final static private Integer sync = 0;
    static SimpleIntegerProperty money = new SimpleIntegerProperty();
    Label showMoney = new Label();


    public Main() throws FileNotFoundException {
    }


    static public Queue<BaseEnemy> getEnemyVector() {
        return enemyVector;
    }

    public static Integer getSync() {
        return sync;
    }

    static public int getMoney() {
        return money.get();
    }

    static public SimpleIntegerProperty moneyProperty() {
        return money;
    }

    public static void setMoney(int money) {
        Main.money.set(money);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Group();

        primaryStage.setTitle("tower Defense");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setFullScreen(true);
        primaryStage.show();

        ImageView back = ImageLoader.loadImageView("background");
        back.setFitWidth(primaryStage.getWidth());
        back.setFitHeight(primaryStage.getHeight());
        root.getChildren().add(back);

        showMoney.setLayoutX(0);
        showMoney.setVisible(true);
        money.set(280);
        showMoney.setText("$" + money.get());

        ft = new MagierTowerMenu(primaryStage);
        towerMenu.getChildren().add(ft);
        towerMenu.setLayoutX(primaryStage.getWidth()-ft.getPrefWidth());

        root.getChildren().add(showMoney);
        root.getChildren().add(towerMenu);

        //Constant spawn of enemies
        Thread t = new Thread(()->{
            int spawnTime = 5000;
            int spawnCount = 5;
           while (true) {

               for (int i = 0; i < spawnCount; i++)
               {

                   synchronized (sync)
                   {
                       BaseEnemy enemy = new BaseEnemy(primaryStage);
                       enemyVector.add(enemy);
                       Platform.runLater(()->root.getChildren().add(enemy));
                   }


                   try {
                       Thread.sleep(spawnTime);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }

               if (spawnTime >= 500)
               {
                   spawnCount =(int)(spawnCount * 1.2);
                   spawnTime = (int)(spawnTime * 0.85);
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
                if(getMoney()>=150){
                    if (tower == null) {
                        tower = ImageLoader.loadImageView("towers::magier::");
                        tower.setFitWidth(56);
                        tower.setFitHeight(58);
                        tower.setX(primaryStage.getWidth()-(ft.getPrefWidth()/2));
                        tower.setY(0);
                        tower.setPreserveRatio(true);
                        root.getChildren().add(tower);
                    }
                }
            });

            //Moves the Rectangle around on the screen
            primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED,e -> {

            if (tower != null) {
                tower.setVisible(true);
                tempX = e.getScreenX() - (tower.getFitWidth() / 2);
                tempY = e.getScreenY() - (tower.getFitHeight() / 2);
                tower.setX(e.getScreenX() - (tower.getFitWidth() / 2));
                tower.setY(e.getScreenY() - (tower.getFitHeight() / 2));
            }

        });

            //Places the Tower on the clicked position
            primaryStage.getScene().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                if (tower != null && getMoney() >= 150) {
                    towerVector.add(new MagierTower(tempX,tempY, root));

                    root.getChildren().add(towerVector.lastElement());
                    root.getChildren().add(towerVector.lastElement().getHitBox());

                    towerVector.lastElement().calcHitBox();

                    root.getChildren().remove(tower);
                    tower = null;

                    updateZIndex(root);
                    setMoney(getMoney()-150);

                }
            });

            showMoney.textProperty().bind(Bindings.concat("$", money.asString()));

    }

    /**
     * Updates the z-index of the button, so the button always stays in the front
     * Could also be used for other elements which need to stay in the front
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
