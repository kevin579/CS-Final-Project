import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame{
    public static void main(String[] args) {
        new GameFrame();
    }
    
    GameFrame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(MainFrame.panelWidth,MainFrame.panelHeight));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        TitlePanel titlePanel = new TitlePanel();
        panel.add(titlePanel);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private class TitlePanel extends JPanel{
        TitlePanel(){
            this.setPreferredSize(new Dimension(MainFrame.panelWidth,MainFrame.panelHeight/10));
            this.setBackground(Color.BLACK);
        }
        public void paintComponents(Graphics g){
            super.paintComponents(g);
        }
    }
}
