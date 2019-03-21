package com.github.dddisch.towerdefense.tower;

import com.github.dddisch.towerdefense.enemy.BaseEnemy;
import com.github.dddisch.towerdefense.main.Main;
import com.github.dddisch.towerdefense.utils.imageloader.ImageLoader;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Adds an element of the first tower which is static and can not be repositioned with the mouse again
 */
public class BaseTower extends ImageView
{


    private Circle hitBox;
    private ImageView missle;

    FadeTransition fadeTransition;
    TranslateTransition translateTransition;
    RotateTransition rotateTransition;
    ScaleTransition scaleTransition;
    ParallelTransition parallelTransition = new ParallelTransition();


    public BaseTower(double x, double y, Group root, String skin, String missileSkin, int hitRadius, int attackSpeed)
    {
        this.setImage(ImageLoader.loadImage(skin));
        missle = ImageLoader.loadImageView(missileSkin);

        fadeTransition = new FadeTransition(Duration.millis(attackSpeed), missle);
        translateTransition = new TranslateTransition(Duration.millis(attackSpeed), missle);
        scaleTransition = new ScaleTransition(Duration.millis(attackSpeed), missle);
        rotateTransition = new RotateTransition(Duration.millis(attackSpeed), missle);
        missle.setVisible(false);
        root.getChildren().add(missle);
        parallelTransition.getChildren().addAll(
                fadeTransition,
                translateTransition,
                rotateTransition,
                scaleTransition
        );

        this.setVisible(true);

        hitBox = new Circle(hitRadius);
        hitBox.setVisible(false);
        hitBox.setFill(Color.rgb(255, 255, 50,0.5));

        addListeners();
    }


    //Return the hitBox off the Circle, is needed for intersection with the enemies
    public Circle getHitBox() {
        return hitBox;
    }

    public void setHeight(int height)
{
    this.setFitHeight(height);
}

    public void setWidth(int width)
    {
        this.setFitWidth(width);
    }

    public void missleHeight(int height)
    {
        missle.setFitHeight(height);
    }

    public void missleWidth(int width)
    {
        missle.setFitWidth(width);
    }

    public void calcHitBox(Group root) {

        Thread test = new Thread(()->{
                while (true) {
                    for (BaseEnemy e : Main.getEnemyVector()) {
                        if (hitBox.intersects(e.getBoundsInLocal())) {

                            Platform.runLater(() -> this.setRotate(calcAngle(e.getX(), e.getY())));

                            shoot(e);

                            e.setLives(e.getLives() - 10);


                                if (e.getLives() <= 0) {
                                    e.getShowEnemyLives().setVisible(false);
                                    Platform.runLater(() -> {
                                        e.setVisible(false);

                                        Main.getEnemyVector().remove(e);
                                            Platform.runLater(() -> {
                                                    root.getChildren().remove(e);

                                            });


                                        Main.setMoney(Main.getMoney()+5);
                                    });
                                }

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }

                            break;
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

    private void shoot(BaseEnemy e) {


        missle.setX(0);
        missle.setY(0);
        missle.setTranslateX(0);
        missle.setTranslateY(0);
        missle.setVisible(false);

        fadeTransition.setFromValue(1.0f);
        fadeTransition.setToValue(0.0f);


        translateTransition.setFromX((this.getX() + (this.getFitWidth()/2))- (missle.getFitWidth()));
        translateTransition.setFromY((this.getY() + (this.getFitHeight()/2)) - (missle.getFitHeight()));


        if(e.getX()>= 0) { translateTransition.setToX(e.getX()); }
        else { return; }

        translateTransition.setFromY(this.getY() + (this.getFitHeight()/2));

        if(e.getY() >= 0) { translateTransition.setToY(e.getY()); }
        else { return; }


        rotateTransition.setByAngle(180f);

        scaleTransition.setToX(1.3f);
        scaleTransition.setToY(1.3f);

        parallelTransition.play();
        missle.setVisible(true);

    }



}