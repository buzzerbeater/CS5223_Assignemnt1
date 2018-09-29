package maze_game;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;


public class MazeGUI extends JFrame {
	private static final long serialVersionUID = 1603598738913730098L;
	private JPanel mainPanel;
	private JPanel playerPanel;
	private JPanel mazePanel;
	private Vector<String> playerList;
	private String[][] maze;
	
	public MazeGUI() {
		this.initPanels();
		this.playerList = this.mockPlayerList();  // TODO: get playerList from game
		this.populatePlayerPanel();
		this.maze = this.mockMaze(); // TODO: get maze from game
		this.populateMazePanel();
 	}
	
	private void initPanels() {
		// player panel
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
		playerPanel.setPreferredSize(new Dimension(200, 500));
		this.playerPanel = playerPanel;
		
		// maze panel
		this.mazePanel = new JPanel(new GridLayout(5, 5)); // TODO: use n for initialization
		this.mazePanel.setPreferredSize(new Dimension(500, 500));
		
		// main panel / frame
		this.mainPanel = new JPanel(new BorderLayout());
		this.mainPanel.add(this.playerPanel, BorderLayout.LINE_START);
		this.mainPanel.add(this.mazePanel, BorderLayout.CENTER);
		
		setContentPane(this.mainPanel);
		setTitle("current player: test");  // TODO: player id
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void populatePlayerPanel() {
		this.playerPanel.add(new JLabel("Currently " + this.playerList.size() + " player(s):"));
		for (String playerId: this.playerList) {
			this.playerPanel.add(new JLabel(playerId));
		}
		this.playerPanel.revalidate();
		this.playerPanel.repaint();
	}
	
	private void populateMazePanel() {
		for (int i = 0; i < 5; i++) {  // TODO: use n
			for (int j = 0; j < 5; j++) {  // TODO: use n
				JLabel cell = new JLabel(this.maze[i][j]);
				cell.setBorder(BorderFactory.createLineBorder(Color.black));
				this.mazePanel.add(cell);
			}
		}
		this.mazePanel.revalidate();
		this.mazePanel.repaint();
	}
	
	private Vector<String> mockPlayerList() {
		Vector<String> playerList = new Vector<String>();
		int i = 1;
		while (i <= 10) {
			String letter = Character.toString((char)(i+'a'-1));
			playerList.add(letter + letter);
			i++;
		}
		return playerList;
	}
	
	private String[][] mockMaze() {
		String [][] maze = {
				{"aa", "", "*", "bb", ""}, 
				{"cc", "dd", "*", "", ""}, 
				{"ff", "", "*", "", "ee"}, 
				{"", "gg", "", "", ""}, 
				{"", "", "*", "", "*"}
				};
		
		return maze;
	}
	
    public static void main(String[] args) {
    	new MazeGUI();
    }
}
