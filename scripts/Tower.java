import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tower extends Rectangle {

    // Fields
    private int gridX, gridY, type, range, damage, freq, cost, level = 0;
    private double angle = 0, estimateTime, dis, speed;
    private BufferedImage image;
    private int px, py;
    private Enemy target;
    private int totalDamage;
    private Audio shootAudio;

    // Constructor
    public Tower(int gridX, int gridY, int type) {
        super(gridX * GameFrame.blockSize + GameFrame.leftMargin, gridY * GameFrame.blockSize + GameFrame.topMargin,
                GameFrame.blockSize, GameFrame.blockSize);
        this.gridX = gridX;
        this.gridY = gridY;
        this.type = type;
        this.px = (int) gridX * GameFrame.blockSize + GameFrame.leftMargin;
        this.py = (int) gridY * GameFrame.blockSize + GameFrame.topMargin;
        this.image = MainFrame.towerImages.get(type);
        this.cost = MainFrame.towerCosts[type - 1];
        this.speed = MainFrame.towerSpeed[type - 1];
        this.freq = MainFrame.towerFreq[type - 1];
        this.damage = MainFrame.towerDamage[type - 1];
        this.range = MainFrame.towerRange[type - 1];
        this.shootAudio = MainFrame.bulletAudios.get(type - 1);
    }

    // Getters and Setters
    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(double estimateTime) {
        this.estimateTime = estimateTime;
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getPy() {
        return py;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public int getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(int totalDamage) {
        this.totalDamage = totalDamage;
    }

    public void playAudio() {
        shootAudio.play();
    }

    // Make tower aim at the closest enemy
    public void aim() {
        double largestDis = 0;
        for (Enemy enemy : GameFrame.enemys) {
            this.dis = Math.sqrt(Math.pow((enemy.getX() - this.px), 2) + Math.pow((enemy.getY() - this.py), 2));
            this.estimateTime = this.dis / this.speed;
            if (this.dis < this.range * GameFrame.blockSize && enemy.getHp() > 0 && enemy.getDis() > largestDis) {
                largestDis = enemy.getDis();
                this.target = enemy;
                this.angle = (Math.atan2(
                        (enemy.getX() + enemy.getWidth() / 2 + enemy.getSpeedX() * this.estimateTime)
                                - (this.x + this.width / 2),
                        -(enemy.getY() + enemy.getHeight() / 2 + enemy.getSpeedY() * this.estimateTime)
                                + (this.y + this.height / 2)))
                        * 180
                        / Math.PI;
            }
        }
    }

    // Shoot at enemy
    public void shoot() {
        if (this.target != null && this.target.getHp() > 0 && this.dis < this.range * GameFrame.blockSize) {
            this.dis = Math
                    .sqrt(Math.pow((this.target.getX() - this.px), 2) + Math.pow((this.target.getY() - this.py), 2));
            if (this.dis < this.range * GameFrame.blockSize) {

                playAudio();

                double speedX = this.speed * (this.target.getX() + this.target.getSpeedX() * this.estimateTime
                        + this.target.getWidth() / 2 - (this.x + this.width / 2)) / (this.dis);
                double speedY = this.speed * (this.target.getY() + this.target.getSpeedY() * this.estimateTime
                        + this.target.getHeight() / 2 - (this.y + this.height / 2)) / (this.dis);
                GameFrame.bullets.add(generateBullet(speedX, speedY));
            }
        }
    }

    public Bullet generateBullet(double speedX, double speedY) {
        Bullet bullet = new Bullet(this.px + this.width / 2, this.py + this.height / 2, type,
                GameFrame.blockSize / 6, speedX, speedY, damage, this);
        return bullet;
    }

    public void upgrade(int level) {
        switch (this.getType()) {
            case 1:
                this.setDamage(this.getDamage() + 1 * level);
                if (this.getLevel() == 5) {
                    this.setFreq(this.getFreq() - 5);
                    this.setRange(this.getRange() + 1);
                }
                break;
            case 2:
                this.setDamage(this.getDamage() + 2 * level);
                if (this.getLevel() == 5) {
                    this.setFreq(this.getFreq() - 3);
                    this.setRange(this.getRange() + 1);
                }
                break;
            case 3:
                this.setDamage(this.getDamage() + 4 * level);
                if (this.getLevel() == 5) {
                    this.setFreq(this.getFreq() - 2);
                    this.setRange(this.getRange() + 1);
                }
                break;
            case 4:
                this.setDamage(this.getDamage() + 3 * level);
                if (this.getLevel() == 5) {
                    this.setDamage(this.getDamage() + 5);
                    this.setFreq(this.getFreq() - 5);
                    this.setRange(this.getRange() + 1);
                }
                break;
            case 5:
                this.setDamage(this.getDamage() + 45 * level);
                this.setFreq(this.getFreq() - 2 * level);
                if (this.getLevel() == 5) {
                    this.setDamage(this.getDamage() + 30);
                    this.setFreq(this.getFreq() - 5);
                }
                break;
            case 6:
                this.setDamage(this.getDamage() + 100 * level);
                this.setFreq(this.getFreq() - 2 * level);
                if (this.getLevel() == 5) {
                    this.setDamage(this.getDamage() + 150);
                    this.setRange(this.getRange() + 2);
                }
                break;
            case 7:
                this.setFreq(this.getFreq() - 1 * level);
                this.setDamage(this.getDamage() + 1 * level);
                if (this.getLevel() == 5) {
                    this.setSpeed(this.getSpeed() + 1);
                    this.setFreq(this.getFreq() - 1);
                    this.setDamage(this.getDamage() + 1);
                    this.setRange(this.getRange() + 1);
                }
                break;

            default:
                break;
        }
    }
}

// One type of tower
class PenetrateTower extends Tower {
    PenetrateTower(int gridX, int gridY, int type) {
        super(gridX, gridY, type);
    }

    @Override
    public Bullet generateBullet(double speedX, double speedY) {
        Bullet bullet = new PenetrateBullet(this.getPx() + this.width / 2, this.getPy() + this.height / 2,
                this.getType(),
                GameFrame.blockSize / 6, speedX, speedY, this.getDamage(), this);
        return bullet;
    }
}

// One type of tower
class RingTower extends Tower {
    RingTower(int gridX, int gridY, int type) {
        super(gridX, gridY, type);
    }

    // Aim at enemy. because it is a ring tower, it does not need to lock the first
    // enemy
    @Override
    public void aim() {
        for (Enemy enemy : GameFrame.enemys) {
            this.setDis(
                    Math.sqrt(Math.pow((enemy.getX() - this.getPx()), 2) + Math.pow((enemy.getY() - this.getPy()), 2)));
            if (this.getDis() < this.getRange() * GameFrame.blockSize && enemy.getHp() > 0) {
                this.setTarget(enemy);
                return;
            }
        }
        this.setTarget(null);
    }

    // Shoot bullets
    @Override
    public void shoot() {
        playAudio();
        if (this.getTarget() != null && this.getTarget().getHp() > 0) {
            for (int i = 0; i < 16; i++) {
                GameFrame.bullets.add(new PenetrateBullet((int) (this.getPx() + this.getWidth() / 2),
                        (int) (this.getPy() + this.getHeight() / 2), this.getType(), 2,
                        this.getSpeed() * Math.sin((22.5 * i) / 180 * Math.PI),
                        this.getSpeed() * Math.cos((22.5 * i) / 180 * Math.PI),
                        this.getDamage(), this));
            }
        }
    }
}

// One type of tower
class BoomTower extends Tower {
    BoomTower(int gridX, int gridY, int type) {
        super(gridX, gridY, type);
    }

    // Shoot at enemy
    @Override
    public Bullet generateBullet(double speedX, double speedY) {
        Bullet bullet = new Boom(this.getPx() + this.width / 2, this.getPy() + this.height / 2, this.getType(),
                GameFrame.blockSize / 6, speedX, speedY, this.getDamage(), this);
        return bullet;
    }
}

// One type of tower
class MissileTower extends Tower {
    MissileTower(int gridX, int gridY, int type) {
        super(gridX, gridY, type);
    }

    // Shoot at enemy
    @Override
    public Bullet generateBullet(double speedX, double speedY) {
        Bullet bullet = new Missile(this.getPx() + this.width / 2, this.getPy() + this.height / 2, this.getType(),
                GameFrame.blockSize / 6, speedX, speedY, this.getDamage(), this);
        return bullet;
    }
}

//This is the block object;
class Block extends Rectangle {
    private int gridX, gridY;
    private BufferedImage image;

    public Block(int gridX, int gridY) {
        super(gridX * GameFrame.blockSize + GameFrame.leftMargin, gridY * GameFrame.blockSize + GameFrame.topMargin,
                GameFrame.blockSize, GameFrame.blockSize);
        this.gridX = gridX;
        this.gridY = gridY;
        this.image = MainFrame.towerImages.get(0);
    }

    // Getters and Setters
    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

