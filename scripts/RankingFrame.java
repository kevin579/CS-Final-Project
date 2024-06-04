package finalProject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class RankingFrame extends JFrame implements ActionListener {
	private JSplitPane mainPanel;
	private JScrollPane scroll;
    private JPanel rankPanel, btnPanel, scrollPanel;
	private JLabel title, rank, spacer, spacel;
	private JButton mainMenu;
	private ArrayList<Integer> sort = new ArrayList<Integer>();
	private HashMap<Integer, String> info = new HashMap<Integer, String>();
	
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
       mainPanel.setBackground(new Color(255, 240, 255));  // Set background color
       //Rank Panel
       rankPanel = new JPanel();
       rankPanel.setLayout(new BorderLayout());
       rankPanel.setPreferredSize(new Dimension(1500, 750));
       rankPanel.setBackground(new Color(255, 240, 255));  // Set background color
       
       title = new JLabel("Rankings");
       rankPanel.add(title, BorderLayout.PAGE_START);
       
       spacer = new JLabel("                                                                                  ");
       spacel = new JLabel("                                                                                  ");
       rankPanel.add(spacer, BorderLayout.LINE_START);
       rankPanel.add(spacel, BorderLayout.LINE_END);
       
       this.fileToArray("src/finalProject/ranking.txt");
       determineRank();
       
       scrollPanel = new JPanel();
       scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
     
       for(int i = 1; i < sort.size() + 1; i++) {
    	   String score = sort.get(i-1).toString();
    	   rank = new JLabel(i + ". " + info.get(score) + "  " + score);
    	   rank.setPreferredSize(new Dimension(300,100));
    	   scrollPanel.add(rank);
       }
       
       scroll = new JScrollPane(scrollPanel);
       scroll.setPreferredSize(new Dimension(200, 200));
       
       rankPanel.add(scroll, BorderLayout.CENTER);
      
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
	
	public void fileToArray(String file) {
		//fills the array with data from file.
		int index = 0;
		String line;
		
		try {
			FileReader in = new FileReader(file);
			BufferedReader reader = new BufferedReader(in);
			
			while((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				int count = 0;
				if(parts.length == 2) {
					String user = parts[0];
					int score = Integer.parseInt(parts[1]);
					
					this.sort.add(score);
					
					count++;
					this.info.put(score, user);
				}
			}
			reader.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File could not be found.");
		}
		catch(NumberFormatException e) {
			System.out.println("Format mismatch.");
		}
		catch(IOException e) {
			System.out.println("An error has occured.");
		}
	}
	
	public void determineRank() {
		for(int index = 1; index < this.info.size(); index++) {
			int prevIndex = index -1;
			int x = sort.get(prevIndex);
			int y = sort.get(prevIndex + 1);
			int temp = sort.get(index);
			
			while ((x > temp) && (prevIndex > 0)) {
				y = x;
				prevIndex -= 1;
			}
			if(sort.get(prevIndex) > temp) {
				y = x;
				x = temp;
			}
			else {
				y = temp;
			}
		}
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		
		if(event.equals("return")) {
			new MainFrame();
		}
	}
}

