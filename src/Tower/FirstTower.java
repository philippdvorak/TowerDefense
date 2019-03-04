package Tower;

import apple.laf.JRSUIConstants;
import javafx.scene.input.MouseEvent;
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

        HitBox = new Circle(150);
        HitBox.setVisible(false);
        HitBox.setFill(Color.rgb(255, 255, 50,0.5));
        HitBox.setCenterX(x+(this.getWidth()/2));
        HitBox.setCenterY(y+(this.getHeight()/2));

        addListeners();
    }


    //Return the HitBox off the Circle, is needed for intersection with the enemies
    public Circle getHitBox() {
        return HitBox;
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
