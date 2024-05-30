import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Queue;

import javax.swing.Timer;

public class GameFrame extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new GameFrame();
    }

    GamePanel gamePanel;
    Timer timer;
    int time = 0;
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

    // variables for block and tower
    static ArrayList<Block> blocks;
    static ArrayList<Tower> towers;
    static ArrayList<Enemy> enemys;
    static ArrayList<Bullet> bullets;
    static ArrayList<TowerIcon> towerIcons;
    static ArrayList<BufferedImage> towerImages;
    static ArrayList<BufferedImage> enemyImages;
    static int selectNum = 0;

    //wave variables
    static char[][] wave = new char[100][100];
    static int waveNum = 0;


    //Game variables
    static int playerHP = 20;
    int cash = 100;



    GameFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(true);
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

        TowerIcon block = new TowerIcon(1, MainFrame.costs[0]);
        TowerIcon t1 = new TowerIcon(2, MainFrame.costs[1]);
        TowerIcon t2 = new TowerIcon(3, MainFrame.costs[2]);
        TowerIcon t3 = new TowerIcon(4, MainFrame.costs[3]);
        TowerIcon t4 = new TowerIcon(5, MainFrame.costs[4]);
        TowerIcon t5 = new TowerIcon(6, MainFrame.costs[5]);
        TowerIcon t6 = new TowerIcon(7, MainFrame.costs[6]);

        towerIcons.add(block);
        towerIcons.add(t1);
        towerIcons.add(t2);
        towerIcons.add(t3);
        towerIcons.add(t4);
        towerIcons.add(t5);
        towerIcons.add(t6);
        

        findPath(towerGrid, row / 2, col / 2 + 1, pathGrid);
        loadWave();
        System.out.println(wave[0][1]);

        // Start the timer
        timer = new Timer(16, this);
        timer.setInitialDelay(1000);
        timer.start();

    }

    public void actionPerformed(ActionEvent e) {
        if (time % 1000 == 0) {
            Enemy enemy = new Enemy(1,1);
            enemys.add(enemy);
        }
        if (edit) {
            if (mouseClick) {
                if (mouseX > leftMargin && mouseX < panelWidth - rightMargin && mouseY > titleHeight
                        && mouseY < buttomY) {
                    int gridX = (mouseX - leftMargin) / blockSize;
                    int gridY = (mouseY - topMargin) / blockSize;
                    if (selectNum == 1 && cash>=MainFrame.costs[0]) {
                        for (int i = 0; i < pathGrid.length; i++) {
                            for (int j = 0; j < pathGrid[0].length; j++) {
                                pathGrid[i][j] = '+';
                            }
                        }

                        towerGrid[gridY][gridX] = 1;
                        if (findPath(towerGrid, row / 2, col / 2 + 1, pathGrid)) {
                            // for (int i = 0; i < pathGrid.length; i++) {
                            //     for (int j = 0; j < pathGrid[0].length; j++) {
                            //         System.out.print(pathGrid[i][j] + " ");
                            //     }
                            //     System.out.println();
                            // }
                            Block block = new Block(gridX, gridY, 10);
                            blocks.add(block);
                            cash -= MainFrame.costs[0];
                        } else {
                            System.out.println("Not avaliabe");
                            towerGrid[gridY][gridX] = 0;
                        }
                    } else if (selectNum > 1) {
                        if (cash < MainFrame.costs[selectNum - 1]) {
                            System.out.println("Not enough money");
                        } else if (towerGrid[gridY][gridX] == 0) {
                            System.out.println("Requires block");
                        } else if (towerGrid[gridY][gridX] > 1) {
                            System.out.println("Already exist tower");
                        } else {
                            cash -= MainFrame.costs[selectNum - 1];
                            towerGrid[gridY][gridX] = selectNum;
                            Tower tower = new Tower(gridX, gridY, selectNum - 1);
                            towers.add(tower);
                        }
                    }
                } else if (mouseY > buttomY) {
                    for (TowerIcon icon : towerIcons) {
                        if (icon.contains(mouseX, mouseY)) {
                            // remove the mark of the old selected icon
                            for (TowerIcon i : towerIcons) {
                                i.select = false;
                            }
                            // mark the selected icon
                            icon.select = true;
                            selectNum = icon.type;
                        }
                    }
                }
            }
            for (int i = 0; i < towerIcons.size(); i++) {

                if (i + 1 == selectNum) {
                    towerIcons.get(i).select = true;
                } else {
                    towerIcons.get(i).select = false;
                }
            }
        }
        if (!edit) {
            for (Tower tower:towers){
                tower.aim();
                if (time%tower.freq==0){
                    tower.shoot();
                }
            }
            
            ArrayList<Enemy> tempEnemys = new ArrayList<Enemy>(enemys);
            ArrayList<Bullet> tempBullets = new ArrayList<Bullet>(bullets);
            for (Enemy enemy : tempEnemys) {
                enemy.move();
                if (enemy.hp<=0){
                    enemys.remove(enemy);
                }
                for (Bullet bullet: tempBullets){
                    bullet.move();
                    if(bullet.intersects(enemy)){
                        enemy.hp-=bullet.damage;
                        bullets.remove(bullet);
                    }
                }
            }
            
            
        }
        time++;
        gamePanel.repaint();
        mouseClick = false;
        mouseX = mouseY = 0;

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

    private class GamePanel extends JPanel {

        GamePanel() {
            this.setPreferredSize(new Dimension(panelWidth, panelHeight));
            this.setBackground(Color.WHITE);

        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D gc = (Graphics2D) g;
            gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw Grid
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
            gc.setColor(Color.RED);
            gc.fillRect((col / 2 - 1) * blockSize + leftMargin, row / 2 * blockSize + topMargin, blockSize, blockSize);
            gc.setColor(Color.BLUE);
            gc.fillRect((col / 2 + 1) * blockSize + leftMargin, row / 2 * blockSize + topMargin, blockSize, blockSize);

            // Draw Buttom Panel
            gc.setFont(new Font("Times New Roman", Font.PLAIN, 30));
            for (TowerIcon icon : towerIcons) {
                gc.setColor(Color.WHITE);
                if (icon.select) {
                    gc.fillRect(icon.x - 2, icon.y - 2, icon.width + 4, icon.height + 4);
                }
                // gc.setColor(Color.BLACK);
                // gc.fillRect(icon.x, icon.y, icon.width, icon.height);
                gc.drawImage(icon.icon,icon.x,icon.y,icon.width,icon.height,null);
                
                gc.setColor(Color.BLACK);
                gc.drawString(icon.text, icon.x, (int) (icon.y + blockSize * 2.5));
            }
            gc.drawString("Cash: " + String.valueOf(cash), panelWidth / 10 * 9, buttomY + buttomHeight / 3);
            gc.drawString("Life: " + String.valueOf(playerHP), panelWidth / 10 * 9, buttomY + buttomHeight / 3*2);

            // Draw Blocks and towers
            gc.setColor(Color.BLACK);
            for (Bullet bullet: bullets){
                gc.fillRect(bullet.x,bullet.y,bullet.width,bullet.height);
            }

            for (Block block : blocks) {
                // gc.fillRect(block.x, block.y, block.width, block.height);
                gc.drawImage(block.image,block.x, block.y, block.width, block.height,null);

            }
            
            
            for (Tower tower : towers) {
                
                int cx = tower.x + blockSize / 2;
                int cy = tower.y + blockSize / 2;
                AffineTransform Towertransform = new AffineTransform();
                Towertransform.translate(cx, cy);
                Towertransform.rotate(Math.toRadians(tower.angle));
                Towertransform.translate(-blockSize / 2, -blockSize / 2);
                Towertransform.scale(blockSize/1500.0, blockSize/1500.0);
                gc.drawImage(tower.image, Towertransform, null);

            }
            

            // Draw Enemies
            for (Enemy enemy : enemys) {
                
                
                int cx = enemy.x + blockSize / 2;
                int cy = enemy.y + blockSize / 2;
                AffineTransform Enemytransform = new AffineTransform();
                
                Enemytransform.translate(cx, cy);
                Enemytransform.rotate(Math.toRadians(enemy.angle));
                Enemytransform.translate(-blockSize / 2, -blockSize / 2);
                Enemytransform.scale(blockSize/348.0, blockSize/348.0);
                gc.drawImage(enemy.image, Enemytransform, null);
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

    static void loadWave(){
        try{
            File file = new File("scripts/wave.txt");
            FileReader in = new FileReader(file);
            BufferedReader fileReader = new BufferedReader(in);
            String text;
            int w = 0;
            while ( (text = fileReader.readLine())!=null){
                wave[w] = text.toCharArray();
                w++;
            }
            fileReader.close();
            in.close();
        }catch(Exception e){
            System.out.println("cannot find file");
        }
    }
}

class KeyInput extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameFrame.edit = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            GameFrame.selectNum = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            GameFrame.selectNum = 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            GameFrame.selectNum = 3;
        }
        if (e.getKeyCode() == KeyEvent.VK_4) {
            GameFrame.selectNum = 4;
        }
        if (e.getKeyCode() == KeyEvent.VK_5) {
            GameFrame.selectNum = 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_6) {
            GameFrame.selectNum = 6;
        }
        if (e.getKeyCode() == KeyEvent.VK_7) {
            GameFrame.selectNum = 7;
        }
        if (e.getKeyCode() == KeyEvent.VK_8) {
            GameFrame.selectNum = 8;
        }
        if (e.getKeyCode() == KeyEvent.VK_9) {
            GameFrame.selectNum = 9;
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
