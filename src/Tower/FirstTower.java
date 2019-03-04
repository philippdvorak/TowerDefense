package Tower;

import javafx.scene.shape.Rectangle;


public class FirstTower extends Rectangle
{

    public FirstTower(double x,double y)
    {
        this.setVisible(true);
        this.setX(x);
        this.setY(y);
        this.setWidth(50);
        this.setHeight(50);
        //this.toBack();
    }

}
