import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
	
	static BowlingGame game;
	static final int NUM_TURNS = 10;
	
	@BeforeAll
	static void setUpGame() {
		game = new BowlingGame();
	}
	
	@BeforeEach
	void clearScore() {
		game.clearScore();
	}

	@Test
	void newGamesStartsFresh() {
		assertEquals(0, game.getScore());
	}
	
	@Test
	void canScorePoints() {
		BowlingGame game = new BowlingGame();
		game.rollBall(5);
		assertEquals(5, game.getScore());
	}
	
	@Test
	void canPlayGame() {
		int pins = 5;
		rollFullGame(pins);
		assertEquals(pins*NUM_TURNS*2, game.getScore());
		
	}
	
	void rollFullGame(int pins) {
		for (int i=0; i< NUM_TURNS*2; i++) {
			game.rollBall(pins);
		}
	}
	
}
