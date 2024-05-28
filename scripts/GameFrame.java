import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Queue;

import javax.swing.Timer;

public class GameFrame extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new GameFrame();
    }

    GamePanel gamePanel;
    Timer timer;
    static boolean edit = true;
    static int mouseX, mouseY;
    static boolean mouseClick = false;
    int panelWidth, panelHeight;
    int row;
    int col = 50;
    int[][] towerGrid;
    char[][] pathGrid;
    int blockSize;
    int titleHeight, buttomHeight, buttomY, gridHeight;
    int topMargin;
    int leftMargin;
    int rightMargin;

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
        // Start the timer
        timer = new Timer(16, this);
        timer.start();

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

    }

    public void actionPerformed(ActionEvent e) {

        gamePanel.repaint();

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
    private void findPath(int[][] grid, int x1, int y1, char[][] pathArray) {
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        // Corresponding arrows for directions: right, down, left, up

        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];

        // Queue for BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { x1, y1 });
        System.out.println(x1);
        System.out.println(y1);
        visited[x1][y1] = true;
        pathArray[x1][y1] = 'E'; // Mark the start point
        char[] arrows = { '<', '^', '>', 'v' };

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

            if (edit) {
                if (mouseClick) {
                    
                    int gridX = (mouseX-leftMargin)/blockSize;
                    int gridY = (mouseY-topMargin)/blockSize;
                    towerGrid[gridY][gridX] = 1;
                    findPath(towerGrid, row / 2, col / 2 + 1, pathGrid);
                    for (int i = 0; i < pathGrid.length; i++) {
                        for (int j = 0; j < pathGrid[0].length; j++) {
                            System.out.print(pathGrid[i][j] + " ");
                        }
                        System.out.println();
                    }
                    gc.setColor(Color.BLACK);
                    gc.fillRect(gridX*blockSize+leftMargin, gridY*blockSize+topMargin, blockSize, blockSize);
                    mouseClick = false;
                    mouseX = mouseY = 0;

                }
            }

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
    }
}

class MouseInput extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        System.out.printf("%d,%d", e.getX(), e.getY());
        GameFrame.mouseClick = true;
        GameFrame.mouseX = e.getX();
        GameFrame.mouseY = e.getY();

    }
}
