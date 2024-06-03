import java.awt.*;
import java.awt.image.BufferedImage;

public class Tower extends Rectangle {
	int gridX, gridY, type, range, damage, freq, cost;
	double angle = 0, estimateTime, dis, speed;
	BufferedImage image;
	int px, py;
	Enemy target;
	int level = 0;

	public Tower(int gridX, int gridY, int type) {
		super(gridX * GameFrame.blockSize + GameFrame.leftMargin, gridY * GameFrame.blockSize + GameFrame.topMargin,
				GameFrame.blockSize, GameFrame.blockSize);
		this.gridX = gridX;
		this.gridY = gridY;
		this.type = type;
		this.px = gridX * GameFrame.blockSize + GameFrame.leftMargin;
		this.py = gridY * GameFrame.blockSize + GameFrame.topMargin;
		this.image = GameFrame.towerImages.get(type);
		this.cost = MainFrame.towerCosts[type - 1];
		this.speed = MainFrame.towerSpeed[type - 1];
		this.freq = MainFrame.towerFreq[type - 1];
		this.damage = MainFrame.towerDamage[type - 1];
		this.range = MainFrame.towerRange[type - 1];
	}

	public void aim() {
		double largestDis = 0;
		for (Enemy enemy : GameFrame.enemys) {
			this.dis = Math.sqrt(Math.pow((enemy.x - this.px), 2) + Math.pow((enemy.y - this.py), 2));
			this.estimateTime = this.dis / this.speed;
			if (this.dis < this.range * GameFrame.blockSize && this.dis>largestDis) {
				largestDis = this.dis;
				this.target = enemy;
				this.angle = (Math.atan2(
						(enemy.x + enemy.width / 2 + enemy.speedX * this.estimateTime) - (this.x + this.width / 2),
						-(enemy.y + enemy.height / 2 + enemy.speedY * this.estimateTime) + (this.y + this.height / 2))) * 180
						/ Math.PI;

				return;
			}
		}
	}

	public void shoot() {
		
		if (this.target != null && this.target.hp > 0) {
			this.dis = Math.sqrt(Math.pow((this.target.x - this.px), 2) + Math.pow((this.target.y - this.py), 2));
			double speedX = this.speed * (this.target.x + this.target.speedX*this.estimateTime + this.target.width / 2 - (this.x+this.width/2)) / (this.dis);
			double speedY = this.speed * (this.target.y + this.target.speedY*this.estimateTime + this.target.height / 2 - (this.y+this.height/2)) / (this.dis);

			Bullet bullet = new Bullet(this.px + this.width / 2, this.py + this.height / 2, type, GameFrame.blockSize/6, speedX,
					speedY, damage);
			GameFrame.bullets.add(bullet);
		}
	}

}

class PenetrateTower extends Tower {
	PenetrateTower(int gridX, int gridY, int type) {
		super(gridX, gridY, type);
	}

	public void shoot() {
		if (this.target != null) {
			double speedX = this.speed * (this.target.x + this.target.speedX + this.target.width / 2 - this.x) / (this.dis);
			double speedY = this.speed * (this.target.y + this.target.speedY + this.target.height / 2 - this.y) / (this.dis);
			Bullet bullet = new PenetrateBullet(this.px + this.width / 2, this.py + this.height / 2, type, GameFrame.blockSize/8, speedX,
					speedY, damage);
			GameFrame.bullets.add(bullet);
		}
	}
}

class RingTower extends Tower {
	RingTower(int gridX, int gridY, int type) {
		super(gridX, gridY, type);
	}

	public void aim() {
		
		for (Enemy enemy : GameFrame.enemys) {
			this.dis = Math.sqrt(Math.pow((enemy.x - this.px), 2) + Math.pow((enemy.y - this.py), 2));
			if (this.dis < this.range * GameFrame.blockSize) {
				this.target = enemy;
			}
		}
	}

	public void shoot() {
		if (this.target != null && this.target.hp > 0) {
			for (int i = 0; i < 16; i++) {
				GameFrame.bullets.add(new PenetrateBullet(this.px + this.width / 2, this.py + this.height / 2, type, 2,
						speed * Math.sin((22.5 * i) / 180 * Math.PI), speed * Math.cos((22.5 * i) / 180 * Math.PI),
						damage));
			}
			;
			// speed * Math.sin(angleInRadians);
		}
	}
}

class BoomTower extends Tower {
	BoomTower(int gridX, int gridY, int type) {
		super(gridX, gridY, type);
	}

	public void shoot() {
		if (this.target != null && this.target.hp > 0) {
			double speedX = this.speed * (this.target.x + this.target.speedX + this.target.width / 2 - this.x) / (this.dis);
			double speedY = this.speed * (this.target.y + this.target.speedY + this.target.height / 2 - this.y) / (this.dis);
			Bullet boom = new Boom(this.px + this.width / 2, this.py + this.height / 2, type, GameFrame.blockSize/6, speedX,
					speedY, damage);
			GameFrame.bullets.add(boom);
		}
	}
}

class MissleTower extends Tower {
	MissleTower(int gridX, int gridY, int type) {
		super(gridX, gridY, type);
	}

	public void shoot() {
		double speedX=0;
		double speedY=0;
		if(this.target!=null&& this.target.hp>0){
			speedX = this.speed * (this.target.x + this.target.speedX + this.target.width / 2 - this.x) / (this.dis);
			speedY = this.speed * (this.target.y + this.target.speedY + this.target.height / 2 - this.y) / (this.dis);
		}
		Bullet missle = new Missle(this.px + this.width / 2, this.py + this.height / 2, type, GameFrame.blockSize/5,speedX,speedY, damage, this);
		GameFrame.bullets.add(missle);
		System.out.println(missle.size);

	}
}

/**
 * This is for the block object, independent from towers
 * But towers will follow the same format, inheritting grid x and y instead of
 * xy.
 */
class Block extends Rectangle {
	int cost, gridX, gridY;
	BufferedImage image;

	Block(int gridX, int gridY, int cost) {
		super(gridX * GameFrame.blockSize + GameFrame.leftMargin, gridY * GameFrame.blockSize + GameFrame.topMargin,
				GameFrame.blockSize, GameFrame.blockSize);
		this.gridX = gridX;
		this.gridY = gridY;
		this.cost = cost;
		this.image = GameFrame.towerImages.get(0);
	}
}

class TowerIcon extends Rectangle {
	int type, cost;
	String text;
	boolean select;
	BufferedImage icon;

	TowerIcon(int type, int cost) {
		super(MainFrame.panelWidth / 20 * type, GameFrame.buttomY + GameFrame.buttomHeight / 5,
				(int) (GameFrame.blockSize * 1.5), (int) (GameFrame.blockSize * 1.5));
		this.type = type;
		this.cost = cost;
		this.text = "$" + String.valueOf(cost);
		this.select = false;
		this.icon = GameFrame.towerImages.get(type - 1);
	}
}

class TowerPanel extends Rectangle {
	int type, gridX, gridY;
	SellButton sellButton;
	UpgradePanel upgradeButton;
	Color color;

	TowerPanel(int type, int gridX, int gridY) {
		super((gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin,
				gridY * GameFrame.blockSize + GameFrame.topMargin,
				GameFrame.blockSize * 2, GameFrame.blockSize * 3);
		this.type = type;
		this.gridX = gridX;
		this.gridY = gridY;
		this.sellButton = new SellButton(this.gridX, this.gridY);
		this.upgradeButton = new UpgradePanel(this.gridX, this.gridY);
		this.color = Color.WHITE;
	}

	public void update(int type, int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.type = type;
		this.x = (gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin;
		this.y = gridY * GameFrame.blockSize + GameFrame.topMargin;
		this.sellButton.update(this.gridX, this.gridY);
		this.upgradeButton.update(this.gridX, this.gridY);
	}

}

class UpgradePanel extends Rectangle {
	int type, gridX, gridY;
	Color color;

	UpgradePanel(int gridX, int gridY) {
		super((gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin,
				(gridY + 1) * GameFrame.blockSize + GameFrame.topMargin,
				GameFrame.blockSize * 2, GameFrame.blockSize);
		this.color = new Color(0, 250, 0);
	}

	public void update(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.x = (gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin;
		this.y = (gridY + 1) * GameFrame.blockSize + GameFrame.topMargin;
	}
}

class SellButton extends Rectangle {
	int gridX, gridY;
	Color color;

	SellButton(int gridX, int gridY) {
		super((gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin,
				(gridY + 2) * GameFrame.blockSize + GameFrame.topMargin,
				GameFrame.blockSize * 2, GameFrame.blockSize);
		this.color = new Color(250, 0, 0);
	}

	public void update(int gridX, int gridY) {
		this.gridX = gridX;
		this.gridY = gridY;
		this.x = (gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin;
		this.y = (gridY + 2) * GameFrame.blockSize + GameFrame.topMargin;

	}
}
