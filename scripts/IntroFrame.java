import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroFrame extends JFrame {
    public IntroFrame() {
        setTitle("General Game Rules");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(204, 153, 255));

        // sub panel for labels only
        JPanel labelsPanel = new JPanel();
        labelsPanel.setBackground(new Color(204, 153, 255));
        labelsPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("General Rules of the Game", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 45));

        JLabel textbody = new JLabel(
                "<html>BLOCKS - Press number key '1' on your keyboard, a block can be selected. It blocks enemy's path, and allows you to deploy defense towers. <br> When a deployed blocked is clicked, and there is not tower deployed on top, the block will removed and the cost will be refunded. <br><br> p.s. you cannot completely seal the path, towerbuilder will NOT respond to your command. <br><br> TOWERS - Keys 2-7 will select corresponding tower. Tower stats displays upon hovering your mouse on them. Similar to blocks, if clicked, <br> tower undeploys and the money refunds. <br><br> p.s. the towers can only be placed on top of a block. <br><br> GAMEPLAY - Drop-down menu can set the DIFFICULTY of the game. Press start button to start a new game. Once the game starts, the control <br> panel disappears and towers become non-editable. <br> <br> When a wave ends, all enemy will disappear. The total number of waves is dependent on the selcted difficulty. <br> <br> IMPORTANT - In order to quit, click ESC. </html>",
                SwingConstants.CENTER);
        textbody.setFont(new Font("Serif", Font.PLAIN, 24));

        // add to labels panel
        labelsPanel.add(title, BorderLayout.NORTH);
        labelsPanel.add(textbody, BorderLayout.CENTER);

        // button to return to main frame
        JButton returnButton = new JButton("Return to main frame");
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.main(null);
                dispose();
            }
        });

        panel.add(labelsPanel, BorderLayout.CENTER);
        panel.add(returnButton, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new IntroFrame();
    }
}