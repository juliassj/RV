public class Frame {
	private int[] rolls;
	
	public Frame() {
		this.rolls = new int[] {0, 0};
	}
	
	public boolean isStrike() {
		return this.rolls[0] == 10;
	}
	
	public boolean isSpare() {
		return !isStrike() && this.rolls[0] + this.rolls[1] == 10;
	}
	
	public int[] getRolls() {
		return this.rolls;
	}
	public int getFirstRoll() {
		return this.rolls[0];
	}
	public int getSecondRoll() {
		return this.rolls[1];
	}
	
	public void updateOneRoll(int pins, int index) {
		this.rolls[index] = pins;
	}
	
	public void updateBothRolls(int[] rolls) {
		this.rolls = rolls;
	}
	
	public void displayFrameRolls() {
		System.out.println("Rolls: " + getFirstRoll() + ", " + getSecondRoll());
	}
	
}
