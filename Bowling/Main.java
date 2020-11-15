/**
 * 
 */

/**
 * @author Julia
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] testRolls = new int[]{1,2,3,4,8,2,5,4,0,10, 1,6,8,1,5,5,4,3,1,9};
		BowlingGame game = new BowlingGame();
		for (int i = 0; i < testRolls.length; i++) {
			game.rollBall(testRolls[i]);
		}
		
	}

}
