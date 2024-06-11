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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameFrame extends JFrame implements ActionListener {
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
    static Color transparentOrange = new Color(255, 165, 0, 50);

    // variables for block and tower
    static ArrayList<Block> blocks;
    static ArrayList<Tower> towers;
    static ArrayList<Enemy> enemys;
    static ArrayList<Bullet> bullets;
    static ArrayList<TowerIcon> towerIcons;
    static int selectNum = 0;// which type of tower the user want to place
    static TowerPanel towerPanel; // this is a small panel that appear when clicking tower of block to make user
                                  // upgrade or sell it

    // wave variables
    static char[][] wave = new char[100][100];
    static int waveNum = -1;
    static int pointer = 0;
    static int delay;
    static int enemyNum = 0;
    static boolean allOut = false;
    static WaveEnd waveEnd;

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

        gamePanel = new GamePanel();

        // add key and mouse inputs
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
        // get panel width and height
        panelWidth = MainFrame.panelWidth;
        panelHeight = MainFrame.panelHeight;

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

        // Find location of start and end
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

        // initialize the tower icons
        for (int i = 0; i < 8; i++) {
            towerIcons.add(new TowerIcon(i + 1, MainFrame.towerCosts[i]));
        }

        // set the iniital path
        findPath();

        // laod all the enemies that will appear in the game from file to array
        loadWave();

        // Depenfing on the user's choice, create new game or load.
        if (this.load) {
            loadGame();
        } else {
            // get the userId and difficulty that the user chooses if start new game
            this.difficult = MainFrame.dif;
            this.userID = MainFrame.userID;
        }

        // Depending on the difficulty, set the enemies hp and speed;
        adjustDifficulty();

        // Start the timer
        timer = new Timer(8, this);
        timer.setInitialDelay(1000);
        timer.start();

        MainFrame.bgm.play();

    }

    public void actionPerformed(ActionEvent e) {
        // if there a panel for the tower, get select towre
        getPanelItem();
        panelOperation = false;

        // get which tower the player wants to put
        getSelectedTower();

        // Check if the user clicked on the tower panel and do the corresponding actions
        excutePanelOperations();

        // If the game is at edit mode
        if (edit) {
            if (waveEnd != null) {
                waveEnd.move();
                if (waveEnd.x > panelWidth) {
                    waveEnd = null;
                }
            }
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

                    // If the user did not click on the tower panel
                    if (panelOperation == false) {
                        if (towerGrid[gridY][gridX] != 0) {
                            updatePanel();
                        }
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
        if (playerHP <= 0) {
            endGame();
        }
        time++;
        mouseClick = false;
        mouseX = mouseY = 0;
        gamePanel.repaint();

    }

    // Get which block or tower the panel is on
    private void getPanelItem() {
        selectedBlock = null;
        selectedTower = null;
        if (towerPanel != null) {

            for (Tower tower : towers) {
                if (tower.getGridX() == towerPanel.getGridX() && tower.getGridY() == towerPanel.getGridY()) {
                    selectedTower = tower;
                    break;

                }
            }
            if (selectedTower == null) {
                for (Block block : blocks) {
                    if (block.getGridX() == towerPanel.getGridX() && block.getGridY() == towerPanel.getGridY()) {
                        selectedBlock = block;
                        break;

                    }
                }
            }
        }
    }

    /**
     * adjust the enemy hp and speed based on difficuly choosed
     */
    private void adjustDifficulty() {
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
        if (waveNum >= 15) {
            for (int i = 0; i < 7; i++) {
                MainFrame.towerSpeed[i] *= 1.5;
                MainFrame.enemyHPs[i] /= 2;
                MainFrame.enemySpeeds[i] *= 2;
            }
        }
    }

    private void excutePanelOperations() {

        if (towerPanel != null) { // if there is a panel

            if (towerPanel.getUpgradeButton().contains(mouseX, mouseY)) { // if upgrade button is clicked

                if (towerPanel.getType() != 1) { // for all towers

                    for (Tower tower : towers) {
                        if (tower.getGridX() == towerPanel.getGridX() && tower.getGridY() == towerPanel.getGridY()) {
                            if (cash >= MainFrame.towerCosts[towerPanel.getType() - 1] / 2 && tower.getLevel() < 5) {
                                // Upgrade the tower selected if cash is sufficient
                                tower.setLevel(tower.getLevel() + 1);
                                cash -= MainFrame.towerCosts[towerPanel.getType() - 1] / 2;
                                tower.upgrade(1);
                            }
                            else{
                                MainFrame.error.play();
                            }
                            break;
                        }
                    }
                }
                // close the panel
                towerPanel = null;
                panelOperation = true;
            } else if (towerPanel.getSellButton().contains(mouseX, mouseY)) { // if sell button is clicked

                if (towerPanel.getType() == 1) { // Remove the block, update enemy path, and re-gain cash

                    ArrayList<Block> tempBlocks = new ArrayList<Block>(blocks);
                    for (Block block : tempBlocks) {
                        if (block.getGridX() == towerPanel.getGridX() && block.getGridY() == towerPanel.getGridY()) {
                            blocks.remove(block);
                            cash += MainFrame.towerCosts[0];
                            towerGrid[towerPanel.getGridY()][towerPanel.getGridX()] = 0;
                            findPath();
                            break;
                        }
                    }
                } else { // remove the tower, and left an empty block

                    ArrayList<Tower> tempTowers = new ArrayList<Tower>(towers);
                    for (Tower tower : tempTowers) {
                        if (tower.getGridX() == towerPanel.getGridX() && tower.getGridY() == towerPanel.getGridY()) {
                            towers.remove(tower);
                            cash += MainFrame.towerCosts[towerPanel.getType() - 1] * (1 + tower.getLevel() / 2.0);
                            towerGrid[towerPanel.getGridY()][towerPanel.getGridX()] = 1;
                            break;
                        }
                    }
                }
                // close the panel
                towerPanel = null;
                panelOperation = true;
            }
        }
    }

    private void addBlock() {
        if (towerGrid[gridY][gridX] == 0) {
            towerPanel = null;
            if (cash >= MainFrame.towerCosts[0]) {

                pathGrid = new char[row][col];

                towerGrid[gridY][gridX] = 1;
                if (findPath()
                        && (gridY != row / 2 || gridX != col / 2 + 1)) {

                    cash -= MainFrame.towerCosts[0];
                    Block block = new Block(gridX, gridY);
                    blocks.add(block);
                } else {
                    MainFrame.error.play();
                    towerGrid[gridY][gridX] = 0;
                    findPath();
                }
            }
            else{
                MainFrame.error.play();
            }
        }
    }

    private void addTower() {
        // towerPanel = null;
        if (towerGrid[gridY][gridX] == 0) {
            MainFrame.error.play();
            towerPanel = null;

        } else if (towerGrid[gridY][gridX] == 1) {
            towerPanel = null;
            if (cash >= MainFrame.towerCosts[selectNum - 1]) {

                cash -= MainFrame.towerCosts[selectNum - 1];
                towerGrid[gridY][gridX] = selectNum;
                Tower tower;
                if (selectNum == 5) {
                    tower = new PenetrateTower(gridX, gridY, selectNum - 1);
                } else if (selectNum == 6) {
                    tower = new MissileTower(gridX, gridY, selectNum - 1);

                } else if (selectNum == 7) {
                    tower = new BoomTower(gridX, gridY, selectNum - 1);

                } else if (selectNum == 8) {
                    tower = new RingTower(gridX, gridY, selectNum - 1);
                } else {
                    tower = new Tower(gridX, gridY, selectNum - 1);
                }
                towers.add(tower);
            } else {
                MainFrame.error.play();
            }
        }

    }

    private void updatePanel() {
        if (towerPanel == null) {
            towerPanel = new TowerPanel(towerGrid[gridY][gridX], gridX, gridY);

        } else {
            if (gridX == towerPanel.getGridX() && gridY == towerPanel.getGridY()) {
                towerPanel = null;

            } else {
                towerPanel.update(towerGrid[gridY][gridX], gridX, gridY);
            }

        }
    }

    private void getSelectedTower() {

        // Select by mouse click
        for (TowerIcon icon : towerIcons) {
            if (icon.contains(mouseX, mouseY)) {
                // remove the highlignht of the old selected icon
                for (TowerIcon i : towerIcons) {
                    i.setSelect(false);
                }
                // mark the selected icon
                icon.setSelect(true);
                selectNum = icon.getType();
            }
        }

        // select by keyboard input, coop with keyadaptor;
        for (int i = 0; i < towerIcons.size(); i++) {

            if (i + 1 == selectNum) {
                towerIcons.get(i).setSelect(true);
            } else {
                towerIcons.get(i).setSelect(false);
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
            if (bullet.getExistTime() * bullet.getSpeed() > (bullet.getParent().getRange() + 1) * blockSize) {
                if (bullet.getExplodeRadius() > 0) {
                    bullet.explode();
                } else {
                    bullets.remove(bullet);
                }
            }
            if (bullet.getExplodeTime() >= 0) {
                bullet.setExplodeTime(bullet.getExplodeTime() + 1);
            }
            if (bullet.getExplodeTime() > 16) {
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
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 2));
                    } else if (waveNum >= 15 && waveNum < 20) {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 3));
                    } else if (waveNum >= 20 && waveNum < 25) {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 5));
                    } else {
                        enemy = new Enemy(elementNum - 9, 1.0 * (1 + (waveNum + 1) * (waveNum + 1) * 6));
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
            waveEnd = new WaveEnd();
        }
    }

    private void excuteTowerOperation() {
        for (Tower tower : towers) {
            tower.aim();
            if (time % tower.getFreq() == 0) {
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
                if (bullet.intersects(enemy) && bullet.getExplodeTime() < 0) {
                    enemy.setHp(enemy.getHp() - bullet.getDamage());

                    if (!bullet.isPenetrate()) {
                        if (bullet.getExplodeRadius() > 0) {
                            bullet.setSpeedX(0);
                            bullet.setSpeedY(0);
                            bullet.explode();
                            bullet.getParent().setTarget(enemy);
                        } else {
                            bullets.remove(bullet);
                        }
                    }
                }
            }
            if (enemy.getHp() <= 0) {
                enemy.die();
                enemys.remove(enemy);
                cash += enemy.getType() * 10;
                score += enemy.getType() * 20 * scoreRate;

            }
        }
    }

    private void endGame() {
        MainFrame.bgm.stop();
        this.setVisible(false);
        new EndFrame();
        timer.stop();
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

            drawEnemy(gc);
            drawTower(gc);
            drawBullet(gc);

            drawTowerPanel(gc);
            if (waveEnd != null) {
                gc.drawImage(MainFrame.waveEndImage, waveEnd.x, waveEnd.y, waveEnd.width, waveEnd.height, null);
            }

        }

        public void drawGrid(Graphics2D gc) {

            // Draw the center grid
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

            if (!edit) {
                gc.drawImage(MainFrame.startImage, (col / 2 - 1) * blockSize + leftMargin,
                        row / 2 * blockSize + topMargin, blockSize,
                        blockSize, null);
            } else {
                gc.fillRect((col / 2 - 1) * blockSize + leftMargin, row / 2 * blockSize + topMargin, blockSize,
                        blockSize);
            }

            gc.drawImage(MainFrame.endImage, (col / 2 + 1) * blockSize + leftMargin, row / 2 * blockSize + topMargin,
                    blockSize,
                    blockSize, null);
            gc.setColor(Color.BLACK);

            // Draw top panel
            gc.setFont(new Font("Times New Roman", Font.PLAIN, 30));
            gc.drawString("Player: " + userID, 100, titleHeight / 3 * 2);
            if (edit) {
                gc.drawString("Edit Mode", 500, titleHeight / 3 * 2);
            } else {
                gc.drawString("Play Mode", 500, titleHeight / 3 * 2);
            }

            gc.drawString("Wave: " + String.valueOf(waveNum + 1), 700, titleHeight / 3 * 2);
            gc.drawString("Score: " + String.valueOf(score), 900, titleHeight / 3 * 2);

            // Draw Buttom Panel

            for (TowerIcon icon : towerIcons) {
                gc.setColor(Color.WHITE);
                if (icon.isSelect()) {
                    gc.fillRect(icon.x - 2, icon.y - 2, icon.width + 4, icon.height + 4);
                }
                // gc.setColor(Color.BLACK);
                // gc.fillRect(icon.x, icon.y, icon.width, icon.height);
                gc.drawImage(icon.getIcon(), icon.x, icon.y, icon.width, icon.height, null);

                gc.setColor(Color.BLACK);
                gc.drawString(icon.getText(), icon.x, (int) (icon.y + blockSize * 2.5));
            }
            gc.drawString("Cash: " + String.valueOf(cash), panelWidth / 10 * 9, buttomY + buttomHeight / 3);
            gc.drawString("Life: " + String.valueOf(playerHP), panelWidth / 10 * 9, buttomY + buttomHeight / 3 * 2);

        }

        public void drawTower(Graphics2D gc) {
            for (Block block : blocks) {
                gc.drawImage(block.getImage(), block.x, block.y, block.width, block.height, null);

            }
            for (Tower tower : towers) {
                int cx = tower.x + blockSize / 2;
                int cy = tower.y + blockSize / 2;
                AffineTransform Towertransform = new AffineTransform();
                Towertransform.translate(cx, cy);
                Towertransform.rotate(Math.toRadians(tower.getAngle()));
                Towertransform.translate(-blockSize / 2, -blockSize / 2);
                Towertransform.scale(blockSize / 1500.0, blockSize / 1500.0);
                gc.drawImage(tower.getImage(), Towertransform, null);

                gc.setColor(Color.YELLOW);
                // gc.fillRect(tower.x,tower.y,blockSize/5,blockSize/5);
                for (int i = 0; i < tower.getLevel(); i++) {
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
                if (bullet.getExplodeRadius() > 0 && bullet.getExplodeTime() >= 0) {
                    // gc.drawImage(MainFrame.explodeImage, bullet.x - bullet.explodeRadius / 2,
                    // bullet.y - bullet.explodeRadius / 2,
                    // bullet.x + bullet.explodeRadius / 2, bullet.y + bullet.explodeRadius / 2,
                    // bullet.explodeTime * 64, 192, (bullet.explodeTime + 1) * 64, 256, null);
                    gc.drawImage(MainFrame.explodeImage, bullet.x - bullet.getExplodeRadius() / 2,
                            bullet.y - bullet.getExplodeRadius() / 2,
                            bullet.x + bullet.getExplodeRadius() / 2, bullet.y + bullet.getExplodeRadius() / 2,
                            bullet.getExplodeTime() * 64, 256, (bullet.getExplodeTime() + 1) * 64, 320, null);
                }
                // Draw Missle with correct direction
                if (bullet.getExplodeTime() < 0 && bullet.getTransform() != null) {

                    gc.drawImage(bullet.getImage(), bullet.getTransform(), null);
                    // gc.fillRect(bullet.x, bullet.y, bullet.size, bullet.size);
                }

            }
        }

        public void drawEnemy(Graphics2D gc) {
            for (Enemy enemy : enemys) {
                if (enemy.getType() == 0) {
                    // if (!(enemy.gridX ==col/2-1 && enemy.gridY==row/2)) {
                    gc.drawImage(enemy.getImage(), enemy.x, enemy.y, blockSize, blockSize, null);
                    // }
                } else {
                    int cx = enemy.x + blockSize / 2;
                    int cy = enemy.y + blockSize / 2;
                    AffineTransform Enemytransform = new AffineTransform();

                    Enemytransform.translate(cx, cy);
                    Enemytransform.rotate(Math.toRadians(enemy.getAngle()));
                    Enemytransform.translate(-blockSize / 2, -blockSize / 2);
                    Enemytransform.scale(blockSize / 348.0, blockSize / 348.0);
                    gc.drawImage(enemy.getImage(), Enemytransform, null);
                    enemy.drawHP(gc);
                }
            }
        }

        public void drawTowerPanel(Graphics2D gc) {
            if (towerPanel != null) {
                gc.setFont(new Font("Times New Roman", Font.PLAIN, 20));
                if (selectedTower != null) {
                    gc.setColor(Color.BLACK);
                    gc.drawOval(selectedTower.x - selectedTower.getRange() * blockSize + blockSize / 2,
                            selectedTower.y - selectedTower.getRange() * blockSize + blockSize / 2,
                            selectedTower.getRange() * blockSize * 2, selectedTower.getRange() * blockSize * 2);
                    gc.setColor(transparentOrange);
                    gc.fillOval(selectedTower.x - selectedTower.getRange() * blockSize + blockSize / 2,
                            selectedTower.y - selectedTower.getRange() * blockSize + blockSize / 2,
                            selectedTower.getRange() * blockSize * 2, selectedTower.getRange() * blockSize * 2);
                    gc.setColor(towerPanel.getColor());
                    gc.fillRect(towerPanel.x, towerPanel.y, towerPanel.width, towerPanel.height);
                    gc.setColor(towerPanel.getUpgradeButton().getColor());
                    gc.fillRect(towerPanel.getUpgradeButton().x, towerPanel.getUpgradeButton().y, towerPanel.getUpgradeButton().width,
                            towerPanel.getUpgradeButton().height);
                    gc.setColor(towerPanel.getSellButton().getColor());
                    gc.fillRect(towerPanel.getSellButton().x, towerPanel.getSellButton().y, towerPanel.getSellButton().width,
                            towerPanel.getSellButton().height);
                    gc.setColor(Color.BLACK);
                    gc.drawString("lv " + String.valueOf(selectedTower.getLevel()),
                            towerPanel.x,
                            towerPanel.y + blockSize * 2 / 3);
                    gc.drawString("↑ - " + String.valueOf(MainFrame.towerCosts[towerPanel.getType() - 1] / 2),
                            towerPanel.getUpgradeButton().x,
                            towerPanel.getUpgradeButton().y + blockSize * 2 / 3);
                    gc.drawString(
                            "x + " + String.valueOf(
                                    (int) (MainFrame.towerCosts[towerPanel.getType() - 1]
                                            * (1 + selectedTower.getLevel() / 2.0))),
                            towerPanel.getSellButton().x,
                            towerPanel.getSellButton().y + blockSize * 2 / 3);
                }
                if (selectedBlock != null) {
                    gc.setColor(towerPanel.getColor());
                    gc.fillRect(towerPanel.x, towerPanel.y, towerPanel.width, towerPanel.height);
                    gc.setColor(towerPanel.getSellButton().getColor());
                    gc.fillRect(towerPanel.getSellButton().x, towerPanel.getSellButton().y, towerPanel.getSellButton().width,
                            towerPanel.getSellButton().height);
                    gc.setColor(Color.BLACK);
                    gc.drawString("Cannot",
                            towerPanel.x,
                            towerPanel.y + blockSize * 2 / 3);
                    gc.drawString("Upgrade",
                            towerPanel.getUpgradeButton().x,
                            towerPanel.getUpgradeButton().y + blockSize * 2 / 3);
                    gc.drawString(
                            "x + " + String
                                    .valueOf(MainFrame.towerCosts[0]),
                            towerPanel.getSellButton().x,
                            towerPanel.getSellButton().y + blockSize * 2 / 3);

                }

            }
        }
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
            MainFrame.error.play();
            System.out.println("cannot find file");
        }
    }

    public void saveGame() {
        try {
            String fileName = "scripts/Progress/" + userID + "progress.txt";
            File progressFile = new File(fileName);
            if (!progressFile.exists()) {
                progressFile.createNewFile();
            }
            FileWriter out = new FileWriter(progressFile, false);
            BufferedWriter writer = new BufferedWriter(out);
            String[][] record = new String[row][col];

            for (Block block : blocks) {
                record[block.getGridY()][block.getGridX()] = "1";
            }
            for (Tower tower : towers) {
                record[tower.getGridY()][tower.getGridX()] = String.valueOf(tower.getLevel()) + String.valueOf(tower.getType() + 1);

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
            writer.close();
            out.close();

            File rankingFile = new File("scripts/ranking.txt");
            FileReader in = new FileReader(rankingFile);
            String[] users = new String[1000];
            int[] scores = new int[1000];
            BufferedReader reader = new BufferedReader(in);
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(" ");
                users[index] = info[0];
                scores[index] = Integer.parseInt(info[1]);
                index++;
            }
            reader.close();
            in.close();
            boolean change = false;
            for (int i = 0; i < index; i++) {
                if (users[i].equals(userID)) {
                    change = true;
                    if (score > scores[i]) {
                        scores[i] = score;
                    }
                    break;
                }
            }
            out = new FileWriter(rankingFile, false);
            writer = new BufferedWriter(out);
            for (int i = 0; i < index; i++) {
                String output = users[i] + " " + String.valueOf(scores[i]);
                writer.write(output);
                writer.newLine();
            }
            if (!change) {
                String output = userID + " " + String.valueOf(score);
                writer.write(output);
                writer.newLine();
            }
            writer.close();
            out.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void loadGame() {

        try {
            String fileName = "scripts/Progress/" + MainFrame.userID + "progress.txt";

            File progressFile = new File(fileName);
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
                        Block block = new Block(x, y);
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
                            tower = new MissileTower(x, y, itemNum % 10 - 1);

                        } else if (itemNum % 10 == 7) {
                            tower = new BoomTower(x, y, itemNum % 10 - 1);

                        } else if (itemNum % 10 == 8) {
                            tower = new RingTower(x, y, itemNum % 10 - 1);
                        } else {
                            tower = new Tower(x, y, itemNum % 10 - 1);
                        }
                        int level = itemNum / 10;
                        tower.setLevel(level);
                        tower.upgrade(level);
                        

                        towers.add(tower);
                    }

                }
            }
            findPath();
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
     * @return If there is a complete path from start to end
     */
    private boolean findPath() {
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        // Corresponding arrows for directions: right, down, left, up
        boolean[][] visited = new boolean[row][col];

        // Queue for BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { row / 2, col / 2 + 1 });

        visited[row / 2][col / 2 + 1] = true;
        pathGrid[row / 2][col / 2 + 1] = 'E'; // Mark the start point
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

                if (newX >= 0 && newX < row && newY >= 0 && newY < col && towerGrid[newX][newY] == 0
                        && !visited[newX][newY]) {
                    queue.add(new int[] { newX, newY });
                    visited[newX][newY] = true;
                    pathGrid[newX][newY] = arrows[i];
                }
            }
        }

        // If there is a complete path from start to end
        for (char direction : arrows) {
            if (pathGrid[row / 2][col / 2 - 1] == direction) {
                return true;
            }
        }
        return false;
    }

    class KeyInput extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // press escape to quit
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            // Press enter to start a wave
            if (e.getKeyCode() == KeyEvent.VK_ENTER && edit == true) {
                waveEnd = null;
                findPath();
                enemys.clear();
                enemyNum = 0;
                time = 0;
                edit = false;
                pointer = 0;
                waveNum++;
                if (waveNum == 15) {
                    // After wave 15, a wave is too long, so increse enemy speed and decrese their
                    // hp to speed up the game
                    for (int i = 0; i < 7; i++) {
                        MainFrame.towerSpeed[i] *= 1.5;
                        MainFrame.enemyHPs[i] /= 2;
                        MainFrame.enemySpeeds[i] *= 2;
                    }
                }
                if (waveNum == 30) {
                    endGame();
                }
                delay = 0;
                allOut = false;
                notSave = true;
            }
            // get tower based on key
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

    // get mouse input
    class MouseInput extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            mouseClick = true;
            mouseX = e.getX();
            mouseY = e.getY();

        }
    }
}
