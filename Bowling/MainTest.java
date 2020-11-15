import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
	
	static BowlingGame game;
	static final int NUM_ROLLS = 20;
	
	@BeforeEach
	void setupNewGame() {
		game = new BowlingGame();
	}

	@Test
	void newGamesStartsFresh() {
		assertEquals(0, game.getScore());
	}
	
	@Test
	void testRollBall() {
		game.rollBall(5);
		assertEquals(5, game.getScore());
		assertEquals(1, game.getCurrentRoll());
	}
	
	@Test
	void cannotRollNegativePins() {
		game.rollBall(-5);
	}
	
	@Test
	void testFullGameWithoutStrikeOrSpare() {
		int pins = 4;
		rollFullGame(pins);
		assertEquals(pins*NUM_ROLLS, game.getScore());
		assertEquals(NUM_ROLLS, game.getCurrentRoll());
	}
	
	@Test
	void testTenPinsOnSecondRoll() {
		game.rollBall(0);
		game.rollBall(10);
		assertEquals(2, game.getCurrentRoll());
		assertFalse(game.getFrame(0).isStrike());
	}
	
	// Scenario 1 + 2
	@Test
	void testRollsWithPinsLeft() {
		game.rollBall(5);
		assertEquals(5, game.getScore());
		game.rollBall(4);
		assertEquals(9, game.getScore());
	}
	
	//Scenario 3
	@Test 
	void testSpareWithoutNextRoll() {
		game.rollBall(4);
		game.rollBall(6);
		assertTrue(game.getFrame(0).isSpare());
		assertEquals(10, game.getFrameScore(0));
	}
	
	
	//Scenario 4
	@Test 
	void testStrikeWithoutNextRoll() {
		game.rollBall(10);
		assertTrue(game.getFrame(0).isStrike());
		assertEquals(10, game.getFrameScore(0));
	}
	
	
	// Scenario 5
	@Test
	void testSpareWithNextRoll() {
		game.rollBall(4);
		game.rollBall(6);
		game.rollBall(5);
		
		// Throw one more to ensure it's not counted in the score
		game.rollBall(1);
		
		// Score = 4 + 6 + 5
		assertEquals(15, game.getFrameScore(0));
	}
	
	// Scenario 6
	@Test
	void testStrikeWithNextRoll() {
		game.rollBall(10);
		
		// Test that the current roll has advanced to 2 (3rd roll) and skipped the 2nd roll
		assertEquals(2, game.getCurrentRoll());
		
		assertTrue(game.getFrame(0).isStrike());

		game.rollBall(3);
		game.rollBall(4);
		
		// Score = 10 + (3 + 4)
		assertEquals(17, game.getFrameScore(0));
	}
	
	// Scenario 7
	@Test
	void testFoulIsZero() {
		game.rollBall(5);
		game.rollFoul();
		
		//Assert the score is 5, based on the first roll
		assertEquals(5, game.getScore());
		//Assert the frame's 2nd roll is 0 (foul)
		assertEquals(0, game.getFrame(0).getSecondRoll());
		
		//Assert that, despite a 2nd roll of 0, the game has advanced to the next frame/roll
		assertEquals(2, game.getCurrentRoll());
		assertEquals(1, game.getCurrentFrameNum());
	}
	
	// Scenario 8
	@Test
	void testScoreQueryMidgame() {
		game.rollBall(5);
		game.rollBall(4);
		rollASpare();
		game.rollBall(3);
		assertEquals(25, game.getScore());
		
	}

	
	void rollASpare() {
		game.rollBall(4);
		game.rollBall(6);
	}
	
	void rollFullGame(int pins) {
		for (int i=0; i< NUM_ROLLS; i++) {
			game.rollBall(pins);
		}
	}
	
}
