package com.github.dddisch.towerdefense.enemy;

import com.github.dddisch.towerdefense.utils.imageloader.ImageLoader;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BaseEnemy extends ImageView {
    private Stage primaryStage;
    private int lives;
    private double height, width;

    public BaseEnemy(Stage primaryStage) {
        this.lives = 100;
        this.primaryStage = primaryStage;

        this.setImage(ImageLoader.loadImage("enemies::baloon::"));

        show();
        move();
    }

    private void show() {
        this.setFitWidth(50);
        this.setFitHeight(71);

        this.setX(0);
        this.setY(primaryStage.getHeight()/2-35);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    private void move() {
        Thread t = new Thread(() -> {
            while(this.getX() < primaryStage.getWidth()) {
                Platform.runLater(() -> this.setX(this.getX() + 1));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.setVisible(false);
        });

        t.setDaemon(true);
        t.start();
    }


}