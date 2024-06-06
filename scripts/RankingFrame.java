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
	private JLabel title, rank, right, left, bottom;
	private JButton mainMenu;
	private ArrayList<Integer> sort = new ArrayList<Integer>();
	private HashMap<Integer, String> info = new HashMap<Integer, String>();
	
	RankingFrame() {
	   this.setTitle("Rankings");
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     //  this.setSize(new Dimension(MainFrame.panelWidth, MainFrame.panelHeight));
      
       //Main Panel
       mainPanel = new JSplitPane();
       mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
       mainPanel.setDividerLocation(760);
       mainPanel.setBackground(new Color(255, 240, 255)); 
       this.setPreferredSize(new Dimension(MainFrame.panelWidth, MainFrame.panelHeight)); // Set background color
       //Rank Panel
       rankPanel = new JPanel();
       rankPanel.setLayout(new BorderLayout());
       rankPanel.setPreferredSize(new Dimension(1500, 750));
       rankPanel.setBackground(new Color(255, 240, 255));  // Set background color
       this.setPreferredSize(new Dimension(MainFrame.panelWidth, MainFrame.panelHeight/20*19));
       
       title = new JLabel("Rankings", SwingConstants.CENTER);
       Font labelFont = title.getFont();
       title.setFont(new Font(labelFont.getName(), Font.PLAIN, 30));
       rankPanel.add(title, BorderLayout.PAGE_START);
       
       right = new JLabel("                                                                                  ");
       left = new JLabel("                                                                                  ");
       bottom = new JLabel("");
       rankPanel.add(right, BorderLayout.LINE_START);
       rankPanel.add(left, BorderLayout.LINE_END);
       rankPanel.add(bottom, BorderLayout.PAGE_END);
       
       //Read file and sort the scores of players.
       this.fileToSortedArray("scripts/ranking.txt");
       
       scrollPanel = new JPanel(new GridLayout(0, 3));
       scroll = new JScrollPane(scrollPanel);
       scrollPanel.setBackground(new Color(230, 255, 255));
     
       for(int i = 0; i < sort.size() + 1; i++) {
    	   if(i == 0) {
    		   rank = new JLabel("Rank", SwingConstants.CENTER); 
        	   rank.setAlignmentX(LEFT_ALIGNMENT);
        	   Font font = rank.getFont();
               rank.setFont(new Font(font.getName(), Font.PLAIN, 16));
        	   rank.setPreferredSize(new Dimension(1,50));
        	   scrollPanel.add(rank);
        	   
        	   rank = new JLabel("User", SwingConstants.CENTER); 
        	   rank.setAlignmentX(LEFT_ALIGNMENT);
               rank.setFont(new Font(font.getName(), Font.PLAIN, 16));
        	   rank.setPreferredSize(new Dimension(1,50));
        	   scrollPanel.add(rank);
        	   
        	   rank = new JLabel("Score", SwingConstants.CENTER);
        	   rank.setAlignmentX(LEFT_ALIGNMENT);
               rank.setFont(new Font(font.getName(), Font.PLAIN, 16));
        	   rank.setPreferredSize(new Dimension(1,50));
        	   scrollPanel.add(rank);  
    	   }
    	   else {
    		   int key = sort.get(i-1);
        	   String score = sort.get(i-1).toString();
        	   
        	   rank = new JLabel(i + ". ", SwingConstants.CENTER); 
        	   rank.setAlignmentX(LEFT_ALIGNMENT);
        	   Font font = rank.getFont();
               rank.setFont(new Font(font.getName(), Font.PLAIN, 16));
        	   rank.setPreferredSize(new Dimension(1,50));
        	   scrollPanel.add(rank);
        	   
        	   rank = new JLabel(this.info.get(key), SwingConstants.CENTER); 
        	   rank.setAlignmentX(LEFT_ALIGNMENT);
               rank.setFont(new Font(font.getName(), Font.PLAIN, 16));
        	   rank.setPreferredSize(new Dimension(1,50));
        	   scrollPanel.add(rank);
        	   
        	   rank = new JLabel(score, SwingConstants.CENTER);
        	   rank.setAlignmentX(LEFT_ALIGNMENT);
               rank.setFont(new Font(font.getName(), Font.PLAIN, 16));
        	   rank.setPreferredSize(new Dimension(1,50));
        	   scrollPanel.add(rank);
    	   }
       }
   
       rankPanel.add(scroll, BorderLayout.CENTER);
      
       //Button Panel for return button
       btnPanel = new JPanel();
       btnPanel.setLayout(new BorderLayout(10, 10));
       btnPanel.setPreferredSize(new Dimension(MainFrame.panelWidth, MainFrame.panelHeight/20));
       btnPanel.setBackground(new Color(255, 240, 255));  // Set background color
       mainMenu = new JButton("Return to Main Menu");
       mainMenu.setActionCommand("return");
       mainMenu.addActionListener(this);
       btnPanel.add(mainMenu, BorderLayout.PAGE_END);
      
       mainPanel.setTopComponent(rankPanel);
       mainPanel.setBottomComponent(btnPanel);
       this.add(mainPanel);
       this.pack();
       this.setLocationRelativeTo(null);
       this.setVisible(true);
	}
	
	public void fileToSortedArray(String file) {
		//fills the array with data from file.
		String line;
		
		try {
			FileReader in = new FileReader(file);
			BufferedReader reader = new BufferedReader(in);
			
			while((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				if(parts.length == 2) {
					String user = parts[0]; //Usernames
					int score = Integer.parseInt(parts[1]); //Scores
					
					this.info.put(score, user); //Add users and scores into hashmap.
					
					this.sort.add(score); //Add scores into arraylist.
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
		
		//Sort arraylist in descending order.
		Collections.sort(sort, Collections.reverseOrder());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		
		if(event.equals("return")) {
			new MainFrame();
		}
	}
}

