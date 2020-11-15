import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BowlingTest {

	static BowlingGame game;
	static final int NUM_ROLLS = 20;

	@BeforeEach
	void setupNewGame() {
		game = new BowlingGame();
	}

	@Test
	void testGameStartsFresh() {
		assertEquals(0, game.getScore());
		assertEquals(0, game.getCurrentRoll());
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
		assertEquals(0, game.getScore());
		assertEquals(0, game.getCurrentRoll());
	}

	@Test
	void cannotRollMoreThanTen() {
		game.rollBall(11);
		assertEquals(0, game.getScore());
		assertEquals(0, game.getCurrentRoll());
	}

	@Test
	void cannotHitMorePinsThanPinsLeft() {
		game.rollBall(4);
		game.rollBall(7);
		assertEquals(4, game.getScore());
		assertEquals(1, game.getCurrentRoll());
	}

	@Test
	void canHitMorePinsIfFinalFrame() {
		// Roll 4s for 9 frames
		for (int i = 0; i < 18; i++) {
			game.rollBall(4);
		}
		int currentRoll = game.getCurrentRoll();
		int currentScore = game.getScore();

		// Roll 10 pins twice in one frame
		game.rollBall(10);
		game.rollBall(10);

		// Game should be updated
		assertEquals(currentScore + 20, game.getScore());
		assertEquals(currentRoll + 2, game.getCurrentRoll());

	}

	// Roll 4s every time
	@Test
	void testFullGameWithoutStrikeOrSpare() {
		int pins = 4;
		rollFullGame(pins);
		assertEquals(pins * NUM_ROLLS, game.getScore());
		assertEquals(NUM_ROLLS - 1, game.getCurrentRoll());
	}

	// Test when 10 pins are hit on second roll of frame (i.e. a spare not a strike)
	@Test
	void testTenPinsOnSecondRoll() {
		game.rollBall(0);
		game.rollBall(10);
		assertEquals(2, game.getCurrentRoll());
		assertFalse(game.getFrame(0).isStrike());
		assertTrue(game.getFrame(0).isSpare());

		game.rollBall(4);
		game.rollBall(2);

		// assert that only 4 pins are added to previous frame's score
		assertEquals(14, game.getFrameScore(0));
	}

	// Scenario 1 + 2
	@Test
	void testRollsWithPinsLeft() {
		game.rollBall(5);
		assertEquals(5, game.getFrameScore(0));
		game.rollBall(4);
		assertEquals(9, game.getFrameScore(0));
	}

	// Scenario 3
	@Test
	void testSpareWithoutNextRoll() {
		game.rollBall(4);
		game.rollBall(6);
		assertTrue(game.getFrame(0).isSpare());
		assertEquals(10, game.getFrameScore(0));
	}

	// Scenario 4
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

		// Frame Score = 4 + 6 + 5
		assertEquals(15, game.getFrameScore(0));
		assertEquals((15 + 6), game.getScore());
	}

	// Scenario 6
	@Test
	void testStrikeWithNextRoll() {
		game.rollBall(10);

		// Test that the current roll has advanced to 2 (3rd roll) and skipped the 2nd
		assertEquals(2, game.getCurrentRoll());

		assertTrue(game.getFrame(0).isStrike());

		game.rollBall(3);
		game.rollBall(4);

		// Frame Score = 10 + (3 + 4)
		assertEquals(17, game.getFrameScore(0));
		assertEquals((17 + 7), game.getScore());
	}

	// Test that 2 strikes in a row scores properly
	@Test
	void testDoubleStrike() {
		game.rollBall(10);
		game.rollBall(10);
		game.rollBall(4);
		game.rollBall(5);

		assertEquals(24, game.getFrameScore(0));
		assertEquals(19, game.getFrameScore(1));
		assertEquals((24 + 19 + 4 + 5), game.getScore());
	}

	// Scenario 7
	@Test
	void testFoulIsZero() {
		game.rollBall(5);
		game.rollFoul();

		// Assert the score is 5, based on the first roll
		assertEquals(5, game.getFrameScore(0));
		// Assert the frame's 2nd roll is 0 (foul)
		assertEquals(0, game.getFrame(0).getSecondRoll());
		assertEquals(5, game.getFrameScore(0));

		// Assert that, despite a 2nd roll of 0, the game has advanced to the next
		// frame/roll
		assertEquals(2, game.getCurrentRoll());
		assertEquals(1, game.getCurrentFrameNum());
	}

	@Test
	void canRollAllFouls() {
		for (int i = 0; i < NUM_ROLLS; i++) {
			game.rollFoul();
		}

		assertEquals(0, game.getScore());
		assertEquals(Status.GAME_OVER, game.getStatus());
	}

	@Test
	void canRollFoulOnBonusBall() {
		for (int i = 0; i < NUM_ROLLS; i++) {
			game.rollBall(5);
		}

		int currentScore = game.getScore();
		assertEquals(Status.BONUS, game.getStatus());

		game.rollFoul();
		assertEquals(Status.GAME_OVER, game.getStatus());
		assertEquals(currentScore, game.getScore());
	}

	// Scenario 8
	@Test
	void testScoreQueryMidgame() {
		game.rollBall(5);
		game.rollBall(4);

		// Roll a spare
		game.rollBall(6);
		game.rollBall(4);

		game.rollBall(3);

		int frameScoresSum = game.getFrameScore(0) + game.getFrameScore(1) + game.getFrameScore(2);

		assertEquals(frameScoresSum, game.getScore());
		assertEquals(25, frameScoresSum);

	}

	// Game should end after 20 rolls (assuming no bonus)
	@Test
	void canOnlyRoll20Times() {
		rollFullGame(4);

		assertEquals(Status.GAME_OVER, game.getStatus());

		int score = game.getScore();

		game.rollBall(4);

		assertEquals(score, game.getScore());
	}

	// If the player gets a bonus roll, game should end after 21st roll
	@Test
	void canRollBonusIfSpare() {
		for (int i = 0; i < NUM_ROLLS - 2; i++) {
			game.rollBall(4);
		}
		game.rollBall(5);
		game.rollBall(5);

		assertEquals(Status.BONUS, game.getStatus());

		game.rollBall(6);

		assertEquals(Status.GAME_OVER, game.getStatus());

	}

	@Test
	void canRollBonusIfStrikeOnFirstRoll() {
		for (int i = 0; i < NUM_ROLLS - 2; i++) {
			game.rollBall(4);
		}
		game.rollBall(10);

		assertEquals(Status.IN_PROGRESS, game.getStatus());

		game.rollBall(5);

		assertEquals(Status.BONUS, game.getStatus());

		game.rollBall(6);

		assertEquals(Status.GAME_OVER, game.getStatus());

	}

	@Test
	void canRollBonusIfStrikeOnSecondRoll() {
		for (int i = 0; i < NUM_ROLLS - 2; i++) {
			game.rollBall(4);
		}
		game.rollBall(5);
		game.rollBall(10);

		assertEquals(Status.BONUS, game.getStatus());

		game.rollBall(6);

		assertEquals(Status.GAME_OVER, game.getStatus());

	}

	// Test hitting 10 pins on each roll of last frame
	@Test
	void canRollTripleStrikeOnLastFrame() {
		for (int i = 0; i < NUM_ROLLS - 2; i++) {
			game.rollBall(4);
		}

		int currentScore = game.getScore();

		game.rollBall(10);

		assertEquals(Status.IN_PROGRESS, game.getStatus());

		game.rollBall(10);

		assertEquals(Status.BONUS, game.getStatus());

		game.rollBall(10);

		assertEquals(Status.GAME_OVER, game.getStatus());

		// Assert it scores the last frame properly
		assertEquals(currentScore + 30, game.getScore());
	}

	// Test sample data
	@Test
	void testCorrectScore() {
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
		game.rollBall(10);
		game.rollBall(10);
		game.rollBall(10);

		assertTrue(game.getStatus() == Status.GAME_OVER);
		assertEquals(149, game.getScore());
	}

	@Test
	void testPerfectGame() {
		// Should be able to roll 12 strikes (10 regular and 2 bonus)
		for (int i = 0; i < 12; i++) {
			game.rollBall(10);
		}

		assertTrue(game.getStatus() == Status.GAME_OVER);
		assertEquals(300, game.getScore());
	}

	@Test
	void canRollAllSpares() {
		for (int i = 0; i < NUM_ROLLS; i++) {
			game.rollBall(5);
		}
		assertEquals(Status.BONUS, game.getStatus());

		game.rollBall(5);
		assertEquals(Status.GAME_OVER, game.getStatus());
		assertEquals(150, game.getScore());
	}

	// Make sure the final frame has the bonus roll included in score
	@Test
	void testFinalFrameScoreWithBonus() {
		for (int i = 0; i < NUM_ROLLS; i++) {
			game.rollBall(5);
		}
		assertEquals(10, game.getFrameScore(9));

		game.rollBall(4);

		assertEquals(14, game.getFrameScore(9));
	}

	void rollFullGame(int pins) {
		for (int i = 0; i < NUM_ROLLS; i++) {
			game.rollBall(pins);
		}
	}

}
