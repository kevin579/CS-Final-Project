import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    static String userID;

    // The parameter of enemy, can be modified
    // including health, speed, bullet speed, dmg, attack rate, spawnspeed, and
    // score
    static int dif = 1;


    static int[] enemyHPs = {8,12,40,60,100,150,25,30};
    static double[] enemySpeeds = {1.2,1.5,1,1,0.8,0.9,1.8,2};
    static int[] towerCosts = {10,20,50,180,200,1000,1500,2000};
    static int[] towerDamage = {2,4,8,3,40,70,5};
    static int[] towerRange = { 5, 6, 7, 5, 50, 8, 4};
    static int[] towerSpeed = {5,6,7,5,8,10,3};
    static int[] towerFreq = {15,10,5,20,20,40,15};
    static int[] explodeRadius = new int[2];

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
        TextLabel titleText = new TextLabel(64, "Welcome to Space Shooting", 0, 0, 0, 0);
        titlePanel.add(titleText);

        // The introduction pannel on how to play the game, is empty and not be added
        // content in this assignment.

        PurplePanel introductionPanel = new PurplePanel();
        introductionPanel.setBackground(new Color(252, 71, 71));
        introductionPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));

        introButton = new JButton("Click to see Introduction");
        introButton.setActionCommand("intro");
        introButton.addActionListener(this);
        introductionPanel.add(introButton);

        // Game rules
        /**
         * Simplified ver.
         * Move blocks and build machine gun towers to defend against alien invasion!
         * Your goal is to withstand all the waves to final victory.
         * 
         * In order to quit, click ESCAPE key
         * 
         * Good luck commander!
         * 
         * 
         */

        // The center panel, will let the player to choose difficulty and start the
        // game. Also displays difficulty information. Include button
        PurplePanel enterPanel = new PurplePanel();
        enterPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        enterPanel.setLayout(new GridLayout(10, 1));
        comboBox = new JComboBox<String>(difficulties);
        comboBox.addActionListener(this);
        PurplePanel textPanel = new PurplePanel();
        PurplePanel buttonPanel = new PurplePanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        textField = new JTextField(10);
        buttonPanel.add(textField);
        TextLabel chooseDifficultText = new TextLabel(36, "Select Difficulty", 0, 0, 0, 0);
        textPanel.add(chooseDifficultText);
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
        PurplePanel rankingPanel = new PurplePanel();
        rankingPanel.setBackground(new Color(204, 153, 255));
        rankingPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));

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

        panelWidth = this.getWidth();
        panelHeight = this.getHeight();
        explodeRadius[0] = panelHeight / 8;
        explodeRadius[1]  = panelHeight / 12;

    }

    // When the player chooses a diffucult or starts the game
    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        if (eventName.equals("start")) {
            // Starts the game based on the difficult choosed when start button is pressed.
            // Create a GameFrame class.
            userID = textField.getText();
            this.setVisible(false);
            new GameFrame(false);
            
        }else if (eventName.equals("load")) {
            // continues the game.
            this.setVisible(false);
            new GameFrame(true);
        }
        else if (eventName.equals("intro")) {
            this.setVisible(false);
            new IntroFrame();
        } else if (eventName.equals("ranking")) {
            this.setVisible(false);
            new RankingFrame();
        } else { // when a difficult is choosed
            @SuppressWarnings("unchecked") // for comboBox
            JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
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

    // Modify the JPanel for convinience
    private class PurplePanel extends JPanel {
        PurplePanel() {
            super();
            this.setBackground(new Color(230, 230, 250));
        }
    }

    // Modify the JLabel for convinience
    private class TextLabel extends JLabel {
        TextLabel(int size, String text, int topMargin, int leftMargin, int bottomMargin, int rightMargin) {
            super(text);
            this.setFont(new Font("Serif", Font.PLAIN, size));
            this.setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));
        }
    }
}