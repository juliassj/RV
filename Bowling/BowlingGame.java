public class BowlingGame {
	
	private int score;
	private int[] rolls;


	public BowlingGame() {
		this.rolls = new int[21];
		this.score = 0;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void updateScore() {
		int score = 0;
		for (int i=0; i < rolls.length; i++) {
			score += rolls[i];
		}
		this.score = score;
	}
	
	public void rollBall( int rollNum, int numPinsHit) {
		rolls[rollNum] = numPinsHit;
		this.updateScore();
		if(rollNum % 2 == 1) {
			this.displayScore(rollNum / 2);
		}
	}
	
	public void displayScore(int frameNum) {
		System.out.println("Current score after Frame " + frameNum + " is: " + this.score);
	}
	
	public void clearGame() {
		this.score = 0;
		this.rolls = new int[21];
	}

}
