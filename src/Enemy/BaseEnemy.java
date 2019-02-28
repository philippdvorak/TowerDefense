package Enemy;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class BaseEnemy extends Rectangle {
    private Stage primaryStage;
    private int lives;
    private double height, width;

    public BaseEnemy(int beginRow, int beginCol, Stage primaryStage) {
        this.lives = 100;
        this.primaryStage = primaryStage;

        show();

        move();
    }

    private void show() {
        this.setWidth(10);
        this.setHeight(40);

        this.setX(0);
        this.setY(200);
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    private void move() {
        Thread t = new Thread(() -> {
            while(lives > 0) {
                Platform.runLater(() -> this.setX(this.getX() + 1));
                Platform.runLater(() -> this.setY(this.getY()));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.setDaemon(true);
        t.start();
    }
}
