import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;


public class Enemy extends Rectangle {
    int factor;
    double speed;
    double speedX = 0;
	double speedY = 0, angle = 0;
	double hp;
    double maxHp;
    BufferedImage image;
    boolean changeBlock = true;
    Rectangle rect;
    Rectangle hpBar;
    Rectangle hpNow;
    Color red;
    Color green;

    Enemy(int type, int factor) {
        super(GameFrame.startX, GameFrame.startY, GameFrame.blockSize, GameFrame.blockSize);
        this.factor = factor;
        this.image = GameFrame.enemyImages.get(type-1);
        if (type == 1) {

            this.hp = 5*factor;

            this.speed = 1.5;
            

            this.speed = 1.2;


        }
        else if (type == 2) {
            this.hp = 8*factor;
            this.speed = 1.5;
        }
        else if (type == 3) {
            this.hp = 10*factor;
            this.speed = 1;
        }
        else if (type == 4) {
            this.hp = 13*factor;
            this.speed = 1;
        }
        else if (type == 5) {
            this.hp = 15*factor;
            this.speed = 0.8;
        }
        else if (type == 6) {
            this.hp = 20*factor;
            this.speed = 0.5;
        }
        else if (type == 7) {
            this.hp = 12*factor;
            this.speed = 2.0;
        }
        else if (type == 8) {
            this.hp = 15*factor;
            this.speed = 2.0;
        }
        
        this.maxHp = this.hp;
        this.rect = new Rectangle(0, 0, GameFrame.blockSize, GameFrame.blockSize);
        this.red = new Color(255, 0, 0);
        this.green = new Color(0, 255, 0);
        this.hpBar = new Rectangle(0, 0, (int) (GameFrame.blockSize), 5);
        updateHp();
    }

    public void move() {
        if ((((this.x - GameFrame.leftMargin) % GameFrame.blockSize <= this.speed &&this.speedX>=0)
                || ((this.x - GameFrame.leftMargin) % GameFrame.blockSize <= this.speed &&this.speedX<=0))
                && ((((this.y - GameFrame.topMargin) % GameFrame.blockSize <= this.speed &&this.speedY>=0)
                        || ((this.y - GameFrame.topMargin) % GameFrame.blockSize <=  this.speed &&this.speedY<=0)))&&changeBlock)   {
            changeBlock = false;
            int gridX = (this.x +GameFrame.blockSize- GameFrame.leftMargin) / GameFrame.blockSize-1;
            int gridY = (this.y +GameFrame.blockSize - GameFrame.topMargin) / GameFrame.blockSize-1;
            char direction = GameFrame.pathGrid[gridY][gridX];
            // char[] arrows = { '←', '↑', '→', '↓' };
            // System.out.println(direction);
            // System.out.printf("%d,%d. ",gridX,gridY);
            if (direction == '→') {
                this.speedX = this.speed;
                this.speedY =0;
            } else if (direction == '↑') {
                this.speedY = -this.speed;
                this.speedX =0;
            } else if (direction == '←') {
                this.speedX = -this.speed;
                this.speedY =0;
            } else if (direction == '↓') {
                this.speedY = this.speed;
                this.speedX =0;
            } else {
                this.hp =0;
                GameFrame.playerHP--;
                
            }
        }
        this.angle+=8;
        if (this.angle>=360){
            this.angle = 0;
        }
        else changeBlock = true;
        this.x+=this.speedX;
        this.y+=this.speedY;
        this.rect.setLocation((int) this.x, (int) this.y);
        this.hpBar.setLocation((int) this.rect.getX(), (int) this.rect.getY() - 10);
        updateHp();
        
    }

    public void updateHp() {
    	this.hpNow = new Rectangle(this.hpBar.x, this.hpBar.y, (int) (GameFrame.blockSize * (this.hp / this.maxHp)), 5);
    }
    public void drawHP(Graphics g) {
        g.setColor(this.green);
        g.fillRect(this.hpBar.x, this.hpBar.y, this.hpBar.width, this.hpBar.height);
        g.setColor(this.red);
        g.fillRect(this.hpNow.x, this.hpNow.y, this.hpNow.width, this.hpNow.height);
    }

    public void die(){
        this.hp = 0;
        GameFrame.enemyNum--;
        
    }
    


    static BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.out.println(e.toString());
            JOptionPane.showMessageDialog(null, "An image failed to load: " + filename, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return img;
    }
}
