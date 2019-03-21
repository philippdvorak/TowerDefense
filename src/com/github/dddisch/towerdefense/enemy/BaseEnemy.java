package com.github.dddisch.towerdefense.enemy;

import com.github.dddisch.towerdefense.utils.imageloader.ImageLoader;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class BaseEnemy extends ImageView {
    private Stage primaryStage;
    private Group root;
    private SimpleDoubleProperty lives = new SimpleDoubleProperty();
    private boolean dead = false;
    private double height, width;
    private boolean isClicked = false;
    private ProgressBar showEnemyLives = new ProgressBar();

    public BaseEnemy(Stage primaryStage, Group root) {
        this.root = root;
        this.lives.set(100);
        this.primaryStage = primaryStage;
        this.setImage(ImageLoader.loadImage("enemies::baloon::"));

        show();
        move();
        addListeners();
    }



    private void show() {
        this.setFitWidth(50);
        this.setFitHeight(71);

        showEnemyLives.setVisible(false);
        showEnemyLives.setPrefWidth(50);
        showEnemyLives.setPrefHeight(5);
        showEnemyLives.setStyle("-fx-accent: red;");
        this.setX(0);
        this.setY(primaryStage.getHeight()/2-35);
        showEnemyLives.setLayoutY(this.getY() - 10);
        showEnemyLives.progressProperty().bind(lives.divide(100));

    }

    public int getLives() {
        return (int)lives.get();
    }

    public void setLives(int lives) {
        if(this.lives != null) {
            this.lives.set(lives);
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public ProgressBar getShowEnemyLives() {
        return showEnemyLives;
    }

    private void move() {
        Thread t = new Thread(() -> {
            while(this.getX() < primaryStage.getWidth()) {
                Platform.runLater(() -> {
                        this.setX(this.getX() + 1);
                        if (isClicked && this.lives.get() > 0)
                        {
                            showEnemyLives.setVisible(true);
                            showEnemyLives.setLayoutX(this.getX());

                        }
                        else {
                            showEnemyLives.setVisible(false);
                        }
                });
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.dead = true;
            this.setVisible(false);
        });

        t.setDaemon(true);
        t.start();
    }

    public void addListeners()
    {
        this.setOnMousePressed(event -> {
            isClicked = true;
            root.getChildren().add(showEnemyLives);
        });

        this.setOnMouseReleased(event -> {
            isClicked = false;

                showEnemyLives.setVisible(false);
                root.getChildren().remove(showEnemyLives);
        });
    }
}