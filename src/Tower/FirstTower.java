package Tower;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class FirstTower extends Rectangle
{

    //Adds an element of the first Tower which is static and can not be repositioned with the mouse again

    Circle HitBox;

    public FirstTower(double x,double y)
    {
        this.setVisible(true);
        this.setX(x);
        this.setY(y);
        this.setWidth(50);
        this.setHeight(50);

        HitBox = new Circle(120);
        HitBox.setVisible(true);
        HitBox.setStroke(Color.BLACK);
        HitBox.setFill(Color.TRANSPARENT);
        HitBox.setCenterX(x+(this.getWidth()/2));
        HitBox.setCenterY(y+(this.getHeight()/2));
    }


    //Return the HitBox off the Circle, is needed for intersection with the enemies
    public Circle getHitBox() {
        return HitBox;
    }

}
