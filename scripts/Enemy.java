import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Rectangle {

    // Fields
    private int gridX, gridY;
    private int type;
    private double speed;
    private double xx, yy;
    private double speedX = 0, speedY = 0, angle = 0;
    private double hp;
    private double maxHp;
    private BufferedImage image;
    private boolean changeBlock = true;
    private Rectangle rect;
    private Rectangle hpBar;
    private Rectangle hpNow;
    private Color red;
    private Color green;
    private double dis = 0;

    // Constructor
    public Enemy(int type, double factor) {
        super(GameFrame.startX, GameFrame.startY, GameFrame.blockSize, GameFrame.blockSize);
        this.xx = this.x;
        this.yy = this.y;
        this.type = type;
        this.rect = new Rectangle(0, 0, GameFrame.blockSize, GameFrame.blockSize);

        if (this.type == 0) {
            this.speed = GameFrame.blockSize;
            this.hp = 1;
        } else {
            this.image = MainFrame.enemyImages.get(type - 1);
            this.hp = MainFrame.enemyHPs[type - 1] * factor;
            this.speed = MainFrame.enemySpeeds[type - 1];
            this.maxHp = this.hp;
            this.red = new Color(255, 0, 0);
            this.green = new Color(0, 255, 0);
            this.hpBar = new Rectangle(0, 0, GameFrame.blockSize, 5);
            updateHp();
        }
    }

    // Getters and Setters
    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getXx() {
        return xx;
    }

    public void setXx(double xx) {
        this.xx = xx;
    }

    public double getYy() {
        return yy;
    }

    public void setYy(double yy) {
        this.yy = yy;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean isChangeBlock() {
        return changeBlock;
    }

    public void setChangeBlock(boolean changeBlock) {
        this.changeBlock = changeBlock;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public Rectangle getHpBar() {
        return hpBar;
    }

    public void setHpBar(Rectangle hpBar) {
        this.hpBar = hpBar;
    }

    public Rectangle getHpNow() {
        return hpNow;
    }

    public void setHpNow(Rectangle hpNow) {
        this.hpNow = hpNow;
    }

    public Color getRed() {
        return red;
    }

    public void setRed(Color red) {
        this.red = red;
    }

    public Color getGreen() {
        return green;
    }

    public void setGreen(Color green) {
        this.green = green;
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    // Method to move enemies along the path
    public void move() {
        this.xx += this.speedX;
        this.yy += this.speedY;
        this.dis += Math.abs(this.speedX);
        this.dis += Math.abs(this.speedY);
        this.x = (int) this.xx;
        this.y = (int) this.yy;
        if ((((this.x - GameFrame.leftMargin) % GameFrame.blockSize <= this.speed && this.speedX >= 0)
                || ((this.x - GameFrame.leftMargin) % GameFrame.blockSize <= this.speed && this.speedX <= 0))
                && ((((this.y - GameFrame.topMargin) % GameFrame.blockSize <= this.speed && this.speedY >= 0)
                        || ((this.y - GameFrame.topMargin) % GameFrame.blockSize <= this.speed && this.speedY <= 0)))
                && changeBlock) {
            changeBlock = false;
            this.gridX = (this.x + GameFrame.blockSize - GameFrame.leftMargin) / GameFrame.blockSize - 1;
            this.gridY = (this.y + GameFrame.blockSize - GameFrame.topMargin) / GameFrame.blockSize - 1;
            char direction = GameFrame.pathGrid[gridY][gridX];

            if (direction == '→') {
                this.speedX = this.speed;
                this.speedY = 0;
                if (this.type == 0) {
                    this.image = MainFrame.pointerRight;
                }
            } else if (direction == '↑') {
                this.speedY = -this.speed;
                this.speedX = 0;
                if (this.type == 0) {
                    this.image = MainFrame.pointerUp;
                }
            } else if (direction == '←') {
                this.speedX = -this.speed;
                this.speedY = 0;
                if (this.type == 0) {
                    this.image = MainFrame.pointerLeft;
                }
            } else if (direction == '↓') {
                this.speedY = this.speed;
                this.speedX = 0;
                if (this.type == 0) {
                    this.image = MainFrame.pointerDown;
                }
            } else {
                this.hp = 0;
                if (this.type != 0) {
                    GameFrame.playerHP--;
                    MainFrame.lossHp.play();
                }
            }
        }

        // Make non-pointer enemy spin for better graphics. also they have a hp bar
        if (this.type != 0) {
            this.angle += 8;
            if (this.angle >= 360) {
                this.angle = 0;
            } else {
                changeBlock = true;
            }

            this.rect.setLocation((int) this.x, (int) this.y);
            this.hpBar.setLocation((int) this.rect.getX(), (int) this.rect.getY() - 10);
            updateHp();
        } else {
            changeBlock = true;
        }
    }

    // Update enemy hp
    public void updateHp() {
        this.hpNow = new Rectangle(this.hpBar.x, this.hpBar.y, (int) (GameFrame.blockSize * (this.hp / this.maxHp)), 5);
    }

    // Draw hp bar
    public void drawHP(Graphics gc) {
        gc.setColor(this.green);
        gc.fillRect(this.hpBar.x, this.hpBar.y, this.hpBar.width, this.hpBar.height);
        gc.setColor(this.red);
        gc.fillRect(this.hpNow.x, this.hpNow.y, this.hpNow.width, this.hpNow.height);
    }

    // Make the enemy disappear
    public void die() {
        this.hp = 0;
        GameFrame.enemyNum--;
    }
}
