import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//This is the introduction page of the application
public class MainFrame extends JFrame implements ActionListener {
    JComboBox<String> comboBox;
    String[] difficulties = { "easy", "medium", "hard", "impossible" };
    String difficult = difficulties[0];
    JButton startButton;
    JPanel mainPanel;
    static int panelWidth = 1500;
    static int panelHeight = 800;
    TextLabel infoText1, infoText2, infoText3, infoText4, infoText5, infoText6, infoText7;

    // The parameter of enemy, can be modified
    static int enemyHP = 5;
    static double enemySpeed = 1;
    static double enemyBulletSpeed = 5;
    static int enemyDamage = 1;
    static int enemyAttackRate = 50;
    static int enemyGenerateSpeed = 500;
    static int scoreRate = 1;

    public static void main(String[] args) {
        new MainFrame();
    }

    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("SetUp");

        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(true);

        // This is the main panel that is borderlayout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        mainPanel.setBackground(Color.GRAY);

        // The title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(135, 206, 250));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));
        TextLabel titleText = new TextLabel(64, "Welcome to Space Shooting", 0, 0, 0, 0);
        titlePanel.add(titleText);

        // The introduction pannel on how to play the game, is empty and not be added
        // content in this assignment.
        PurplePanel introductionPanel = new PurplePanel();
        introductionPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        TextLabel introText = new TextLabel(36, "Click to see introduction", 0, 0, 0, 0);
        introductionPanel.add(introText);

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
        TextLabel chooseDifficultText = new TextLabel(36, "Choose your difficult", 0, 0, 0, 0);
        textPanel.add(chooseDifficultText);
        buttonPanel.add(comboBox);

        //
        startButton = new JButton("start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
        buttonPanel.add(startButton);
        infoText1 = new TextLabel(24, "Enemy hp x1", 0, 50, 0, 0);
        infoText2 = new TextLabel(24, "Enemy damage x1", 0, 50, 0, 0);
        infoText3 = new TextLabel(24, "Enemy speed x1", 0, 50, 0, 0);
        infoText4 = new TextLabel(24, "Enemy attack speed x1", 0, 50, 0, 0);
        infoText5 = new TextLabel(24, "Enemy bullet speed x1", 0, 50, 0, 0);
        infoText6 = new TextLabel(24, "Enemy generate speed x1", 0, 50, 0, 0);
        infoText7 = new TextLabel(24, "Score magnification x1", 0, 50, 0, 0);
        enterPanel.add(textPanel);
        enterPanel.add(buttonPanel);
        enterPanel.add(infoText1);
        enterPanel.add(infoText2);
        enterPanel.add(infoText3);
        enterPanel.add(infoText4);
        enterPanel.add(infoText5);
        enterPanel.add(infoText6);
        enterPanel.add(infoText7);
        mainPanel.add(enterPanel);

        // Ranking panel, show the top players who played this game. Also empty and not
        // made.
        PurplePanel rankingPanel = new PurplePanel();
        rankingPanel.setBackground(new Color(230, 230, 250));
        rankingPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 10, 10));
        TextLabel rankingText = new TextLabel(36, "Click to see ranking", 0, 0, 0, 0);
        rankingPanel.add(rankingText);

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

    }

    // When the player chooses a diffucult or starts the game
    public void actionPerformed(ActionEvent event) {
        String eventName = event.getActionCommand();
        if (eventName.equals("start")) {
            // Starts the game based on the difficult choosed when start button is pressed.
            // Create a GameFrame class.
            // this.setVisible(false);
            // if (difficult.equals("easy"))
            // new GameFrame(panelWidth, panelHeight, enemyHP, enemySpeed, enemyBulletSpeed,
            // enemyAttackRate,
            // enemyDamage, enemyGenerateSpeed, scoreRate);
            // else if (difficult.equals("medium"))
            // new GameFrame(panelWidth, panelHeight, enemyHP * 2, enemySpeed * 1.2,
            // enemyBulletSpeed * 1.2,
            // (int) (enemyAttackRate / 1.5), enemyDamage * 2, (int) (enemyGenerateSpeed /
            // 1.2),
            // scoreRate * 3);
            // else if (difficult.equals("hard"))
            // new GameFrame(panelWidth, panelHeight, enemyHP * 3, enemySpeed * 1.5,
            // enemyBulletSpeed * 1.5,
            // (int) (enemyAttackRate / 2), enemyDamage * 3, (int) (enemyGenerateSpeed /
            // 1.5), scoreRate * 5);
            // else if (difficult.equals("impossible"))
            // new GameFrame(panelWidth, panelHeight, enemyHP * 5, enemySpeed * 2,
            // enemyBulletSpeed * 1.5,
            // (int) (enemyAttackRate / 3), enemyDamage * 10000, (int) (enemyGenerateSpeed /
            // 1.5),
            // scoreRate * 10);
        } else { // when a difficult is choosed
            @SuppressWarnings("unchecked") // for comboBox
            JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
            difficult = (String) comboBox.getSelectedItem();
            if (difficult.equals("easy")) {
                infoText1.setText("Enemy hp x1");
                infoText2.setText("Enemy damage x1");
                infoText3.setText("Enemy speed x1");
                infoText4.setText("Enemy attack speed x1");
                infoText5.setText("Enemy bullet speed x1");
                infoText6.setText("Enemy generate speed x1");
                infoText7.setText("Score magnification x1");
            } else if (difficult.equals("medium")) {
                infoText1.setText("Enemy hp x2");
                infoText2.setText("Enemy damage x2");
                infoText3.setText("Enemy speed x1.2");
                infoText4.setText("Enemy attack speed x1.5");
                infoText5.setText("Enemy bullet speed x1.2");
                infoText6.setText("Enemy generate speed x1.2");
                infoText7.setText("Score magnification x3");
            } else if (difficult.equals("hard")) {
                infoText1.setText("Enemy hp x3");
                infoText2.setText("Enemy damage x3");
                infoText3.setText("Enemy speed x1.5");
                infoText4.setText("Enemy attack speed x2");
                infoText5.setText("Enemy bullet speed x1.5");
                infoText6.setText("Enemy generate speed x1.5");
                infoText7.setText("Score magnification x5");
            } else if (difficult.equals("impossible")) {
                infoText1.setText("Enemy hp x5");
                infoText2.setText("Enemy damage ∞ *!!One Hit Kill!!");
                infoText3.setText("Enemy speed x2");
                infoText4.setText("Enemy attack speed x5");
                infoText5.setText("Enemy bullet speed x2");
                infoText6.setText("Enemy generate speed x3");
                infoText7.setText("Score magnification x10");
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
