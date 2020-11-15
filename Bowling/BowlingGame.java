import java.util.ArrayList;

public class BowlingGame {

	private int currentRoll;
	private ArrayList<Frame> frames;

	public BowlingGame() {
		this.currentRoll = 0;
		this.frames = new ArrayList<Frame>(10);
		for (int i = 0; i < 10; i++) {
			this.frames.add(new Frame());
		}
	}

	public int getScore() {
		int score = 0;
		int currentFrameNum = getCurrentFrameNum();
		for (int i = 0; i <= currentFrameNum; i++) {
			score += getFrameScore(i);
		}

		return score;
	}

	public int getCurrentRoll() {
		return this.currentRoll;
	}

	
	public void rollFoul() {
		currentRoll++;
	}
	
	public void rollBall(int numPinsHit) {
		if (numPinsHit < 0 || numPinsHit > 10) {
			// throw new Exception("Number of pins must be between 0 and 10");
		}

		Frame currentFrame = getCurrentFrame();
		int rollNum = currentRoll % 2;
		// Check if it's a strike
		if (numPinsHit == 10 && rollNum == 0) {
			int[] rolls = { 10, 0 };
			currentFrame.updateBothRolls(rolls);
			currentRoll++;
			} else {
			currentFrame.updateOneRoll(numPinsHit, rollNum);
		}
		
		if(rollNum == 1) {
			displayScore();
		}
		
		currentRoll++;

		
	}

	public Frame getFrame(int frameNum) {
		return this.frames.get(frameNum);
	}

	public Frame getCurrentFrame() {
		return this.frames.get(this.currentRoll / 2);
	}

	public int getCurrentFrameNum() {
		if(this.currentRoll >=20 ) {
			return 9;
		} else {
		return this.currentRoll / 2;
		}
	}

	public int getFrameScore(int frameNum) {
		Frame frame = this.frames.get(frameNum);
		int score = frame.getFirstRoll() + frame.getSecondRoll();
		if (frameNum < 9) {
			Frame nextFrame = this.frames.get(frameNum + 1);
			if (frame.isSpare()) {
				score += nextFrame.getFirstRoll();
			} else if (frame.isStrike()) {
				score += nextFrame.getFirstRoll() + nextFrame.getSecondRoll();
			}
		}
		return score;
	}

	public void displayScore() {
		int currentFrameNum = this.getCurrentFrameNum();
		for (int i = 0; i <= currentFrameNum; i++) {
			System.out.println("Frame: " + i + " Rolls: " + this.frames.get(i).getFirstRoll() + ", "
					+ this.frames.get(i).getSecondRoll() + " Frame score: " + getFrameScore(i));
		}
		System.out.println("Total score: " + getScore());
	}

}
