package com.github.dddisch.towerdefense.tower;

import javafx.scene.control.Button;

/**
 * This class adds a button for creating elements of the first tower
 * The listener which is needed for creating the elements is placed in the Main.java
 * but could maybe be placed in this class again later on
 */
public class TowerMenu extends Button
{

    public TowerMenu(String text)
    {
        this.setText(text);
        this.setVisible(true);
        this.setPrefWidth(150);
    }
}