package Tower;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Vector;


public class FirstTower extends Button
{
    Rectangle Tower = null;
    Stage primaryStage;
    Group root;
    Vector<Rectangle> listFirstTowers = new Vector<>();


    public FirstTower(Stage primaryStage, Group root)
    {
        this.primaryStage = primaryStage;
        this.root = root;
        addlistener();
        show();
    }

    private void show()
    {
    this.setText("First Tower");
    this.setVisible(true);
    this.setPrefWidth(100);
}

    private void addlistener()
    {
        this.setOnMouseClicked(e ->
        {
            if (Tower == null) {
                Tower = new Rectangle();
                Tower.setWidth(50);
                Tower.setHeight(50);
                root.getChildren().add(Tower);

            }
        });

        primaryStage.getScene().setOnMouseMoved(e -> {
            if (Tower != null) {
                Tower.setVisible(true);
                Tower.setX(MouseInfo.getPointerInfo().getLocation().x);
                Tower.setY(MouseInfo.getPointerInfo().getLocation().y);
            }
        });


        primaryStage.getScene().setOnMouseClicked(e -> {
            if (Tower != null)
            {
                setTower(Tower.getX(),Tower.getY());
                Tower = null;
            }
        });
    }

    private void setTower(double x, double y)
    {
        listFirstTowers.add(new Rectangle(x, y));
        listFirstTowers.lastElement().setVisible(true);
        listFirstTowers.lastElement().setX(x);
        listFirstTowers.lastElement().setY(y);
    }

}
