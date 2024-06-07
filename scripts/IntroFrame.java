import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class IntroFrame extends JFrame {
    public IntroFrame() {
        setTitle("General Game Rules");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // set up main panel, including backgroun color and broder
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(230, 230, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel for title, text body, and image in a single column
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(230, 230, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("General Rules of the Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 45));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(title);

        // add space between title and text body
        contentPanel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Text body
        JLabel textbody = new JLabel(
                "<html>BLOCKS - Press number key '1' on your keyboard, a block can be selected. It blocks enemy's path, and allows you to deploy defense towers. <br> When a deployed blocked is clicked, and there is not tower deployed on top, the block will removed and the cost will be refunded. <br> p.s. you cannot completely seal the path, towerbuilder will NOT respond to your command. <br><br> TOWERS - Keys 2-8 will select corresponding tower. Tower stats displays upon hovering your mouse on them. Similar to blocks, if clicked, <br> tower undeploys and the money refunds. <br> p.s. the towers can only be placed on top of a block. <br><br> GAMEPLAY - Drop-down menu can set the DIFFICULTY of the game. Press start button to start a new game. Once the game starts, the control <br> panel disappears and towers become non-editable. <br> <br> When a wave ends, all enemy will disappear. The total number of waves is dependent on the selcted difficulty. <br> <br> IMPORTANT - In order to quit, click ESC. </html>",
                SwingConstants.CENTER);
        textbody.setFont(new Font("Serif", Font.PLAIN, 20));
        textbody.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(textbody);

        // add some space between text body and image for improved readability
        contentPanel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Image label and display the image
        JLabel imageLabel = new JLabel();
        // BufferedImage image = ImageIO.read(new
        // File("bin/Images/gameDisplayImage.png"));
        BufferedImage image = MainFrame.loadImage("scripts/Images/gameDisplayImage.png");
        // Resize the image
        int newWidth = image.getWidth() / 2;
        int newHeight = image.getHeight() / 2;
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(scaledImageIcon);

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(imageLabel);

        // Scroll pane for content panel
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 230, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Button to return to main frame
        JButton returnButton = new JButton("Return to main frame");
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.main(null);
                dispose();
            }
        });
        buttonPanel.add(returnButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new IntroFrame();
    }
}
