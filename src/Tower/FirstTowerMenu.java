package Tower;

import javafx.scene.control.Button;
import javafx.stage.Stage;


//This class adds a button for creating elements of the first tower
//The listener which is needed for creating the elements is placed in the Main.java
//but could maybe be placed in this class again later on


public class FirstTowerMenu extends Button
{
    Stage primaryStage;

    public FirstTowerMenu(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.setText("First Tower");
        this.setVisible(true);
        this.setPrefWidth(100);
    }
}