package Tower;

import Enemy.BaseEnemy;
import com.sun.tools.corba.se.idl.ExceptionEntry;
import com.sun.xml.internal.rngom.parse.host.Base;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import sample.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;


public class MagierTower extends ImageView
{

    //Adds an element of the first Tower which is static and can not be repositioned with the mouse again

    Circle HitBox;
    ImageView MagicShoot;


    public MagierTower(double x, double y, Group root)
    {

        try {
            this.setImage(new Image(new FileInputStream("./src/img/Magier.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            MagicShoot = new ImageView(new Image(new FileInputStream("./src/img/MagieBall.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        MagicShoot.setVisible(false);
        root.getChildren().add(MagicShoot);

        this.setVisible(true);
        this.setX(x);
        this.setY(y);
        this.setFitWidth(56);
        this.setFitHeight(58);

        HitBox = new Circle(150);
        HitBox.setVisible(false);
        HitBox.setFill(Color.rgb(255, 255, 50,0.5));
        HitBox.setCenterX(x+(this.getFitWidth()/2));
        HitBox.setCenterY(y+(this.getFitHeight()/2));


        addListeners();
    }


    //Return the HitBox off the Circle, is needed for intersection with the enemies
    public Circle getHitBox() {
        return HitBox;
    }

    public void calcHitBox(Group root) {

        Thread test = new Thread(()->{
            while (true) {
                synchronized (this) {
                    for (BaseEnemy e : Main.getEnemyVector()) {
                        if (HitBox.intersects(e.getBoundsInLocal())) {

                          this.setRotate(calcAngle(e.getX(), e.getY()));

                         // shoot(e,root);


                        try {
                                Thread.sleep(500);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }

                            this.setRotate(calcAngle(e.getX(), e.getY()));

                            e.setLives(e.getLives() - 10);



                        }


                            if (e.getLives() <= 0) {
                                Platform.runLater(()->{

                                    e.setVisible(false);
                                Main.getEnemyVector().remove(e);
                                });
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

    public void addListeners() {
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->
        {
            HitBox.setVisible(true);
        });


        this.addEventFilter(MouseEvent.MOUSE_RELEASED,e->{

            HitBox.setVisible(false);

        });
    }

    public void shoot(BaseEnemy e, Group root)
    {

        Thread t = new Thread(()->{

            MagicShoot.setFitHeight(50);
            MagicShoot.setFitWidth(50);
            MagicShoot.setX(0);
            MagicShoot.setY(0);
            MagicShoot.setTranslateX(0);
            MagicShoot.setTranslateY(0);
            MagicShoot.setVisible(true);


                FadeTransition fadeTransition =
                        new FadeTransition(Duration.millis(300), MagicShoot);
                fadeTransition.setFromValue(1.0f);
                fadeTransition.setToValue(0.0f);

                TranslateTransition translateTransition =
                        new TranslateTransition(Duration.millis(300), MagicShoot);
                translateTransition.setFromX(this.getX());

                translateTransition.setToX(e.getX());

                translateTransition.setFromY(this.getY());
                translateTransition.setToY(e.getY());


                RotateTransition rotateTransition =
                        new RotateTransition(Duration.millis(300), MagicShoot);
                rotateTransition.setByAngle(180f);

                ScaleTransition scaleTransition =
                        new ScaleTransition(Duration.millis(300), MagicShoot);
                scaleTransition.setToX(1.3f);
                scaleTransition.setToY(1.3f);


                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().addAll(
                        fadeTransition,
                        translateTransition,
                        rotateTransition,
                        scaleTransition
                );
                parallelTransition.play();


        });
        t.setDaemon(true);
        t.start();


    }



}
