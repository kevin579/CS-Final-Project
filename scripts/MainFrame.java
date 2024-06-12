import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

//This is the introduction page of the application
public class MainFrame extends JFrame implements ActionListener {
    JComboBox<String> comboBox;
    String[] difficulties = { "easy", "medium", "hard", "impossible" };
    String difficult = difficulties[0];
    JButton startButton, introButton, rankButton, loadButton;
    JPanel mainPanel;
    JTextField textField;
    static int panelWidth;
    static int panelHeight;
    TextLabel infoText1, infoText2, infoText3;
    static String userID = "anonymous";

    // The parameter of enemy, can be modified
    // including health, speed, bullet speed, dmg, attack rate, spawnspeed, and
    // score
    static int dif = 1;
    static BufferedImage pointerUp, pointerDown, pointerLeft, pointerRight;

    static int[] enemyHPs = { 8, 12, 40, 60, 100, 125, 25, 30 };
    static double[] enemySpeeds = { 1.2, 1.5, 1, 1, 0.8, 0.9, 1.8, 2 };

    static int[] towerCosts = { 10, 20, 50, 180, 200, 1000, 1500, 2000 };
    static int[] towerDamage = { 2, 4, 8, 20, 90, 200, 20 };
    static int[] towerRange = { 5, 6, 7, 5, 50, 8, 3 };
    static int[] towerSpeed = { 5, 6, 7, 5, 8, 10, 3 };

    static int[] towerFreq = { 15, 10, 5, 40, 50, 80, 15 };
    static int[] explodeRadius = new int[2];

    static ArrayList<BufferedImage> towerImages;
    static ArrayList<BufferedImage> enemyImages;
    static ArrayList<BufferedImage> bulletImages;
    static BufferedImage startImage;
    static BufferedImage endImage;
    static BufferedImage explodeImage;
    static BufferedImage waveEndImage;

    // sound variable
    static Audio bgm, error, lossHp, explode;
    static ArrayList<Audio> bulletAudios;

    public static void main(String[] args) {
        new MainFrame();
    }

    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("SetUp");

        // max w and h, makes the frame cover the whole screen
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(true);

        // esc to quit when VK_ESCAPE is detected
        // add global key dispatcher so after JCombobox is clicked, key will still be
        // listened
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
                return false;
            }
        });

        // This is the main panel that is borderlayout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        mainPanel.setBackground(Color.GRAY);

        // The title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(152, 251, 152));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));
        TextLabel titleText = new TextLabel(64, "Welcome to Tower Defense!", 0, 0, 0, 0);
        titlePanel.add(titleText);

        // The introduction pannel on how to play the game, Lead to intro frame for more
        // detail
        JPanel introductionPanel = new JPanel();
        introductionPanel.setBackground(new Color(252, 71, 71));
        introductionPanel.setLayout(new BoxLayout(introductionPanel, BoxLayout.PAGE_AXIS));
        introductionPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        introductionPanel.add(new TextLabel(30, "Introduction", 0, 10, 40, 0));
        introButton = new JButton("Click to see Introduction");
        introButton.setActionCommand("intro");
        introButton.addActionListener(this);
        introductionPanel.add(introButton);

        // The center panel, will let the player to choose difficulty and start the
        // game. Also displays difficulty information.
        JPanel enterPanel = new JPanel();
        JPanel textPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        textPanel.add(new TextLabel(36, "Select Difficulty", 0, 0, 0, 0));

        enterPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        enterPanel.setLayout(new GridLayout(10, 1));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // A text field for username input
        buttonPanel.add(new TextLabel(18, "Enter username", -3, 0, 0, 0));
        textField = new JTextField(10);
        buttonPanel.add(textField);

        // ComboBox for choose difficulty
        comboBox = new JComboBox<String>(difficulties);
        comboBox.addActionListener(this);
        buttonPanel.add(comboBox);

        // start button to start the game
        startButton = new JButton("newGame");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        buttonPanel.add(startButton);

        // load game button
        loadButton = new JButton("Load Game");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);
        buttonPanel.add(loadButton);

        infoText1 = new TextLabel(24, "Enemy hp x1", 0, 50, 0, 0);
        infoText2 = new TextLabel(24, "Enemy speed x1", 0, 50, 0, 0);
        infoText3 = new TextLabel(24, "Score magnification x1", 0, 50, 0, 0);
        enterPanel.add(textPanel);
        enterPanel.add(buttonPanel);
        enterPanel.add(infoText1);
        enterPanel.add(infoText2);
        enterPanel.add(infoText3);
        mainPanel.add(enterPanel);

        // Ranking panel, show the top players who played this game. Also empty and not
        // made.
        JPanel rankingPanel = new JPanel();
        rankingPanel.setBackground(new Color(204, 153, 255));
        rankingPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.PAGE_AXIS));
        rankingPanel.add(new TextLabel(30, "Ranking", 0, 20, 40, 0));
        rankButton = new JButton("Click to see ranking");
        rankButton.setActionCommand("ranking");
        rankButton.addActionListener(this);
        rankingPanel.add(rankButton);

        // The bottom panel, with almost information
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(152, 251, 152));
        JLabel contact = new JLabel("Contact Kevin: 339624579@gotvdsb.ca");
        bottomPanel.add(contact);

        // add all panels to the borderlayout panel.
        mainPanel.add(titlePanel, BorderLayout.PAGE_START);
        mainPanel.add(introductionPanel, BorderLayout.LINE_START);
        mainPanel.add(rankingPanel, BorderLayout.LINE_END);
        mainPanel.add(enterPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        // frame setup
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // make sure frame can receive key events
        this.setFocusable(true);
        this.requestFocusInWindow(true);

        initializeVariables();

    }

    /**
     * Get the size of the maximized window
     * Load all the images.
     * 
     */
    public void initializeVariables() {
        panelWidth = this.getWidth();
        panelHeight = this.getHeight();
        explodeRadius[0] = panelHeight / 8;
        explodeRadius[1] = panelHeight / 12;

        towerImages = new ArrayList<BufferedImage>();
        enemyImages = new ArrayList<BufferedImage>();
        bulletImages = new ArrayList<BufferedImage>();
        pointerUp = loadImage("Images/pointerUp.png");
        pointerDown = loadImage("Images/pointerDown.png");
        pointerLeft = loadImage("Images/pointerLeft.png");
        pointerRight = loadImage("Images/pointerRight.png");

        startImage = loadImage("Images/start.png");
        endImage = loadImage("Images/end.png");

        explodeImage = loadImage("Images/explosions.png");
        bulletImages.add(loadImage("Images/bullet.png"));
        bulletImages.add(loadImage("Images/boom.png"));
        bulletImages.add(loadImage("Images/missle.png"));

        enemyImages.add(loadImage("Images/enemy_2.png"));
        enemyImages.add(loadImage("Images/enemy_3.png"));
        enemyImages.add(loadImage("Images/enemy_4.png"));
        enemyImages.add(loadImage("Images/enemy_5.png"));
        enemyImages.add(loadImage("Images/enemy_6.png"));
        enemyImages.add(loadImage("Images/enemy_7.png"));
        enemyImages.add(loadImage("Images/enemy_8.png"));
        enemyImages.add(loadImage("Images/enemy_9.png"));
        enemyImages.add(loadImage("Images/enemy_10.png"));

        towerImages.add(loadImage("Images/block.png"));
        towerImages.add(loadImage("Images/tower_1.png"));
        towerImages.add(loadImage("Images/tower_2.png"));
        towerImages.add(loadImage("Images/tower_3.png"));
        towerImages.add(loadImage("Images/tower_4.png"));
        towerImages.add(loadImage("Images/tower_5.png"));
        towerImages.add(loadImage("Images/tower_6.png"));
        towerImages.add(loadImage("Images/tower_7.png"));

        waveEndImage = loadImage("Images/waveEnd.png");

        bgm = new Audio("Audios/bgm.wav", 0.9f);
        error = new Audio("Audios/error.wav", 1);
        lossHp = new Audio("Audios/lossHp.wav", 0.9f);
        explode = new Audio("Audios/explode.wav", 1);
        bulletAudios = new ArrayList<Audio>();
        bulletAudios.add(new Audio("Audios/shoot.wav", 0.8f));
        bulletAudios.add(new Audio("Audios/shoot.wav", 0.8f));
        bulletAudios.add(new Audio("Audios/shoot.wav", 0.8f));
        bulletAudios.add(new Audio("Audios/penetrateShoot.wav", 0.75f));
        bulletAudios.add(new Audio("Audios/missleShoot.wav", 0.8f));
        bulletAudios.add(new Audio("Audios/cannonShoot.wav", 0.8f));
        bulletAudios.add(new Audio("Audios/shoot.wav", 0.8f));
    }

    // When the player chooses a diffucult or starts the game
    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();

        // get the userName
        userID = textField.getText().toLowerCase();
        if (userID.equals("")) {
            userID = "anonymous";
        }
        // Starts a new game based on the difficult choosed when start button is
        // pressed.
        if (eventName.equals("start")) {

            JOptionPane.showMessageDialog(null,
                    "Are you sure you want to start new Game? Your previous progress will be coverd. Press esc to cancel ",
                    "Warning",
                    JOptionPane.NO_OPTION);

            this.setVisible(false);

            // Create the game frame which is new
            new GameFrame(false);

        }
        // Load the game when load is pressed
        else if (eventName.equals("load")) {

            // Check if the userName have a progress file
            String fileName = "Progress/" + userID + "progress.txt";
            File file = new File(fileName);
            if (!file.exists()) {// Tell the user file not exits
                JOptionPane.showMessageDialog(null, "No such Archive, please recheck user name", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // continues the game.
            this.setVisible(false);
            new GameFrame(true);
        }
        // Brings the user to the intro frame
        else if (eventName.equals("intro")) {
            this.setVisible(false);
            new IntroFrame();
        }
        // Brings the user to ranking frame
        else if (eventName.equals("ranking")) {
            this.setVisible(false);
            new RankingFrame();
        }
        // If the combo box is clicked and choosed difficulty
        else {
            @SuppressWarnings("unchecked") // for comboBox
            JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
            // Get and set difficulty
            difficult = (String) comboBox.getSelectedItem();
            if (difficult.equals("easy")) {
                infoText1.setText("Enemy hp x1");
                infoText2.setText("Enemy speed x1");
                dif = 1;
                infoText3.setText("Score magnification x1");
            } else if (difficult.equals("medium")) {
                infoText1.setText("Enemy hp x1.25");
                infoText2.setText("Enemy speed x1.25");
                infoText3.setText("Score magnification x1.5");
                dif = 2;
            } else if (difficult.equals("hard")) {
                infoText1.setText("Enemy hp x1.5");
                infoText2.setText("Enemy speed x1.5");
                infoText3.setText("Score magnification x2");
                dif = 3;
            } else if (difficult.equals("impossible")) {
                infoText1.setText("Enemy hp x2");
                infoText2.setText("Enemy speed x2");
                infoText3.setText("Score magnification x5");
                dif = 4;
            }
        }
    }

    /**
     * Load the image
     * 
     * @param filename The file name of the image
     * @return The buffered image
     */
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

    // Modify the JLabel text for convinience
    private class TextLabel extends JLabel {
        TextLabel(int size, String text, int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
            super(text, SwingConstants.CENTER);
            this.setFont(new Font("Serif", Font.PLAIN, size));
            this.setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));
        }
    }

}