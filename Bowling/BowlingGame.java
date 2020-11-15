public class BowlingGame {
	
	private int score;


	public BowlingGame() {
		this.score = 0;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void rollBall(int numPinsHit) {
		this.score += numPinsHit;
	}
	
	public void clearScore() {
		this.score = 0;
	}

}
