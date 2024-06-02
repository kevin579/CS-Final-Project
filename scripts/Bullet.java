import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends Rectangle {
	int existTime;
	int type, damage;
	double xx, yy, speedX, speedY, targetX, targetY;
	double speed;
	boolean penetrate = false;
	int explodeRadius = 0, explodeTime = -1;
	int size;
	BufferedImage image;
	double angle = 0;
	AffineTransform transform;

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
		if (this.type ==5){
			this.image = GameFrame.bulletImages.get(2);
		}
		else if (this.type ==6){
			this.image = GameFrame.bulletImages.get(1);
		}
		else{
			this.image = GameFrame.bulletImages.get(0);
		}


		
	}

	public void move() {
		this.existTime++;
		
		this.xx += this.speedX;
		this.yy += this.speedY;
		this.x = (int) this.xx;
		this.y = (int) this.yy;
		this.angle = Math.atan2(this.speedY, this.speedX) + Math.PI / 2;
		transform = new AffineTransform();
		transform.translate(this.x + this.size / 2, this.y + this.size / 2);
		transform.rotate(this.angle);
		transform.translate(-this.size / 2 * 4, -this.size / 2 * 4);
		transform.scale(this.size / 80.0 * 4, this.size / 80.0 * 4);

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

class Boom extends Bullet {

	Boom(int x, int y, int type, int size, double speedX, double speedY, int damage) {
		super(x, y, type, size, speedX, speedY, damage);
		this.explodeRadius = MainFrame.explodeRadius[0];

	}

	public void explode() {
		for (Enemy enemy : GameFrame.enemys) {
			if (Math.sqrt(Math.pow(enemy.x - this.x, 2) + Math.pow(enemy.y - this.y, 2)) < this.explodeRadius) {
				enemy.hp -= this.damage;
			}
		}
		this.explodeTime++;
		this.speedX = 0;
		this.speedY = 0;
	}

}

class Missle extends Boom {
	Tower parent;
	double dis;
	boolean orbit;
	int centerX, centerY;

	Missle(int x, int y, int type, int size, double speedX, double speedY, int damage, Tower parent) {
		super(x, y, type, size, speedX, speedY, damage);
		this.explodeRadius = MainFrame.explodeRadius[1];
		this.parent = parent;
		this.speed = MainFrame.towerSpeed[4];
		this.centerX = (GameFrame.col / 2 - 1) * GameFrame.blockSize + GameFrame.leftMargin;
		this.centerY = GameFrame.row / 2 * GameFrame.blockSize + GameFrame.topMargin;
	}

	public void move() {
		if (orbit) {
			if (parent.target != null && parent.target.hp > 0) {
				orbit = false;
				return;
			}
			double distanceX = this.x - this.centerX;
			double distanceY = this.y - this.centerY;
			double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

			// Calculate the centripetal acceleration
			double forceMagnitude = (speed * speed) / GameFrame.blockSize / 2;
			double accelerationX = -forceMagnitude * (distanceX / distance);
			double accelerationY = -forceMagnitude * (distanceY / distance);

			// Update the velocity
			this.speedX += accelerationX;
			this.speedY += accelerationY;

		} else if (parent.target != null && parent.target.hp > 0) {
			this.dis = Math.sqrt(Math.pow((parent.target.x - this.x), 2) + Math.pow((parent.target.y - this.y), 2));
			this.speedX = this.speed * (parent.target.x + parent.target.width / 2 - this.x) / (dis);
			this.speedY = this.speed * (parent.target.y + parent.target.height / 2 - this.y) / (dis);

		} else if (this.explodeTime < 0) {
			this.dis = Math.sqrt(Math.pow((this.centerX + GameFrame.blockSize * 4 - this.x), 2)
					+ Math.pow((this.centerY - this.y), 2));
			if (this.dis > GameFrame.blockSize * 4) {
				this.speedX = this.speed * (this.centerX + GameFrame.blockSize * 4 - this.x) / (dis);
				this.speedY = this.speed * (this.centerY - this.y) / (dis);

			} else {

				this.speedX = 0;
				this.speedY = -speed;

				this.orbit = true;
			}

		}
		this.xx += this.speedX;
		this.yy += this.speedY;
		this.x = (int) this.xx;
		this.y = (int) this.yy;

		this.angle = Math.atan2(this.speedY, this.speedX) + Math.PI / 2;
		transform = new AffineTransform();
		transform.translate(this.x + this.size / 2, this.y + this.size / 2);
		transform.rotate(this.angle);
		transform.translate(-this.size / 2 * 5, -this.size / 2 * 5);
		transform.scale(this.size / 150.0 * 5, this.size / 150.0 * 5);
	}
}
