import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

//the ending score panel
public class EndFrame extends JFrame implements ActionListener{
    int panelWidth, panelHeight;
    JPanel scorePanel;
    JButton button;
    BufferedImage endImg;

    EndFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("GameOver");
        this.panelWidth = MainFrame.panelWidth;
        this.panelHeight = MainFrame.panelHeight;
        // endImg = MainFrame.loadImage("Assignment5\\images\\endImg.jpg");
        scorePanel = new JPanel();
        scorePanel.setPreferredSize(new Dimension(this.panelWidth,this.panelHeight));
        scorePanel.setBackground(Color.WHITE);
        ImagePanel imgPanel = new ImagePanel();
        scorePanel.add(imgPanel);
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        String t = "Your score is " + String.valueOf(GameFrame.score);
        JLabel text = new JLabel(t);
        text.setFont(new Font("Serif", Font.PLAIN, 64));
        text.setBorder(BorderFactory.createEmptyBorder(0, 100, 100, 100));
        text.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        scorePanel.add(text);
        button = new JButton("close");
        button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        // button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setActionCommand("end");
        button.addActionListener(this);
        button.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        scorePanel.add(button);
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBorder(BorderFactory.createEmptyBorder(0, 100, 200, 100));
        emptyPanel.setBackground(Color.WHITE);
        scorePanel.add(emptyPanel);
        this.add(scorePanel);
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    private class ImagePanel extends JPanel{
        ImagePanel(){
            this.setBackground(Color.WHITE);
            this.setPreferredSize(new Dimension(panelWidth, panelHeight/2));
        }
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            // g.drawImage(endImg,0,0,panelWidth,panelHeight/2,null);
        }
    }
    public void actionPerformed(ActionEvent e){
        String name = e.getActionCommand();
        if(name.equals("end")){
            this.setVisible(false);
            System.exit(0);
        }
    }

}
