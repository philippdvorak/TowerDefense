package Tower;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class FirstTower extends Rectangle
{

    Circle HitBox;

    public FirstTower(double x,double y)
    {
        this.setVisible(true);
        this.setX(x);
        this.setY(y);
        this.setWidth(50);
        this.setHeight(50);

        HitBox.setVisible(true);
        HitBox.setStroke(Color.BLACK);
        HitBox.setFill(Color.WHITE);
        HitBox = new Circle(40);
        HitBox.setCenterX(x-(this.getWidth()/2));
        HitBox.setCenterY(y-(this.getHeight()/2));

    }


    public Circle getHitBox() {
        return HitBox;
    }

}
