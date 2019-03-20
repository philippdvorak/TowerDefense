package com.github.dddisch.towerdefense.enemy;

import com.github.dddisch.towerdefense.main.Main;
import com.github.dddisch.towerdefense.utils.imageloader.ImageLoader;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BaseEnemy extends Group {
    private Stage primaryStage;
    private int lives;
    private boolean dead = false;
    private double height, width;
    private ImageView tower = new ImageView();
    private ProgressBar enemyLives = new ProgressBar();
    public BaseEnemy(Stage primaryStage) {
        this.lives = 100;
        this.primaryStage = primaryStage;

        tower.setImage(ImageLoader.loadImage("enemies::baloon::"));
        this.getChildren().add(tower);

        show();
        move();
    }

    private void show() {
        tower.setFitWidth(50);
        tower.setFitHeight(71);

        tower.setX(0);
        tower.setY(primaryStage.getHeight()/2-35);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    private void move() {
        Thread t = new Thread(() -> {
            while(tower.getX() < primaryStage.getWidth()) {
                Platform.runLater(() -> {
                        tower.setX(tower.getX() + 1);

                });
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.dead = true;
            tower.setVisible(false);
        });

        t.setDaemon(true);
        t.start();
    }


}