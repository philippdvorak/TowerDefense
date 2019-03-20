package com.github.dddisch.towerdefense.main;

import com.github.dddisch.towerdefense.enemy.BaseEnemy;
import com.github.dddisch.towerdefense.tower.BaseTower;
import com.github.dddisch.towerdefense.tower.TowerMenu;
import com.github.dddisch.towerdefense.utils.imageloader.ImageLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main extends Application {

    static private Queue<BaseEnemy> enemyVector = new ConcurrentLinkedQueue<>();
    private Vector<BaseTower> towerVector = new Vector<>();
    private Group root;
    private ImageView tower = null;
    private TowerMenu mT, sT;
    private double tempX, tempY;
    private VBox towerMenu = new VBox();
    final static private Integer sync = new Integer(0);
    static SimpleIntegerProperty money = new SimpleIntegerProperty();
    private ProgressBar showLives = new ProgressBar();
    private SimpleDoubleProperty mainLives = new SimpleDoubleProperty(100);
    Label showMoney = new Label();
    String whichTower;
    private DropShadow drop = new DropShadow();
    private int costMoney = 0;


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

        drop.setOffsetY(3);
        drop.setColor(Color.color(0.4f, 0.4f, 0.4f));

        showMoney.setLayoutX(0);
        showMoney.setVisible(true);
        showMoney.setPrefWidth(150);
        showMoney.setPrefHeight(50);
        showMoney.setTextFill(Color.WHITE);
        showMoney.setEffect(drop);
        showMoney.setFont(new Font("Helvetica", 40));
        money.set(3000);
        showMoney.setText("$" + money.get());

        mT = new TowerMenu(primaryStage, "Magier - $150");
        sT = new TowerMenu(primaryStage, "Sniper - $250");

        towerMenu.getChildren().add(mT);
        towerMenu.getChildren().add(sT);

        towerMenu.setLayoutX(primaryStage.getWidth()-mT.getPrefWidth());

        showLives.setLayoutY(0);
        showLives.setPrefWidth(400);
        showLives.setLayoutX(primaryStage.getWidth()/2 - showLives.getPrefWidth()/2);
        showLives.setStyle("-fx-accent: red;");
        showLives.setVisible(true);
        mainLives.set(10);
        showLives.progressProperty().bind(mainLives.divide(10));

        root.getChildren().add(showMoney);
        root.getChildren().add(showLives);
        root.getChildren().add(towerMenu);

        //Constant spawn of enemies
        Thread t = new Thread(()->{
            int spawnTime = 5000;
            int spawnCount = 5;
            while (true) {

                for (int i = 0; i < spawnCount; i++)
                {
                    BaseEnemy enemy = new BaseEnemy(primaryStage);
                    Platform.runLater(()->enemyVector.add(enemy));
                    Platform.runLater(()->root.getChildren().add(enemy));


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

        //Check if Lives is 0 and if Baloon is out of Screen
        Thread endCheck = new Thread(() -> {
            while(true) {
                for (BaseEnemy e : enemyVector) {
                    if (e.isDead()) {
                        mainLives.set(mainLives.get()-1);
                        System.out.println(mainLives);
                        enemyVector.remove(e);
                        Platform.runLater(() -> root.getChildren().remove(e));
                    }
                }

                if(mainLives.get() <= 0) {
                    //Execution Code will be added Here to End Programm
                    System.exit(0);
                }
            }
        });
        endCheck.setDaemon(true);
        endCheck.start();

        addListener(primaryStage);
    }

    /**
     * Adds all listeners needed for the first Button
     * @param primaryStage stage, which the listeners shall be registered on
     */
    private void addListener(Stage primaryStage) {
        //Listens to the click of the Tower Button
        mT.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if(getMoney()>=150){
                if (tower == null) {
                    tower = ImageLoader.loadImageView("towers::magier::");
                    whichTower = "magic";
                    tower.setFitWidth(56);
                    tower.setFitHeight(58);
                    tower.setX(primaryStage.getWidth()-(mT.getPrefWidth()/2));
                    tower.setY(0);
                    tower.setPreserveRatio(true);
                    root.getChildren().add(tower);
                }
            }
        });

        sT.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if(getMoney()>=250){
                if (tower == null) {
                    tower = ImageLoader.loadImageView("towers::sniper::");
                    whichTower = "sniper";
                    tower.setFitWidth(55);
                    tower.setFitHeight(140);
                    tower.setX(primaryStage.getWidth()-(sT.getPrefWidth()/2));
                    tower.setY(0);
                    tower.setPreserveRatio(true);
                    root.getChildren().add(tower);
                }
            }
        });

        //Moves the Tower around on the screen
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

            if (tower != null) {
                if (whichTower.equals("magic")) {
                    towerVector.add(new BaseTower(tempX, tempY, root, "towers::magier::", "towers::magier::missile", 150, 300));
                    towerVector.lastElement().setHeight(56);
                    towerVector.lastElement().setWidth(58);
                    towerVector.lastElement().missleHeight(50);
                    towerVector.lastElement().missleWidth(50);
                    costMoney = 150;
                    whichTower = "";
                }

                if (whichTower.equals("sniper")) {
                    towerVector.add(new BaseTower(tempX, tempY, root, "towers::sniper::", "towers::sniper::missile", (int) (primaryStage.getHeight() * 2), 100));
                    towerVector.lastElement().setHeight(140);
                    towerVector.lastElement().setWidth(55);

                    towerVector.lastElement().missleHeight(10);
                    towerVector.lastElement().missleWidth(10);
                    costMoney = 250;
                    whichTower = "";
                }

                if (e.getY() > (primaryStage.getHeight() / 2 + 50) || e.getY() < (primaryStage.getHeight() / 2 -  50)) {
                    towerVector.lastElement().setX(e.getX() - towerVector.lastElement().getFitWidth()/2);
                    towerVector.lastElement().setY(e.getY() - towerVector.lastElement().getFitHeight()/2);
                    root.getChildren().add(towerVector.lastElement());
                    root.getChildren().add(towerVector.lastElement().getHitBox());

                    towerVector.lastElement().calcHitBox(root);
                    money.set(money.get() - costMoney);
                    root.getChildren().remove(tower);

                    tower = null;
                    updateZIndex(root);
                }
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