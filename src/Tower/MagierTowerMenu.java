package Tower;

import javafx.scene.control.Button;
import javafx.stage.Stage;


//This class adds a button for creating elements of the first tower
//The listener which is needed for creating the elements is placed in the Main.java
//but could maybe be placed in this class again later on


public class MagierTowerMenu extends Button
{
    Stage primaryStage;

    public MagierTowerMenu(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.setText("Sniper");
        this.setVisible(true);
        this.setPrefWidth(100);
    }
}