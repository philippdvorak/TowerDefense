package Enemy;

public class BaseEnemy {
    private int speedX;
    private int speedY;
    private int lives;
    private boolean shooting;

    public BaseEnemy(int speedX, int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.lives = 100;
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
