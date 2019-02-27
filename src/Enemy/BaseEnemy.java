package Enemy;

public class BaseEnemy {
    private int speedX;
    private int speedY;
    private int lives;

    public BaseEnemy(int speedX, int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
        this.lives = 100;
    }

    public int getLives() {
        return lives;
    }
}
