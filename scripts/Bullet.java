import java.awt.*;

public class Bullet extends Rectangle {
	int existTime;
	int type, damage;
	double xx, yy, speedX, speedY, targetX, targetY;
	double speed;
	boolean penetrate = false;
	int explodeRadius = 0,explodeTime =-1;
	int size;

	public Bullet(int x, int y, int type, int size, double speedX, double speedY, int damage) {
		super(x, y, size, size);
		this.xx = x;
		this.yy = y;
		this.x = x;
		this.y = y;
		this.type = type;
		this.damage = damage;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speed = Math.sqrt(speedX * speedX + speedY * speedY);
		this.size = size;
	}

	public void move() {
		this.existTime++;
		this.xx += this.speedX;
		this.yy += this.speedY;
		this.x = (int) this.xx;
		this.y = (int) this.yy;

	}

	public void explode() {
	}
}

class PenetrateBullet extends Bullet {
	PenetrateBullet(int x, int y, int type, int size, double speedX, double speedY, int damage) {
		super(x, y, type, size, speedX, speedY, damage);
		this.penetrate = true;

	}
}
// class FrozenBullet extends Bullet{
// FrozenBullet(int x, int y, int type, int size, double speed,double
// targetX,double targetY, double dis, int damage){
// super(x, y, type, size, speed, targetX, targetY, dis, damage);
// }
// }

class Boom extends Bullet {
	double targetX, targetY;

	Boom(int x, int y, int type, int size, double speedX, double speedY, int damage, double targetX, double targetY) {
		super(x, y, type, size, speedX, speedY, damage);
		this.explodeRadius = MainFrame.explodeRadius;
		this.targetX = targetX;
		this.targetY = targetY;

	}

	public void explode() {
		for (Enemy enemy: GameFrame.enemys){
			if (Math.sqrt(Math.pow(enemy.x-this.x,2)+Math.pow(enemy.y-this.y,2))<this.explodeRadius){
				enemy.hp-=this.damage;
			}
		}
		this.explodeTime ++;
		this.speedX = 0;
		this.speedY = 0;
	}

}
