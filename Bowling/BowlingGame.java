import java.util.ArrayList;

public class BowlingGame {

	private int currentRoll;
	private ArrayList<Frame> frames;
	private Status status;
	private int bonus;

	public BowlingGame() {
		this.currentRoll = 0;
		this.frames = new ArrayList<Frame>(10);
		for (int i = 0; i < 10; i++) {
			this.frames.add(new Frame());
		}
		this.status = Status.IN_PROGRESS;
		this.bonus = 0;
	}

	public int getScore() {
		int score = 0;
		int currentFrameNum = getCurrentFrameNum();

		// Total score is simply the sum of all the frame scores up to this point
		for (int i = 0; i <= currentFrameNum; i++) {
			score += getFrameScore(i);
		}
		return score;
	}

	public Frame getFrame(int frameNum) {
		return this.frames.get(frameNum);
	}

	public Frame getCurrentFrame() {
		return this.frames.get(getCurrentFrameNum());
	}

	public int getCurrentFrameNum() {
		return this.currentRoll >= 20 ? 9 : this.currentRoll / 2;
	}

	public int getFrameScore(int frameNum) {
		Frame frame = this.frames.get(frameNum);
		// Base score just the sum of the rolls
		int score = frame.getFirstRoll() + frame.getSecondRoll();

		// Check if any frame except the last one needs a spare or strike bonus
		if (frameNum < 9) {
			Frame nextFrame = this.frames.get(frameNum + 1);
			if (frame.isSpare()) {
				score += nextFrame.getFirstRoll();
			} else if (frame.isStrike()) {
				// If a double strike, the 2nd bonus roll comes from the next, next frame
				if (nextFrame.isStrike() && frameNum < 8) {
					score += nextFrame.getFirstRoll() + this.frames.get(frameNum + 2).getFirstRoll();
				} else {
					score += nextFrame.getFirstRoll() + nextFrame.getSecondRoll();
				}
			}
			// the final frame gets the bonus if there is one
		} else if (frameNum == 9) {
			score += this.bonus;
		}
		return score;
	}

	public Status getStatus() {
		return this.status;
	}

	public int getCurrentRoll() {
		return this.currentRoll;
	}

	public void updateGame(Frame currentFrame) {

		if (currentRoll == 19
				&& (currentFrame.isSpare() || currentFrame.isStrike() || currentFrame.getSecondRoll() == 10)) {
			this.status = Status.BONUS;
		} else if (currentRoll >= 19) {
			this.status = Status.GAME_OVER;
			displayFinalScore();
			return;
		}

		if (currentRoll % 2 == 1) {
			displayScore();
		}

		currentRoll++;

	}

	public void rollFoul() {
		Frame currentFrame = getCurrentFrame();

		updateGame(currentFrame);
	}

	public void rollBall(int numPinsHit) {

		if (this.status != Status.GAME_OVER) {

			// Check if number of pins is valid
			if (numPinsHit < 0 || numPinsHit > 10) {
				// throw new Exception("Number of pins must be between 0 and 10");
				// Just return for now
				return;
			}

			Frame currentFrame = getCurrentFrame();

			// Check if number of pins is less than number of pins remaining
			if (currentRoll < 19 && currentRoll % 2 == 1 && numPinsHit > (10 - currentFrame.getFirstRoll())) {
				// throw new Exception("# of pins cannot be more than # of pins remaining");
				// Just return for now
				return;
			}

			if (this.status == Status.BONUS) {
				this.bonus = numPinsHit;
			} else {
				// Check if it's a strike and not the final frame
				if (numPinsHit == 10 && currentRoll % 2 == 0 && getCurrentFrameNum() != 9) {
					int[] strikeRolls = { 10, 0 };
					currentFrame.updateBothRolls(strikeRolls);
					// If it's a strike, there's no 2nd roll, so advance counter
					currentRoll++;
				} else {
					currentFrame.updateOneRoll(numPinsHit, currentRoll % 2);
				}
			}

			updateGame(currentFrame);
		}

	}

	public void displayScore() {
		int currentFrameNum = this.getCurrentFrameNum();
		String str = "";
		for (int i = 0; i <= currentFrameNum; i++) {
			str += "[ ";
			Frame frame = getFrame(i);
			int frameScore = getFrameScore(i);
			int cumulativeScore = frameScore;
			for (int j = (i - 1); j >= 0; j--) {
				cumulativeScore += getFrameScore(j);
			}
			if (frame.isStrike() || (i == 9 && (frame.getSecondRoll() == 10 || bonus == 10))) {
				str += frameScore + "|" + cumulativeScore + "(X)";

			} else if (frame.isSpare()) {
				str += frameScore + "|" + cumulativeScore + "(/)";
			} else if (i != 0) {
				str += cumulativeScore + "(" + frameScore + ")";

			} else {
				str += frameScore;
			}
			str += " ]";

		}
		System.out.println(str);
	}

	public void displayFinalScore() {
		displayScore();
		System.out.println("Final score: " + getScore());
	}

}
