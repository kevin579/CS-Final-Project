import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;


public class Enemy extends Rectangle {
    int hp;
    int factor;
    double speed;
    double speedX = 0;
	double speedY = 0, angle = 0;
    BufferedImage image;
    boolean changeBlock = true;
    Rectangle hpBar;

    Enemy(int type, int factor) {
        super(GameFrame.startX, GameFrame.startY, GameFrame.blockSize, GameFrame.blockSize);
        this.factor = factor;
        if (type == 1) {

        	this.image = loadImage("scripts/Images/enemy_2.png");
            this.hp = 5*factor;
            this.speed = 1.5;
            

            this.hp = 10;
            this.speed = 1;
            this.image = loadImage("scripts/Images/enemy_2.png");

        }
        else if (type == 2) {
        	this.image = loadImage("scripts/Images/enemy_3.png");
            this.hp = 8*factor;
            this.speed = 1.2;
        }
        else if (type == 3) {
        	this.image = loadImage("scripts/Images/enemy_4.png");
            this.hp = 10*factor;
            this.speed = 1;
        }
        else if (type == 4) {
        	this.image = loadImage("scripts/Images/enemy_5.png");
            this.hp = 13*factor;
            this.speed = 1;
        }
        else if (type == 5) {
        	this.image = loadImage("scripts/Images/enemy_6.png");
            this.hp = 15*factor;
            this.speed = 0.8;
        }
        else if (type == 6) {
        	this.image = loadImage("scripts/Images/enemy_7.png");
            this.hp = 20*factor;
            this.speed = 0.5;
        }
        else if (type == 7) {
        	this.image = loadImage("scripts/Images/enemy_8.png");
            this.hp = 12*factor;
            this.speed = 2.0;
        }
        else if (type == 8) {
        	this.image = loadImage("scripts/Images/enemy_9.png");
            this.hp = 15*factor;
            this.speed = 2.0;
        }
        this.hpBar = new Rectangle(0, 0, (int) (GameFrame.blockSize * 0.6), 5);
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
            System.out.printf("%d,%d. ",gridX,gridY);
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
                this.hp = 0;
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
