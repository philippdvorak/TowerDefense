package Tower;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;

import java.util.Vector;



public class FirstTowerMenu extends Button
{
    Rectangle Tower = null;
    Stage primaryStage;
    Vector<Rectangle> listFirstTowers = new Vector<>();


    public FirstTowerMenu(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        show();
    }

    public Vector<Rectangle> getListFirstTowers() {
        return listFirstTowers;
    }

    private void show()
    {
    this.setText("First Tower");
    this.setVisible(true);
    this.setPrefWidth(100);
}



/*

    private void setTower(double x, double y)
    {
        listFirstTowers.add(new FirstTower(x,y));
        root.getChildren().add(listFirstTowers.lastElement());
    }
    */

}