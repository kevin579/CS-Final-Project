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
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        TitlePanel titlePanel = new TitlePanel();
        panel.add(titlePanel);
        GamePanel gamePanel = new GamePanel();
        panel.add(gamePanel);
        TowerPanel towerPanel = new TowerPanel();
        panel.add(towerPanel);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private class TitlePanel extends JPanel{
        TitlePanel(){
            this.setPreferredSize(new Dimension(MainFrame.panelWidth,MainFrame.panelHeight/30*2));
            this.setBackground(Color.GRAY);
        }
        public void paintComponents(Graphics g){
            super.paintComponents(g);
        }
    }
    private class GamePanel extends JPanel{
        GamePanel(){
            this.setPreferredSize(new Dimension(MainFrame.panelWidth,MainFrame.panelHeight/30*24));
            this.setBackground(Color.WHITE);
        }
        public void paintComponents(Graphics g){
            super.paintComponents(g);
        }
    }
    private class TowerPanel extends JPanel{
        TowerPanel(){
            this.setPreferredSize(new Dimension(MainFrame.panelWidth,MainFrame.panelHeight/30*4));
            this.setBackground(Color.GRAY);
        }
        public void paintComponents(Graphics g){
            super.paintComponents(g);
        }
    }
    private class KeyInput extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                System.exit(0);
            }
        }
    }
}
