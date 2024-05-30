import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Bullet extends Rectangle{
	int type,damage;
	double xx,yy,speedX,speedY,targetX, targetY;
	public Bullet(int x, int y, int type, int size, double speed,double targetX,double targetY, double dis, int damage){
		super(x,y,size,size);
		this.xx = x;
		this.yy = y;
		this.x = x;
		this.y = y;
		this.type = type;
		this.damage= damage;
		this.speedX = speed*(targetX-x)/(dis);
		this.speedY = speed*(targetY-y)/(dis);
		System.out.println(dis);
	}
	public void move(){
		this.xx+=this.speedX;
		this.yy+=this.speedY;
		this.x = (int)this.xx;
		this.y = (int)this.yy;
		
	}
}
