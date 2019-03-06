package Tower;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class FirstTower extends ImageView
{

    //Adds an element of the first Tower which is static and can not be repositioned with the mouse again

    Circle HitBox;

    public FirstTower(double x,double y)
    {

        this.setImage(new Image("https://banner2.kisspng.com/20180324/voe/kisspng-balloon-clip-art-blue-balloon-cliparts-5ab676069caae9.6687421615219072066417.jpg"));
        this.setVisible(true);
        this.setX(x);
        this.setY(y);
        this.setFitWidth(43);
        this.setFitHeight(73);

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
