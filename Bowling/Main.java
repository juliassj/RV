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
		game.rollBall(4);
		game.rollBall(3);
		game.rollBall(5);
		game.rollBall(3);
		game.rollBall(9);
		game.rollBall(1);
		game.rollBall(3);
		game.rollBall(6);
		game.rollBall(10);
		game.rollBall(6);
		game.rollBall(2);
		game.rollBall(10);
		game.rollBall(10);
		game.rollBall(8);
		game.rollBall(1);
		game.rollBall(5);
		game.rollBall(5);
		game.rollBall(10);
//		while(game.getStatus() != Status.GAME_OVER) {
//			game.rollBall(r.nextInt(11));
//		}
	}
	
}

enum Status {
	GAME_OVER,
	IN_PROGRESS,
	BONUS
}
