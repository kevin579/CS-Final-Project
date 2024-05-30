import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.io.IOException;

import java.awt.image.BufferedImage;


public class Enemy extends Rectangle {
    int hp, speed;
    int speedX = 0, speedY = 0, angle = 0;
    BufferedImage image;
    boolean changeBlock = true;

    Enemy(int type) {
        super(GameFrame.startX, GameFrame.startY, GameFrame.blockSize, GameFrame.blockSize);
        this.image = GameFrame.enemyImages.get(type-1);
        if (type == 1) {
            this.hp = 10;
            this.speed = 1;
            
        }
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
