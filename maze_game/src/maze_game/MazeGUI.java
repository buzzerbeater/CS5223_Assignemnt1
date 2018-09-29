package maze_game;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;


public class MazeGUI extends JFrame {
	private static final long serialVersionUID = 1603598738913730098L;
	private int n;
	private JPanel mainPanel;
	private JPanel playerPanel;
	private JPanel mazePanel;
	private Vector<Player> playerList;
	private String[][] maze;
	
	// Initializations
	
	public MazeGUI(Player currentPlayer, GameState gameState) {
		this.n = gameState.getN();
		this.initPanels(currentPlayer.getPlayerId());
		this.playerList = gameState.getListOfCurrentPlayer();
		this.updatePlayerPanel();
		this.maze = gameState.getMaze();
		this.updateMazePanel();
 	}
	
	private void initPanels(String playerId) {
		// player panel
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
		playerPanel.setPreferredSize(new Dimension(200, 500));
		this.playerPanel = playerPanel;
		
		// maze panel
		this.mazePanel = new JPanel(new GridLayout(this.n, this.n));
		this.mazePanel.setPreferredSize(new Dimension(500, 500));
		
		// main panel / frame
		this.mainPanel = new JPanel(new BorderLayout());
		this.mainPanel.add(this.playerPanel, BorderLayout.LINE_START);
		this.mainPanel.add(this.mazePanel, BorderLayout.CENTER);
		
		setContentPane(this.mainPanel);
		setTitle("current player: " + playerId);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void updatePlayerPanel() {
		this.playerPanel.removeAll();
		this.playerPanel.add(new JLabel("Currently " + this.playerList.size() + " player(s):"));
		for (Player p: this.playerList) {
			this.playerPanel.add(new JLabel(p.getPlayerId() + ": " + p.getScore()));
		}
		this.playerPanel.revalidate();
		this.playerPanel.repaint();
	}
	
	private void updateMazePanel() {
		this.mazePanel.removeAll();
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				JLabel cell = new JLabel(this.maze[i][j], SwingConstants.CENTER);
				cell.setBorder(BorderFactory.createLineBorder(Color.black));
				this.mazePanel.add(cell);
			}
		}
		this.mazePanel.revalidate();
		this.mazePanel.repaint();
	}
	
	// Update & Repaint
	
	public void updatePanels(GameState gameState) {
		this.playerList = gameState.getListOfCurrentPlayer();
		this.updatePlayerPanel();
		this.maze = gameState.getMaze();
		this.updateMazePanel();
	}
}