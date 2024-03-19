package server.ruleChecker;

import server.exceptions.RuleViolationException;
import server.gameData.Game;
import server.gameData.player.PlayerId;

public abstract class AbstractRule<T> {

	abstract boolean validate(T objectToBeValidated);

	abstract RuleViolationException getException();

	void ruleViolationHandler(Game game, PlayerId playerId) {
		game.playerLose(playerId);
	}
}
