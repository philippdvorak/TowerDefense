package Tower;

import Enemy.BaseEnemy;
import com.sun.xml.internal.rngom.parse.host.Base;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;


public class MagierTower extends ImageView
{

    //Adds an element of the first Tower which is static and can not be repositioned with the mouse again

    Circle HitBox;

    public MagierTower(double x, double y)
    {

        try {
            this.setImage(new Image(new FileInputStream("./src/img/Magier.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    public void calcHitBox(Vector<BaseEnemy> m) {
        synchronized(this) {
            for (BaseEnemy e : m) {
                if (HitBox.intersects(e.getBoundsInLocal())) {

                    this.setRotate(calcAngle(e.getX(), e.getY()));

                    e.setLives(e.getLives() - 10);
                    if(e.getLives() <= 0) {
                        m.remove(e);
                    }

                    return;
                }
            }
        }
    }

    private double calcAngle(double x, double y) {
        double angle = 0;

        angle = Math.toDegrees(Math.atan2(x - this.getX(), y - this.getY()));

        return -angle + 180;
    }

    public void addListeners()
    {
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->
        {
            HitBox.setVisible(true);
        });


        this.addEventFilter(MouseEvent.MOUSE_RELEASED,e->{

            HitBox.setVisible(false);

        });
    }

}
