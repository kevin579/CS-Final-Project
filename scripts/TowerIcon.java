import java.awt.Rectangle;
import java.awt.image.BufferedImage;


// Make tower icons for game frame
//These are the icons to allow the user to click on them and select what type of tower they want
//They have the same images as the tower
public class TowerIcon extends Rectangle {
    private int type, cost;
    private String text;
    private boolean select;
    private BufferedImage icon;

    public TowerIcon(int type, int cost) {
        super(MainFrame.panelWidth / 15 * type, GameFrame.buttomY + GameFrame.buttomHeight / 5,
                (int) (GameFrame.blockSize * 1.5), (int) (GameFrame.blockSize * 1.5));
        this.type = type;
        this.cost = cost;
        this.text = "$" + String.valueOf(cost);
        this.select = false;
        this.icon = MainFrame.towerImages.get(type - 1);
    }

    // Getters and Setters
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }
}


// An image that shows at the end of every wave
class WaveEnd extends Rectangle {
    private double speed = 80;
    private double acceleration;

    public WaveEnd() {
        super(-MainFrame.panelWidth / 3, MainFrame.panelHeight / 3, MainFrame.panelWidth / 3,
                MainFrame.panelHeight / 5);
        this.acceleration = (this.speed - 8) * (this.speed - 8) / (MainFrame.panelWidth);
    }

    // Getters and Setters
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void move() {
        if (this.x + this.width / 2 < MainFrame.panelWidth / 2) {
            this.speed -= acceleration;
        } else {
            this.speed += acceleration;
        }
        this.speed = Math.max(this.speed, 3);
        this.x += this.speed;
    }
}
