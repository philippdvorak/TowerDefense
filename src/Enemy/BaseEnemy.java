package Enemy;

import javafx.scene.shape.Rectangle;

public class BaseEnemy extends Rectangle {
    private int speedX;
    private int speedY;
    private int lives;
    private boolean shooting;
    private double height, width;

    public BaseEnemy(int speedX, int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.lives = 100;
    }

    private void show() {
        //TODO: Add Initalization Code for Rectangle
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }
}
