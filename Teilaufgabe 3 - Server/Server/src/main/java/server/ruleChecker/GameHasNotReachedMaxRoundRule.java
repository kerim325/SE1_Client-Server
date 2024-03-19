package server.ruleChecker;

import server.exceptions.GameRuleViolationException;
import server.gameData.Game;

public class GameHasNotReachedMaxRoundRule extends AbstractGameRule {

	private final int MAX_ROUND_NUMBER = 320;

	@Override
	GameRuleViolationException getException() {
		return new GameRuleViolationException("GameReachedMaxBoundError",
				"Game reached the maximum number of round. Game has to end. Winner will be selected randomly.");
	}

	@Override
	boolean validate(Game game) {
		return game.getGameState().rounds() < MAX_ROUND_NUMBER;
	}

}
