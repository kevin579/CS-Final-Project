import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

import javax.swing.*;

public class RankingFrame extends JFrame implements ActionListener {
	private JSplitPane mainPanel;
    private JPanel rankPanel, btnPanel;
	private JLabel title, ranks;
	private JButton mainMenu;
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new RankingFrame();
			}
		});
	}
	
	public RankingFrame() {
		this.setTitle("Rankings");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Maximize the window
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setUndecorated(true);

        //press esc to exit
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
                return false;
            }
        });

        //Set the font to be bigger
        //Change the margin and style of button
        //Create method to read file record.txt
        
        //Main Panel
        mainPanel = new JSplitPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setDividerLocation(760);
        mainPanel.setBackground(new Color(255, 240, 255));  // Set background color

        //Rank Panel
        rankPanel = new JPanel();
        rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.PAGE_AXIS));
        rankPanel.setPreferredSize(new Dimension(1500, 750));
        rankPanel.setBackground(new Color(255, 240, 255));  // Set background color

        title = new JLabel("Rankings");
        rankPanel.add(title);
        
        ranks = new JLabel("tbd..."); //tbd...
        rankPanel.add(ranks);
        
        //Button Panel for return button
        btnPanel = new JPanel();
        btnPanel.setLayout(new BorderLayout(10, 10));
        btnPanel.setPreferredSize(new Dimension(1500, 40));
        btnPanel.setBackground(new Color(255, 240, 255));  // Set background color

        mainMenu = new JButton("Return to Main Menu");
        mainMenu.setActionCommand("return");
        mainMenu.addActionListener(this);
        btnPanel.add(mainMenu, BorderLayout.PAGE_END);
        
        mainPanel.setTopComponent(rankPanel);
        mainPanel.setBottomComponent(btnPanel);
        this.add(mainPanel);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		
		if(event.equals("return")) {
	new MainFrame();
		}
	}
}

