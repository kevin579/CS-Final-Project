import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends Rectangle {
	// Fields
	private int existTime;
	private int type, damage;
	private double xx, yy, speedX, speedY, targetX, targetY;
	private double speed;
	private boolean penetrate = false;
	private int explodeRadius = 0, explodeTime = -1;
	private int size;
	private BufferedImage image;
	private double angle = 0;
	private AffineTransform transform;
	private Tower parent;

	// Constructor
	public Bullet(int x, int y, int type, int size, double speedX, double speedY, int damage, Tower parent) {
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
		this.parent = parent;

		// Load images
		if (this.type == 5) {
			this.image = MainFrame.bulletImages.get(2);
		} else if (this.type == 6) {
			this.image = MainFrame.bulletImages.get(1);
		} else {
			this.image = MainFrame.bulletImages.get(0);
		}
	}

	// Getters and Setters
	public int getExistTime() {
		return existTime;
	}

	public void setExistTime(int existTime) {
		this.existTime = existTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public boolean isPenetrate() {
		return penetrate;
	}

	public void setPenetrate(boolean penetrate) {
		this.penetrate = penetrate;
	}

	public int getExplodeRadius() {
		return explodeRadius;
	}

	public void setExplodeRadius(int explodeRadius) {
		this.explodeRadius = explodeRadius;
	}

	public int getExplodeTime() {
		return explodeTime;
	}

	public void setExplodeTime(int explodeTime) {
		this.explodeTime = explodeTime;
	}

	public int getBulletSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public AffineTransform getTransform() {
		return transform;
	}

	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}

	public Tower getParent() {
		return parent;
	}

	public void setParent(Tower parent) {
		this.parent = parent;
	}

	// Method to move bullet
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

	// Method to handle explosion (empty implementation)
	public void explode() {
	}

	public void rotateImage() {
		transform = new AffineTransform();
		transform.translate(this.getX() + this.getBulletSize() / 2, this.getY() + this.getBulletSize() / 2);
		transform.rotate(this.getAngle());
		transform.translate(-this.getBulletSize() / 2 * 5, -this.getBulletSize() / 2 * 5);
		transform.scale(this.getBulletSize() / 150.0 * 5, this.getBulletSize() / 150.0 * 5);
	}
}

// Check for penetration
class PenetrateBullet extends Bullet {
	PenetrateBullet(int x, int y, int type, int size, double speedX, double speedY, int damage, Tower parent) {
		super(x, y, type, size, speedX, speedY, damage, parent);
		this.setPenetrate(true);
	}
}

// Class for the explosion effect
class Boom extends Bullet {

	Boom(int x, int y, int type, int size, double speedX, double speedY, int damage, Tower parent) {
		super(x, y, type, size, speedX, speedY, damage, parent);
		this.setExplodeRadius(MainFrame.explodeRadius[0]);
	}

	// Damage enemies in explosion area
	@Override
	public void explode() {
		if (this.getType() == 5) {
			MainFrame.explode.play();
		}
		for (Enemy enemy : GameFrame.enemys) {
			if (Math.sqrt(Math.pow(enemy.getX() - this.getX(), 2) + Math.pow(enemy.getY() - this.getY(), 2)) < this
					.getExplodeRadius()) {
				enemy.setHp(enemy.getHp() - this.getDamage());
			}
		}
		this.setExplodeTime(this.getExplodeTime() + 1);
		this.setSpeedX(0);
		this.setSpeedY(0);
	}
}

// Special bullet "missile" for tower
class Missile extends Boom {
	private double dis;
	private boolean orbit;
	private int centerX, centerY;

	// Constructor
	Missile(int x, int y, int type, int size, double speedX, double speedY, int damage, Tower parent) {
		super(x, y, type, size, speedX, speedY, damage, parent);
		this.setExplodeRadius(MainFrame.explodeRadius[1]);
		this.setParent(parent);
		this.setSpeed(MainFrame.towerSpeed[4]);
		this.centerX = (GameFrame.col / 2 - 1) * GameFrame.blockSize + GameFrame.leftMargin;
		this.centerY = GameFrame.row / 2 * GameFrame.blockSize + GameFrame.topMargin;
	}

	// Method to move missile
	@Override
	public void move() {
		if (orbit) {
			if (getParent().getTarget() != null && getParent().getTarget().getHp() > 0) {
				orbit = false;
				return;
			}
			double distanceX = this.getX() - this.centerX;
			double distanceY = this.getY() - this.centerY;
			double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

			// Calculate the centripetal acceleration
			double forceMagnitude = (getSpeed() * getSpeed()) / GameFrame.blockSize / 2;
			double accelerationX = -forceMagnitude * (distanceX / distance);
			double accelerationY = -forceMagnitude * (distanceY / distance);

			// Update the velocity
			this.setSpeedX(this.getSpeedX() + accelerationX);
			this.setSpeedY(this.getSpeedY() + accelerationY);

		} else if (getParent().getTarget() != null && getParent().getTarget().getHp() > 0
				&& this.getExplodeTime() < 0) {
			this.dis = Math.sqrt(Math.pow((getParent().getTarget().getX() - this.getX()), 2)
					+ Math.pow((getParent().getTarget().getY() - this.getY()), 2));
			this.setSpeedX(this.getSpeed()
					* (getParent().getTarget().getX() + getParent().getTarget().getWidth() / 2 - this.getX()) / (dis));
			this.setSpeedY(this.getSpeed()
					* (getParent().getTarget().getY() + getParent().getTarget().getHeight() / 2 - this.getY()) / (dis));

		} else if (this.getExplodeTime() < 0) {
			this.dis = Math.sqrt(Math.pow((this.centerX + GameFrame.blockSize * 4 - this.getX()), 2)
					+ Math.pow((this.centerY - this.getY()), 2));
			if (this.dis > GameFrame.blockSize * 4) {
				this.setSpeedX(this.getSpeed() * (this.centerX + GameFrame.blockSize * 4 - this.getX()) / (dis));
				this.setSpeedY(this.getSpeed() * (this.centerY - this.getY()) / (dis));

			} else {
				this.setSpeedX(0);
				this.setSpeedY(-getSpeed());
				this.orbit = true;
			}
		}

		this.setXx(this.getXx() + this.getSpeedX());
		this.setYy(this.getYy() + this.getSpeedY());
		this.setX((int) this.getXx());
		this.setY((int) this.getYy());

		this.setAngle(Math.atan2(this.getSpeedY(), this.getSpeedX()) + Math.PI / 2);
		rotateImage();
	}
}
