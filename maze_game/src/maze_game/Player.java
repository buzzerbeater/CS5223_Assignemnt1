package maze_game;

public class Player {
	String playerId;
	int score;
	Position position;
	
	public Player(String playerId, Position position) {
		this.playerId = playerId;
		this.position = position;
		this.score = 0;
	}
	
	// getter and setter
	public String getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}

	public int addScore(int scoreDelta) {
		this.score += scoreDelta;
		return this.score;
	}
	
}
