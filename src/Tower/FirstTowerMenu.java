package Tower;

import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
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


}