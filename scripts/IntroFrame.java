import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IntroFrame extends JFrame {
    public IntroFrame() {
        setTitle("General Game Rules");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(204, 153, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel for title, text body, and image
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel for title and text body
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(204, 153, 255));
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel title = new JLabel("General Rules of the Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 45));
        textPanel.add(title);

        // Text body
        JLabel textbody = new JLabel(
                "<html>BLOCKS - Press number key '1' on your keyboard, a block can be selected. It blocks enemy's path, and allows you to deploy defense towers. <br> When a deployed blocked is clicked, and there is not tower deployed on top, the block will removed and the cost will be refunded. <br> p.s. you cannot completely seal the path, towerbuilder will NOT respond to your command. <br><br> TOWERS - Keys 2-7 will select corresponding tower. Tower stats displays upon hovering your mouse on them. Similar to blocks, if clicked, <br> tower undeploys and the money refunds. <br> p.s. the towers can only be placed on top of a block. <br><br> GAMEPLAY - Drop-down menu can set the DIFFICULTY of the game. Press start button to start a new game. Once the game starts, the control <br> panel disappears and towers become non-editable. <br> <br> When a wave ends, all enemy will disappear. The total number of waves is dependent on the selcted difficulty. <br> <br> IMPORTANT - In order to quit, click ESC. </html>",
                SwingConstants.CENTER);
        textbody.setFont(new Font("Serif", Font.PLAIN, 20));
        textPanel.add(textbody);

        contentPanel.add(textPanel);

        // Panel for image
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(new Color(204, 153, 255));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel imageLabel = new JLabel();
        try {
            BufferedImage image = ImageIO.read(new File("bin/Images/gameDisplayImage.png"));
            ImageIcon scaledImageIcon = new ImageIcon(image);
            imageLabel.setIcon(scaledImageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        contentPanel.add(imagePanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(204, 153, 255));
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
