import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameFrame extends JFrame implements ActionListener {
    // public static void main(String[] args) {
    // new GameFrame();
    // }

    GamePanel gamePanel;
    Timer timer;
    static int time = 0;
    static boolean edit = true;
    static int mouseX, mouseY;
    static boolean mouseClick = false;

    // Variables for grid and panel
    int panelWidth, panelHeight;
    static int row;
    static int col = 50;
    static int[][] towerGrid;
    static char[][] pathGrid;
    static int blockSize;
    static int titleHeight, buttomHeight, buttomY, gridHeight;
    static int topMargin, leftMargin, rightMargin;
    static int startX, startY, endX, endY;
    static int gridX, gridY;
    static Color transparentRed = new Color(250, 0, 0, 50);

    // variables for block and tower
    static ArrayList<Block> blocks;
    static ArrayList<Tower> towers;
    static ArrayList<Enemy> enemys;
    static ArrayList<Bullet> bullets;
    static ArrayList<TowerIcon> towerIcons;
    static TowerPanel towerPanel;
    static ArrayList<BufferedImage> towerImages;
    static ArrayList<BufferedImage> enemyImages;
    static ArrayList<BufferedImage> bulletImages;
    static int selectNum = 0;
    static BufferedImage explodeImage;
    static BufferedImage start;
    static BufferedImage end;

    // wave variables
    static char[][] wave = new char[100][100];
    static int waveNum = -1;
    static int pointer = 0;
    static int delay;
    static int enemyNum = 0;
    static boolean allOut = false;

    // Game variables
    static int playerHP = 20;
    static int score = 0;
    int difficult = 1;
    int cash = 80;
    boolean notSave = true;
    boolean load;
    int scoreRate = 1;

    // panel variables
    static Tower selectedTower;
    static Block selectedBlock;
    static boolean panelOperation;
    String userID;

    GameFrame(boolean load) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.load = load;

        panelWidth = MainFrame.panelWidth;
        panelHeight = MainFrame.panelHeight;
        gamePanel = new GamePanel();
        KeyInput key = new KeyInput();
        MouseInput mouse = new MouseInput();
        this.addMouseListener(mouse);
        this.addKeyListener(key);
        this.add(gamePanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        setup();
    }

    public void setup() {
        // Find a suitable size for the top and buttom panel based on the screen size
        topMargin = titleHeight = panelHeight / 15;
        buttomHeight = panelHeight / 8;
        buttomY = panelHeight - buttomHeight;
        gridHeight = panelHeight - buttomHeight - titleHeight;

        // based on the screen size, determine the size of a square block. Then
        // determine the max number of rows that can fit in the middle.
        blockSize = panelWidth / col;
        row = gridHeight / blockSize;
        towerGrid = new int[row][col];
        pathGrid = new char[row][col];

        // Slightly adjust the size of buttom panel and the margin to avoid deformity of
        // the last row and col of blocks
        buttomY = titleHeight + row * blockSize;
        buttomHeight = panelHeight - buttomY;
        gridHeight = panelHeight - buttomHeight - titleHeight;
        leftMargin = (panelWidth - col * blockSize) / 2;
        rightMargin = panelWidth - col * blockSize - leftMargin;

        startX = (col / 2 - 1) * blockSize + leftMargin;
        startY = (row / 2) * blockSize + topMargin;
        endX = (col / 2 + 1) * blockSize + leftMargin;
        endY = (row / 2) * blockSize + topMargin;

        // initialize variables
        blocks = new ArrayList<Block>();
        towers = new ArrayList<Tower>();
        enemys = new ArrayList<Enemy>();
        bullets = new ArrayList<Bullet>();
        towerIcons = new ArrayList<TowerIcon>();
        towerImages = new ArrayList<BufferedImage>();
        enemyImages = new ArrayList<BufferedImage>();
        bulletImages = new ArrayList<BufferedImage>();

        start = loadImage("scripts/Images/start.png");
        end = loadImage("scripts/Images/end.png");

        explodeImage = GameFrame.loadImage("scripts/Images/explosions.png");
        bulletImages.add(loadImage("scripts/Images/bullet.png"));
        bulletImages.add(loadImage("scripts/Images/boom.png"));
        bulletImages.add(loadImage("scripts/Images/missle.png"));

        enemyImages.add(loadImage("scripts/Images/enemy_2.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_3.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_4.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_5.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_6.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_7.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_8.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_9.png"));
        enemyImages.add(loadImage("scripts/Images/enemy_10.png"));

        towerImages.add(loadImage("scripts/Images/block.png"));
        towerImages.add(loadImage("scripts/Images/tower_1.png"));
        towerImages.add(loadImage("scripts/Images/tower_2.png"));
        towerImages.add(loadImage("scripts/Images/tower_3.png"));
        towerImages.add(loadImage("scripts/Images/tower_4.png"));
        towerImages.add(loadImage("scripts/Images/tower_5.png"));
        towerImages.add(loadImage("scripts/Images/tower_6.png"));
        towerImages.add(loadImage("scripts/Images/tower_7.png"));

        TowerIcon block = new TowerIcon(1, MainFrame.towerCosts[0]);
        TowerIcon t1 = new TowerIcon(2, MainFrame.towerCosts[1]);
        TowerIcon t2 = new TowerIcon(3, MainFrame.towerCosts[2]);
        TowerIcon t3 = new TowerIcon(4, MainFrame.towerCosts[3]);
        TowerIcon t4 = new TowerIcon(5, MainFrame.towerCosts[4]);
        TowerIcon t5 = new TowerIcon(6, MainFrame.towerCosts[5]);
        TowerIcon t6 = new TowerIcon(7, MainFrame.towerCosts[6]);
        TowerIcon t7 = new TowerIcon(8, MainFrame.towerCosts[7]);

        towerIcons.add(block);
        towerIcons.add(t1);
        towerIcons.add(t2);
        towerIcons.add(t3);
        towerIcons.add(t4);
        towerIcons.add(t5);
        towerIcons.add(t6);
        towerIcons.add(t7);
        findPath(towerGrid, row / 2, col / 2 + 1, pathGrid);
        loadWave();
        if (this.load) {
            loadGame();
        } else {
            this.difficult = MainFrame.dif;
            this.userID = MainFrame.userID;
        }
        int[] tempEnemyHPs = new int[8];
        double[] tempEnemySpeeds = new double[8];
        if (this.difficult == 1) {

            for (int i = 0; i < 8; i++) {
                tempEnemyHPs[i] = MainFrame.enemyHPs[i] / 2;
            }
            for (int i = 0; i < 8; i++) {
                tempEnemySpeeds[i] = MainFrame.enemySpeeds[i] / 2;
            }
            MainFrame.enemyHPs = tempEnemyHPs;
            MainFrame.enemySpeeds = tempEnemySpeeds;
            scoreRate = 1;
        } else if (this.difficult == 2) {

            for (int i = 0; i < 8; i++) {
                tempEnemyHPs[i] = (int) (MainFrame.enemyHPs[i] / 1.6);
            }
            for (int i = 0; i < 8; i++) {
                tempEnemySpeeds[i] = MainFrame.enemySpeeds[i] / 1.6;
            }
            MainFrame.enemyHPs = tempEnemyHPs;
            MainFrame.enemySpeeds = tempEnemySpeeds;
            scoreRate = 2;
        } else if (this.difficult == 3) {

            for (int i = 0; i < 8; i++) {
                tempEnemyHPs[i] = (int) (MainFrame.enemyHPs[i] / 1.33);
            }
            for (int i = 0; i < 8; i++) {
                tempEnemySpeeds[i] = MainFrame.enemySpeeds[i] / 1.33;
            }
            MainFrame.enemyHPs = tempEnemyHPs;
            MainFrame.enemySpeeds = tempEnemySpeeds;
            scoreRate = 3;
        } else {
            scoreRate = 5;
        }

        // Start the timer
        timer = new Timer(8, this);
        timer.setInitialDelay(1000);
        timer.start();

    }

    public void actionPerformed(ActionEvent e) {

        // Is there a panel for the tower
        getPanelItem();
        panelOperation = false;
        // get which tower the player wants to put
        getSelectedTower();

        // If the game is at edit mode
        if (edit) {
            Enemy pointer = new Enemy(0, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 2));
            enemys.add(pointer);
            // remove all bullets from previous wave
            if (notSave) {
                saveGame();
                notSave = false;
            }
            bullets.clear();
            if (mouseClick) {
                notSave = true;
                // If the player clicked on the game area
                if (mouseX > leftMargin && mouseX < panelWidth - rightMargin && mouseY > titleHeight
                        && mouseY < buttomY) {

                    // Identify which block the player is clicking.
                    gridX = (mouseX - leftMargin) / blockSize;
                    gridY = (mouseY - topMargin) / blockSize;

                    // Check if the user clicked on the tower panel and do the corresponding actions
                    excutePanelOperations();

                    // If the user did not click on the tower panel
                    if (panelOperation == false) {
                        if (selectNum == 1) {// the player select block
                            addBlock();
                        } else if (selectNum > 1) {// the player select player
                            addTower();
                        }
                    }
                }

            }

        }

        // When the games quits edit mode and starts a wave
        else {
            // Make the towers aim and shoot
            excuteTowerOperation();

            // Generate enemy based on the wave number.
            generateEnemy();

        }
        excuteEnemyOperations();
        // Make the bullets move, track, explode
        excuteBulletOperation();
        time++;
        mouseClick = false;
        mouseX = mouseY = 0;
        gamePanel.repaint();

    }

    private void getPanelItem() {
        selectedBlock = null;
        selectedTower = null;
        if (towerPanel != null) {

            for (Tower tower : towers) {
                if (tower.gridX == towerPanel.gridX && tower.gridY == towerPanel.gridY) {
                    selectedTower = tower;
                    break;

                }
            }
            if (selectedTower == null) {
                for (Block block : blocks) {
                    if (block.gridX == towerPanel.gridX && block.gridY == towerPanel.gridY) {
                        selectedBlock = block;
                        break;

                    }
                }
            }
        }
    }

    private void excutePanelOperations() {
        if (towerPanel != null) {

            if (towerPanel.upgradeButton.contains(mouseX, mouseY)) {
                if (towerPanel.type != 1) {
                    for (Tower tower : towers) {
                        if (tower.gridX == towerPanel.gridX && tower.gridY == towerPanel.gridY) {
                            if (cash >= MainFrame.towerCosts[towerPanel.type - 1] / 2 && tower.level < 5) {
                                tower.level++;
                                cash -= MainFrame.towerCosts[towerPanel.type - 1] / 2;
                                switch (tower.type) {
                                    case 1:
                                        tower.damage += 1;
                                        if (tower.level == 5) {
                                            tower.freq -= 5;
                                        }
                                        break;
                                    case 2:
                                        tower.damage += 2;
                                        if (tower.level == 5) {
                                            tower.freq -= 3;
                                        }
                                        break;
                                    case 3:
                                        tower.damage += 5;
                                        if (tower.level == 5) {
                                            tower.freq -= 2;
                                        }
                                        break;
                                    case 4:
                                        tower.freq = tower.freq * 2 / 3 + 1;
                                        if (tower.level == 5) {
                                            tower.damage += 2;
                                        }
                                        break;
                                    case 5:
                                        tower.damage += 20;
                                        tower.freq--;
                                        if (tower.level == 5) {
                                            tower.freq -= 5;
                                            tower.damage += 20;
                                        }
                                        break;
                                    case 6:
                                        tower.damage += 50;
                                        tower.freq -= 2;
                                        if (tower.level == 5) {
                                            tower.damage += 80;
                                        }
                                        break;
                                    case 7:
                                        tower.freq--;
                                        if (tower.level == 5) {
                                            tower.speed--;
                                            tower.damage += 1;
                                        }
                                        break;

                                    default:
                                        break;
                                }

                            }
                            break;
                        }
                    }
                }
                towerPanel = null;
                panelOperation = true;
            } else if (towerPanel.sellButton.contains(mouseX, mouseY)) {

                if (towerPanel.type == 1) {

                    ArrayList<Block> tempBlocks = new ArrayList<Block>(blocks);

                    for (Block block : tempBlocks) {
                        if (block.gridX == towerPanel.gridX && block.gridY == towerPanel.gridY) {
                            blocks.remove(block);
                            cash += MainFrame.towerCosts[0];
                            towerGrid[towerPanel.gridY][towerPanel.gridX] = 0;
                            findPath(towerGrid, row / 2, col / 2 + 1, pathGrid);

                            break;
                        }
                    }
                } else {
                    ArrayList<Tower> tempTowers = new ArrayList<Tower>(towers);

                    for (Tower tower : tempTowers) {
                        if (tower.gridX == towerPanel.gridX && tower.gridY == towerPanel.gridY) {
                            towers.remove(tower);
                            cash += MainFrame.towerCosts[towerPanel.type - 1] * (1 + tower.level / 2.0);
                            towerGrid[towerPanel.gridY][towerPanel.gridX] = 1;
                            break;
                        }
                    }
                }
                towerPanel = null;
                panelOperation = true;
            }
        }
    }

    private void addBlock() {
        if (towerGrid[gridY][gridX] != 0) {
            if (towerPanel == null) {
                towerPanel = new TowerPanel(towerGrid[gridY][gridX], gridX, gridY);
            } else {
                if (gridX == towerPanel.gridX && gridY == towerPanel.gridY) {
                    towerPanel = null;
                } else {
                    towerPanel.update(towerGrid[gridY][gridX], gridX, gridY);
                }
            }
        } else {
            towerPanel = null;
            if (cash >= MainFrame.towerCosts[0]) {
                for (int i = 0; i < pathGrid.length; i++) {
                    for (int j = 0; j < pathGrid[0].length; j++) {
                        pathGrid[i][j] = '+';
                    }
                }

                towerGrid[gridY][gridX] = 1;
                if (findPath(towerGrid, row / 2, col / 2 + 1, pathGrid)
                        && (gridY != row / 2 || gridX != col / 2 + 1)) {
                    for (int i = 0; i < pathGrid.length; i++) {
                        for (int j = 0; j < pathGrid[0].length; j++) {
                            System.out.print(pathGrid[i][j] + " ");
                        }
                        System.out.println();
                    }
                    cash -= MainFrame.towerCosts[0];
                    Block block = new Block(gridX, gridY, 10);
                    blocks.add(block);
                } else {
                    System.out.println("Not avaliabe");
                    towerGrid[gridY][gridX] = 0;
                }
            }
        }
    }

    private void addTower() {
        if (towerGrid[gridY][gridX] == 0) {
            System.out.println("Requires block");
            towerPanel = null;
        } else if (towerGrid[gridY][gridX] > 1) {
            if (towerPanel == null) {
                towerPanel = new TowerPanel(towerGrid[gridY][gridX], gridX, gridY);

            } else {
                if (gridX == towerPanel.gridX && gridY == towerPanel.gridY) {
                    towerPanel = null;

                } else {
                    towerPanel.update(towerGrid[gridY][gridX], gridX, gridY);
                }

            }

        } else if (cash >= MainFrame.towerCosts[selectNum - 1]) {
            towerPanel = null;
            cash -= MainFrame.towerCosts[selectNum - 1];
            towerGrid[gridY][gridX] = selectNum;
            Tower tower;
            if (selectNum == 5) {
                tower = new PenetrateTower(gridX, gridY, selectNum - 1);
            } else if (selectNum == 6) {
                tower = new MissleTower(gridX, gridY, selectNum - 1);

            } else if (selectNum == 7) {
                tower = new BoomTower(gridX, gridY, selectNum - 1);

            } else if (selectNum == 8) {
                tower = new RingTower(gridX, gridY, selectNum - 1);
            } else {
                tower = new Tower(gridX, gridY, selectNum - 1);
            }
            towers.add(tower);
        }
    }

    private void getSelectedTower() {

        // Select by mouse click
        for (TowerIcon icon : towerIcons) {
            if (icon.contains(mouseX, mouseY)) {
                // remove the highlignht of the old selected icon
                for (TowerIcon i : towerIcons) {
                    i.select = false;
                }
                // mark the selected icon
                icon.select = true;
                selectNum = icon.type;
            }
        }

        // select by keyboard input, coop with keyadaptor;
        for (int i = 0; i < towerIcons.size(); i++) {

            if (i + 1 == selectNum) {
                towerIcons.get(i).select = true;
            } else {
                towerIcons.get(i).select = false;
            }
        }
    }

    private void excuteBulletOperation() {
        ArrayList<Bullet> tempBullets = new ArrayList<Bullet>(bullets);
        for (Bullet bullet : tempBullets) {
            bullet.move();
            // Remove out of boundet
            if (bullet.x + bullet.width < 0 || bullet.x > panelWidth || bullet.y < titleHeight
                    || bullet.y > buttomY) {
                bullets.remove(bullet);
            }
            // remove to far away from tower.
            if (bullet.existTime * bullet.speed > (MainFrame.towerRange[bullet.type - 1] + 1) * blockSize) {
                if (bullet.explodeRadius > 0) {
                    bullet.explode();
                } else {
                    bullets.remove(bullet);
                }
            }
            if (bullet.explodeTime >= 0) {
                bullet.explodeTime++;
            }
            if (bullet.explodeTime > 16) {
                bullets.remove(bullet);
            }

        }
    }

    private void generateEnemy() {
        if (time % (15 + waveNum / 3) == 0 && pointer < wave[waveNum].length) {
            if (delay > 0) {
                delay--;
            } else {
                char element = wave[waveNum][pointer];
                int elementNum = Character.getNumericValue(element);
                if (elementNum < 10) {
                    delay = elementNum;
                } else {
                    Enemy enemy;
                    if (waveNum < 5) {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) / 2.0));
                    } else if (waveNum >= 5 && waveNum < 10) {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1)));
                    } else if (waveNum >= 10 && waveNum < 15) {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 1.5));
                    } else if (waveNum >= 15 && waveNum < 20) {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 2.5));
                    } else if (waveNum >= 20 && waveNum < 25) {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 3.5));
                    } else {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 3));
                    }
                    enemys.add(enemy);
                    enemyNum++;
                }
                pointer++;
            }
            if (pointer == wave[waveNum].length) {
                allOut = true;
            }

        }
        if (allOut && enemyNum == 0) {
            edit = true;
            cash += (waveNum + 1) * 20;
            score += (waveNum + 1) * 50 * scoreRate;
        }
    }

    private void excuteTowerOperation() {
        for (Tower tower : towers) {
            tower.aim();
            if (time % tower.freq == 0) {
                tower.shoot();
            }
        }
    }

    private void excuteEnemyOperations() {
        ArrayList<Enemy> tempEnemys = new ArrayList<Enemy>(enemys);

        for (Enemy enemy : tempEnemys) {
            enemy.move();
            ArrayList<Bullet> tempBullets = new ArrayList<Bullet>(bullets);
            for (Bullet bullet : tempBullets) {
                if (bullet.intersects(enemy) && bullet.explodeTime < 0) {
                    enemy.hp -= bullet.damage;
                    if (!bullet.penetrate) {
                        if (bullet.explodeRadius > 0) {
                            bullet.explode();
                        } else {
                            bullets.remove(bullet);
                        }
                    }
                }
            }
            if (enemy.hp <= 0) {
                enemy.die();
                enemys.remove(enemy);
                cash += enemy.type * 10;
                score += enemy.type * 20 * scoreRate;

            }
        }
    }

    private class GamePanel extends JPanel {

        GamePanel() {
            this.setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setBackground(Color.WHITE);

        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D gc = (Graphics2D) g;
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawGrid(gc);

            drawTower(gc);
            drawEnemy(gc);
            drawBullet(gc);

            drawTowerPanel(gc);

        }

        public void drawGrid(Graphics2D gc) {

            gc.setColor(Color.GRAY);
            gc.fillRect(0, titleHeight, leftMargin, gridHeight);
            gc.fillRect(panelWidth - rightMargin, titleHeight, rightMargin, gridHeight);
            gc.fillRect(0, 0, panelWidth, titleHeight);
            gc.fillRect(0, buttomY, panelWidth, buttomHeight);

            for (int i = 0; i <= col; i++) {
                gc.drawLine(i * blockSize + leftMargin, 0, i * blockSize + leftMargin, buttomY);
            }
            for (int i = 0; i <= row; i++) {
                gc.drawLine(0, i * blockSize + titleHeight, panelWidth, i * blockSize + titleHeight);
            }
            // gc.setColor(Color.BLACK);
            // gc.fillRect((col / 2 - 1) * blockSize + leftMargin, row / 2 * blockSize +
            // topMargin, blockSize, blockSize);
            gc.drawImage(start, (col / 2 - 1) * blockSize + leftMargin, row / 2 * blockSize + topMargin, blockSize,
                    blockSize, null);
            // gc.setColor(Color.BLACK);
            // gc.fillRect((col / 2 + 1) * blockSize + leftMargin, row / 2 * blockSize +
            // topMargin, blockSize, blockSize);
            gc.drawImage(end, (col / 2 + 1) * blockSize + leftMargin, row / 2 * blockSize + topMargin, blockSize,
                    blockSize, null);
            gc.setColor(Color.BLACK);
            // Draw top panel
            gc.setFont(new Font("Times New Roman", Font.PLAIN, 30));
            if (edit) {
                gc.drawString("Edit Mode", 100, titleHeight / 2);
            } else {
                gc.drawString("Play Mode", 100, titleHeight / 2);
            }
            gc.drawString("Wave: " + String.valueOf(waveNum + 1), 300, titleHeight / 2);
            gc.drawString("Score: " + String.valueOf(score), 500, titleHeight / 2);
            gc.drawString(userID, 700, titleHeight / 2);

            // Draw Buttom Panel

            for (TowerIcon icon : towerIcons) {
                gc.setColor(Color.WHITE);
                if (icon.select) {
                    gc.fillRect(icon.x - 2, icon.y - 2, icon.width + 4, icon.height + 4);
                }
                // gc.setColor(Color.BLACK);
                // gc.fillRect(icon.x, icon.y, icon.width, icon.height);
                gc.drawImage(icon.icon, icon.x, icon.y, icon.width, icon.height, null);

                gc.setColor(Color.BLACK);
                gc.drawString(icon.text, icon.x, (int) (icon.y + blockSize * 2.5));
            }
            gc.drawString("Cash: " + String.valueOf(cash), panelWidth / 10 * 9, buttomY + buttomHeight / 3);
            gc.drawString("Life: " + String.valueOf(playerHP), panelWidth / 10 * 9, buttomY + buttomHeight / 3 * 2);

        }

        public void drawTower(Graphics2D gc) {
            for (Block block : blocks) {
                gc.drawImage(block.image, block.x, block.y, block.width, block.height, null);

            }
            for (Tower tower : towers) {
                int cx = tower.x + blockSize / 2;
                int cy = tower.y + blockSize / 2;
                AffineTransform Towertransform = new AffineTransform();
                Towertransform.translate(cx, cy);
                Towertransform.rotate(Math.toRadians(tower.angle));
                Towertransform.translate(-blockSize / 2, -blockSize / 2);
                Towertransform.scale(blockSize / 1500.0, blockSize / 1500.0);
                gc.drawImage(tower.image, Towertransform, null);

                gc.setColor(Color.YELLOW);
                // gc.fillRect(tower.x,tower.y,blockSize/5,blockSize/5);
                for (int i = 0; i < tower.level; i++) {
                    double centerX = tower.x + blockSize / 5 + blockSize / 5 * i;
                    double centerY = tower.y + blockSize / 5;
                    double outerRadius = blockSize / 5;
                    double innerRadius = outerRadius / 2.5;

                    Polygon star = new Polygon();
                    for (int u = 0; u < 10; u++) {
                        double angle = Math.PI / 5 * u + Math.PI / 2;
                        double radius = (u % 2 == 0) ? outerRadius : innerRadius;
                        int dx = (int) (centerX + Math.cos(angle) * radius);
                        int dy = (int) (centerY - Math.sin(angle) * radius);
                        star.addPoint(dx, dy);
                    }

                    gc.fill(star);

                }

            }
        }

        public void drawBullet(Graphics2D gc) {
            for (Bullet bullet : bullets) {
                // draw explsion animation
                if (bullet.explodeRadius > 0 && bullet.explodeTime >= 0) {
                    gc.drawImage(explodeImage, bullet.x - bullet.explodeRadius / 2, bullet.y - bullet.explodeRadius / 2,
                            bullet.x + bullet.explodeRadius / 2, bullet.y + bullet.explodeRadius / 2,
                            bullet.explodeTime * 64, 192, (bullet.explodeTime + 1) * 64, 256, null);
                }
                // Draw Missle with correct direction
                if (bullet.explodeTime < 0 && bullet.transform != null) {

                    gc.drawImage(bullet.image, bullet.transform, null);
                    // gc.fillRect(bullet.x, bullet.y, bullet.size, bullet.size);
                }

            }
        }

        public void drawEnemy(Graphics2D gc) {
            gc.setColor(transparentRed);
            for (Enemy enemy : enemys) {
                if (enemy.type == 0) {
                    gc.fillRect(enemy.x, enemy.y, blockSize, blockSize);
                } else {
                    int cx = enemy.x + blockSize / 2;
                    int cy = enemy.y + blockSize / 2;
                    AffineTransform Enemytransform = new AffineTransform();

                    Enemytransform.translate(cx, cy);
                    Enemytransform.rotate(Math.toRadians(enemy.angle));
                    Enemytransform.translate(-blockSize / 2, -blockSize / 2);
                    Enemytransform.scale(blockSize / 348.0, blockSize / 348.0);
                    gc.drawImage(enemy.image, Enemytransform, null);
                    enemy.drawHP(gc);
                }
            }
        }

        public void drawTowerPanel(Graphics2D gc) {
            if (towerPanel != null) {
                gc.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                if (selectedTower != null) {
                    gc.setColor(Color.BLACK);
                    gc.drawOval(selectedTower.x - selectedTower.range * blockSize + blockSize / 2,
                            selectedTower.y - selectedTower.range * blockSize + blockSize / 2,
                            selectedTower.range * blockSize * 2, selectedTower.range * blockSize * 2);
                    gc.setColor(towerPanel.color);
                    gc.fillRect(towerPanel.x, towerPanel.y, towerPanel.width, towerPanel.height);
                    gc.setColor(towerPanel.upgradeButton.color);
                    gc.fillRect(towerPanel.upgradeButton.x, towerPanel.upgradeButton.y, towerPanel.upgradeButton.width,
                            towerPanel.upgradeButton.height);
                    gc.setColor(towerPanel.sellButton.color);
                    gc.fillRect(towerPanel.sellButton.x, towerPanel.sellButton.y, towerPanel.sellButton.width,
                            towerPanel.sellButton.height);
                    gc.setColor(Color.BLACK);
                    gc.drawString("lv " + String.valueOf(selectedTower.level),
                            towerPanel.x,
                            towerPanel.y + blockSize * 2 / 3);
                    gc.drawString("↑ - " + String.valueOf(MainFrame.towerCosts[towerPanel.type - 1] / 2),
                            towerPanel.upgradeButton.x,
                            towerPanel.upgradeButton.y + blockSize * 2 / 3);
                    gc.drawString(
                            "x + " + String.valueOf(
                                    (int) (MainFrame.towerCosts[towerPanel.type - 1]
                                            * (1 + selectedTower.level / 2.0))),
                            towerPanel.sellButton.x,
                            towerPanel.sellButton.y + blockSize * 2 / 3);
                }
                if (selectedBlock != null) {
                    gc.setColor(towerPanel.color);
                    gc.fillRect(towerPanel.x, towerPanel.y, towerPanel.width, towerPanel.height);
                    gc.setColor(towerPanel.sellButton.color);
                    gc.fillRect(towerPanel.sellButton.x, towerPanel.sellButton.y, towerPanel.sellButton.width,
                            towerPanel.sellButton.height);
                    gc.setColor(Color.BLACK);
                    gc.drawString("Cannot",
                            towerPanel.x,
                            towerPanel.y + blockSize * 2 / 3);
                    gc.drawString("Upgrade",
                            towerPanel.upgradeButton.x,
                            towerPanel.upgradeButton.y + blockSize * 2 / 3);
                    gc.drawString(
                            "x + " + String
                                    .valueOf(MainFrame.towerCosts[0]),
                            towerPanel.sellButton.x,
                            towerPanel.sellButton.y + blockSize * 2 / 3);

                }

            }
        }
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

    static void loadWave() {
        try {
            File file = new File("scripts/wave.txt");
            FileReader in = new FileReader(file);
            BufferedReader fileReader = new BufferedReader(in);
            String text;
            int w = 0;
            while ((text = fileReader.readLine()) != null) {
                wave[w] = text.toCharArray();
                w++;
            }
            fileReader.close();
            in.close();
        } catch (Exception e) {
            System.out.println("cannot find file");
        }
    }

    public void saveGame() {
        try {
            File progressFile = new File("scripts/progress.txt");
            FileWriter out = new FileWriter(progressFile, false);
            BufferedWriter writer = new BufferedWriter(out);
            String[][] record = new String[row][col];

            for (Block block : blocks) {
                record[block.gridY][block.gridX] = "1";
            }
            for (Tower tower : towers) {
                record[tower.gridY][tower.gridX] = String.valueOf(tower.level) + String.valueOf(tower.type + 1);

            }
            for (int y = 0; y < row; y++) {
                for (int x = 0; x < col; x++) {
                    if (record[y][x] == null) {
                        writer.write("0");
                        ;
                    } else {
                        writer.write(record[y][x]);
                    }
                    if (x != col - 1) {
                        writer.write(" ");
                    }
                }
                writer.newLine();
            }
            writer.write(String.valueOf(cash));
            writer.newLine();
            writer.write(String.valueOf(score));
            writer.newLine();
            writer.write(String.valueOf(waveNum));
            writer.newLine();
            writer.write(String.valueOf(playerHP));
            writer.newLine();
            writer.write(String.valueOf(this.difficult));
            writer.newLine();
            writer.write(userID);

            File rankingFile = new File("scripts/ranking.txt");
            out = new FileWriter(rankingFile, true);
            writer = new BufferedWriter(out);
            String output = userID + String.valueOf(score);
            writer.newLine();
            writer.write(output);
            writer.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Cannot find progress file when writing");
        }
    }

    public void loadGame() {

        try {
            File progressFile = new File("scripts/progress.txt");
            FileReader in = new FileReader(progressFile);
            BufferedReader reader = new BufferedReader(in);
            for (int y = 0; y < row; y++) {
                String row = reader.readLine();
                if (row == null) {
                    reader.close();
                    in.close();
                    return;
                }
                String[] num = row.split(" ");
                for (int x = 0; x < col; x++) {

                    int itemNum = Integer.parseInt(num[x]);

                    if (itemNum >= 1) {
                        towerGrid[y][x] = itemNum % 10;
                        Block block = new Block(x, y, 10);
                        blocks.add(block);
                    }
                    if (itemNum > 1) {
                        Tower tower;
                        if (itemNum % 10 == 2) {
                            tower = new Tower(x, y, itemNum % 10 - 1);
                        } else if (itemNum % 10 == 3) {
                            tower = new Tower(x, y, itemNum % 10 - 1);

                        } else if (itemNum % 10 == 4) {
                            tower = new Tower(x, y, itemNum % 10 - 1);

                        } else if (itemNum % 10 == 5) {
                            tower = new PenetrateTower(x, y, itemNum % 10 - 1);
                        } else if (itemNum % 10 == 6) {
                            tower = new MissleTower(x, y, itemNum % 10 - 1);

                        } else if (itemNum % 10 == 7) {
                            tower = new BoomTower(x, y, itemNum % 10 - 1);

                        } else if (itemNum % 10 == 8) {
                            tower = new RingTower(x, y, itemNum % 10 - 1);
                        } else {
                            tower = new Tower(x, y, itemNum % 10 - 1);
                        }
                        tower.level = itemNum / 10;
                        switch (tower.type) {
                            case 1:
                                tower.damage += tower.level;
                                if (tower.level == 5) {
                                    tower.freq -= 5;
                                }
                                break;
                            case 2:
                                tower.damage += 2 * tower.level;
                                if (tower.level == 5) {
                                    tower.freq -= 3;
                                }
                                break;
                            case 3:
                                tower.damage += 5 * tower.level;
                                if (tower.level == 5) {
                                    tower.freq -= 2;
                                }
                                break;
                            case 4:
                                tower.damage += tower.level;
                                if (tower.level == 5) {
                                    tower.damage += 2;
                                }
                                break;
                            case 5:
                                tower.damage += 20 * tower.level;
                                tower.freq -= tower.level;
                                if (tower.level == 5) {
                                    tower.freq -= 5;
                                    tower.damage += 20;
                                }
                                break;
                            case 6:
                                tower.damage += 35 * tower.level;
                                tower.freq -= tower.level;
                                if (tower.level == 5) {
                                    tower.damage += 50;
                                }
                                break;
                            case 7:
                                tower.freq -= tower.level * 2;
                                tower.damage += tower.level;
                                if (tower.level == 5) {
                                    tower.speed--;
                                    tower.freq--;
                                    tower.damage += 2;
                                }
                                break;

                            default:
                                break;
                        }
                        towers.add(tower);
                    }

                }
            }
            findPath(towerGrid, row / 2, col / 2 + 1, pathGrid);
            cash = Integer.parseInt(reader.readLine());
            score = Integer.parseInt(reader.readLine());
            waveNum = Integer.parseInt(reader.readLine());
            playerHP = Integer.parseInt(reader.readLine());
            this.difficult = Integer.parseInt(reader.readLine());
            this.userID = reader.readLine();
            reader.close();
            in.close();
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
    }

    /**
     * It will update the 2D array pathGrid.
     * It will run a BFS from the end as start.
     * every time it expands from the start, it will write an int 1-4 at the
     * coresponding pathGrid representing move up down left or right.
     * For example, it starts at the end point (26,13) and one of its neighbor is
     * (25,13). It reachs (25,13) by moving left,
     * which means all enemies located at the block (25,13) will have to move right
     * to reach (26,13)
     * 
     */
    private boolean findPath(int[][] grid, int x1, int y1, char[][] pathArray) {
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        // Corresponding arrows for directions: right, down, left, up

        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];

        // Queue for BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { x1, y1 });

        visited[x1][y1] = true;
        pathArray[x1][y1] = 'E'; // Mark the start point
        char[] arrows = { '←', '↑', '→', '↓' };

        // Perform BFS
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currX = current[0];
            int currY = current[1];

            // Explore all four possible directions
            for (int i = 0; i < directions.length; i++) {
                int newX = currX + directions[i][0];
                int newY = currY + directions[i][1];

                if (newX >= 0 && newX < row && newY >= 0 && newY < col && grid[newX][newY] == 0
                        && !visited[newX][newY]) {
                    queue.add(new int[] { newX, newY });
                    visited[newX][newY] = true;
                    pathArray[newX][newY] = arrows[i];
                }
            }
        }
        if (pathArray[row / 2][col / 2 - 1] != '+') {
            return true;
        } else {
            return false;
        }
    }

    class KeyInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER && edit == true) {
                findPath(towerGrid, row / 2, col / 2 + 1, pathGrid);
                enemys.clear();
                enemyNum = 0;
                time = 0;
                edit = false;
                pointer = 0;
                waveNum++;
                delay = 0;
                allOut = false;
                notSave = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_1) {
                selectNum = 1;
            }
            if (e.getKeyCode() == KeyEvent.VK_2) {
                selectNum = 2;
            }
            if (e.getKeyCode() == KeyEvent.VK_3) {
                selectNum = 3;
            }
            if (e.getKeyCode() == KeyEvent.VK_4) {
                selectNum = 4;
            }
            if (e.getKeyCode() == KeyEvent.VK_5) {
                selectNum = 5;
            }
            if (e.getKeyCode() == KeyEvent.VK_6) {
                selectNum = 6;
            }
            if (e.getKeyCode() == KeyEvent.VK_7) {
                selectNum = 7;
            }
            if (e.getKeyCode() == KeyEvent.VK_8) {
                selectNum = 8;
            }
            if (e.getKeyCode() == KeyEvent.VK_9) {
                selectNum = 9;
            }

        }

    }
}

class MouseInput extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        // System.out.printf("%d,%d", e.getX(), e.getY());
        GameFrame.mouseClick = true;
        GameFrame.mouseX = e.getX();
        GameFrame.mouseY = e.getY();

    }
}