import java.util.Random;

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
		BowlingGame game = new BowlingGame();
		Random r = new Random();
		while(game.getStatus() != Status.GAME_OVER) {
			game.rollBall(r.nextInt(11));
		}
	}
	
}

enum Status {
	GAME_OVER,
	IN_PROGRESS,
	BONUS
}
