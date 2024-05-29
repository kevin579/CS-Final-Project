import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class Bullet extends Rectangle{
	int type,speed,damage;
	double angle,speedx,speedy;
	Tower parent;
	public Bullet(Tower parent,int x, int y, int type, int size, int speed,double angle,int damage){
		super(x,y,size,size);
		this.type = type;
		this.angle = angle;
		this.speed = speed;
		this.damage= damage;
		this.parent = parent;
	}
	public void move(){
		this.x+=speed*Math.sin(parent.angle*Math.PI/180);
		this.y+=speed*Math.sin(parent.angle*Math.PI/180);
	}
}
