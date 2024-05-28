import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        this.setSize(new Dimension(1500, 800));
        
        //Main Panel
        mainPanel = new JSplitPane();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setDividerLocation(760);
        
        //Rank Panel
        rankPanel = new JPanel();
        rankPanel.setLayout(new BoxLayout(rankPanel, BoxLayout.PAGE_AXIS));
        rankPanel.setPreferredSize(new Dimension(1500, 750));
        
        title = new JLabel("Rankings");
        rankPanel.add(title);
        
        ranks = new JLabel("tbd..."); //tbd...
        rankPanel.add(ranks);
        
        //Button Panel for return button
        btnPanel = new JPanel();
        btnPanel.setLayout(new BorderLayout(10, 10));
        btnPanel.setPreferredSize(new Dimension(1500, 40));
        
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
			//return to main menu.
		}
	}
}

