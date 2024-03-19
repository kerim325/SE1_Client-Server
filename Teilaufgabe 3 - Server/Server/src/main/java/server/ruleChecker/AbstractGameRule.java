package server.ruleChecker;

import server.exceptions.GameRuleViolationException;
import server.gameData.Game;

public abstract class AbstractGameRule extends AbstractRule<Game> {

	@Override
	abstract GameRuleViolationException getException();

}
