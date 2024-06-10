
import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Rectangle {
	
	//initialize variables
    int gridX,gridY;
    int type;
    double speed;
    double xx, yy;
    double speedX = 0, speedY = 0, angle = 0;
    double hp;
    double maxHp;
    BufferedImage image;
    boolean changeBlock = true;
    Rectangle rect;
    Rectangle hpBar;
    Rectangle hpNow;
    Color red;
    Color green;
    double dis = 0;

    //constructor
    Enemy(int type, double factor) {
        super(GameFrame.startX, GameFrame.startY, GameFrame.blockSize, GameFrame.blockSize);
        this.xx = this.x;
        this.yy = this.y;
        this.type = type;
        this.rect = new Rectangle(0, 0, GameFrame.blockSize, GameFrame.blockSize);

        if (this.type == 0) {// this is used for showing the path (called pointer enemy). 
            this.speed = GameFrame.blockSize;
            this.hp =1;
            
        } else {// These are normal enemies

            this.image = MainFrame.enemyImages.get(type - 1);
            this.hp = MainFrame.enemyHPs[type - 1] * factor;
            this.speed = MainFrame.enemySpeeds[type - 1];
            this.maxHp = this.hp;
            this.red = new Color(255, 0, 0);
            this.green = new Color(0, 255, 0);
            this.hpBar = new Rectangle(0, 0, (int) (GameFrame.blockSize), 5);
            updateHp();
        }
    }

    //method to move enemies along the path
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
                if (this.type ==0){
                    this.image = MainFrame.pointerRight;
                }
            } else if (direction == '↑') {
                this.speedY = -this.speed;
                this.speedX = 0;
                if (this.type ==0){
                    this.image = MainFrame.pointerUp;
                }
            } else if (direction == '←') {
                this.speedX = -this.speed;
                this.speedY = 0;
                if (this.type ==0){
                    this.image = MainFrame.pointerLeft;
                }
            } else if (direction == '↓') {
                this.speedY = this.speed;
                this.speedX = 0;
                if (this.type ==0){
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
        

        //Make non pointer enemy spin for better graphics. also they have a hp bar
        if (this.type != 0) {
            this.angle += 8;
            if (this.angle >= 360) {
                this.angle = 0;
            } else
                changeBlock = true;

            this.rect.setLocation((int) this.x, (int) this.y);
            this.hpBar.setLocation((int) this.rect.getX(), (int) this.rect.getY() - 10);
            updateHp();
        } else {
            changeBlock = true;
            
        }

    }

    //update enemy hp 
    public void updateHp() {
        this.hpNow = new Rectangle(this.hpBar.x, this.hpBar.y, (int) (GameFrame.blockSize * (this.hp / this.maxHp)), 5);
    }

    //draw hp bar
    public void drawHP(Graphics gc) {
        gc.setColor(this.green);
        gc.fillRect(this.hpBar.x, this.hpBar.y, this.hpBar.width, this.hpBar.height);
        gc.setColor(this.red);
        gc.fillRect(this.hpNow.x, this.hpNow.y, this.hpNow.width, this.hpNow.height);

    }

    //make the enemy disappear
    public void die() {
        this.hp = 0;
        GameFrame.enemyNum--;

    }

}
