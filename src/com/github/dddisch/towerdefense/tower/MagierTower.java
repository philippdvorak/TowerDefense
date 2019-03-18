package com.github.dddisch.towerdefense.tower;

import com.github.dddisch.towerdefense.enemy.BaseEnemy;
import com.github.dddisch.towerdefense.enemy.BaseEnemy;
import com.github.dddisch.towerdefense.main.Main;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Adds an element of the first tower which is static and can not be repositioned with the mouse again
 */
public class MagierTower extends ImageView
{


    private Circle hitBox;
    private ImageView magicShoot;


    public MagierTower(double x, double y, Group root)
    {

        try {
            this.setImage(new Image(new FileInputStream("./res/img/Magier.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            magicShoot = new ImageView(new Image(new FileInputStream("./res/img/MagieBall.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        magicShoot.setVisible(false);
        root.getChildren().add(magicShoot);

        this.setVisible(true);
        this.setX(x);
        this.setY(y);
        this.setFitWidth(56);
        this.setFitHeight(58);

        hitBox = new Circle(150);
        hitBox.setVisible(false);
        hitBox.setFill(Color.rgb(255, 255, 50,0.5));
        hitBox.setCenterX(x+(this.getFitWidth()/2));
        hitBox.setCenterY(y+(this.getFitHeight()/2));


        addListeners();
    }


    //Return the hitBox off the Circle, is needed for intersection with the enemies
    public Circle getHitBox() {
        return hitBox;
    }

    public void calcHitBox(Group root) {

        Thread test = new Thread(()->{
                while (true) {
                    for (BaseEnemy e : Main.getEnemyVector()) {
                        if (hitBox.intersects(e.getBoundsInLocal())) {

                            Platform.runLater(() -> this.setRotate(calcAngle(e.getX(), e.getY())));

                            shoot(e,root);

                            e.setLives(e.getLives() - 10);

                            if (e.getLives() <= 0) {
                                Platform.runLater(() -> {
                                    e.setVisible(false);
                                    Main.getEnemyVector().remove(e);
                                });
                            }

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }

                            break;
                        }


                        if (e.getLives() <= 0) {
                            Platform.runLater(() -> {
                                e.setVisible(false);
                                Main.getEnemyVector().remove(e);
                            });
                        }
                    }
                }
        });


        test.setDaemon(true);
        test.start();




    }

    private double calcAngle(double x, double y) {

            double angle = Math.toDegrees(Math.atan2(x - this.getX(), y - this.getY()));
            return -angle + 180;

    }

    private void addListeners() {
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> hitBox.setVisible(true));


        this.addEventFilter(MouseEvent.MOUSE_RELEASED,e-> hitBox.setVisible(false));
    }

    private void shoot(BaseEnemy e, Group root) {

            magicShoot.setFitHeight(50);
            magicShoot.setFitWidth(50);
            magicShoot.setX(0);
            magicShoot.setY(0);
            magicShoot.setTranslateX(0);
            magicShoot.setTranslateY(0);


            FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), magicShoot);
            fadeTransition.setFromValue(1.0f);
            fadeTransition.setToValue(0.0f);

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(300), magicShoot);
            translateTransition.setFromX(this.getX());

            translateTransition.setToX(e.getX());

            translateTransition.setFromY(this.getY());
            translateTransition.setToY(e.getY());


            RotateTransition rotateTransition = new RotateTransition(Duration.millis(300), magicShoot);
            rotateTransition.setByAngle(180f);

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), magicShoot);
            scaleTransition.setToX(1.3f);
            scaleTransition.setToY(1.3f);


            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().addAll(
                    fadeTransition,
                    translateTransition,
                    rotateTransition,
                    scaleTransition
            );
            magicShoot.setVisible(true);
            parallelTransition.play();


    }



}
