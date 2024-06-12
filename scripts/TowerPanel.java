import java.awt.Color;
import java.awt.Rectangle;

// Class for towers in game
public class TowerPanel extends Rectangle {
    private int type, gridX, gridY;
    private SellButton sellButton;
    private UpgradeButton upgradeButton;
    private Color color;

    public TowerPanel(int type, int gridX, int gridY) {
        super((gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin,
                gridY * GameFrame.blockSize + GameFrame.topMargin,
                GameFrame.blockSize * 2, GameFrame.blockSize * 3);
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
        if (this.gridX >= GameFrame.col - 2) {
            this.x -= 3 * GameFrame.blockSize;
        }
        if (this.gridY >= GameFrame.row - 2) {
            this.y -= 2 * GameFrame.blockSize;
        }
        this.sellButton = new SellButton(this.gridX, this.gridY);
        this.upgradeButton = new UpgradeButton(this.gridX, this.gridY);
        this.color = Color.WHITE;
    }

    // Getters and Setters
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public SellButton getSellButton() {
        return sellButton;
    }

    public void setSellButton(SellButton sellButton) {
        this.sellButton = sellButton;
    }

    public UpgradeButton getUpgradeButton() {
        return upgradeButton;
    }

    public void setUpgradeButton(UpgradeButton upgradeButton) {
        this.upgradeButton = upgradeButton;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // Update the location and type of tower it is on
    public void update(int type, int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.type = type;
        this.x = (gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin;
        this.y = gridY * GameFrame.blockSize + GameFrame.topMargin;
        if (this.gridX >= GameFrame.col - 2) {
            this.x -= 3 * GameFrame.blockSize;
        }
        if (this.gridY >= GameFrame.row - 2) {
            this.y -= 2 * GameFrame.blockSize;
        }
        this.sellButton.update(this.gridX, this.gridY);
        this.upgradeButton.update(this.gridX, this.gridY);
    }
}

// Panel for upgrade when clicked on tower
class UpgradeButton extends Rectangle {
    private int type, gridX, gridY;
    private Color color;

    public UpgradeButton(int gridX, int gridY) {
        super((gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin,
                (gridY + 1) * GameFrame.blockSize + GameFrame.topMargin,
                GameFrame.blockSize * 2, GameFrame.blockSize);
        this.color = new Color(0, 250, 0);
        if (gridX >= GameFrame.col - 2) {
            this.x -= 3 * GameFrame.blockSize;
        }
        if (gridY >= GameFrame.row - 2) {
            this.y -= 2 * GameFrame.blockSize;
        }
    }

    // Getters and Setters
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // update location
    public void update(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.x = (gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin;
        this.y = (gridY + 1) * GameFrame.blockSize + GameFrame.topMargin;
        if (this.gridX >= GameFrame.col - 2) {
            this.x -= 3 * GameFrame.blockSize;
        }
        if (this.gridY >= GameFrame.row - 2) {
            this.y -= 2 * GameFrame.blockSize;
        }
    }
}

// Panel for sell when clicked on tower
class SellButton extends Rectangle {
    private int gridX, gridY;
    private Color color;

    public SellButton(int gridX, int gridY) {
        super((gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin,
                (gridY + 2) * GameFrame.blockSize + GameFrame.topMargin,
                GameFrame.blockSize * 2, GameFrame.blockSize);
        this.color = new Color(250, 0, 0);
        if (gridX >= GameFrame.col - 2) {
            this.x -= 3 * GameFrame.blockSize;
        }
        if (gridY >= GameFrame.row - 2) {
            this.y -= 2 * GameFrame.blockSize;
        }
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // update location
    public void update(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.x = (gridX + 1) * GameFrame.blockSize + GameFrame.leftMargin;
        this.y = (gridY + 2) * GameFrame.blockSize + GameFrame.topMargin;
        if (this.gridX >= GameFrame.col - 2) {
            this.x -= 3 * GameFrame.blockSize;
        }
        if (this.gridY >= GameFrame.row - 2) {
            this.y -= 2 * GameFrame.blockSize;
        }
    }
}
