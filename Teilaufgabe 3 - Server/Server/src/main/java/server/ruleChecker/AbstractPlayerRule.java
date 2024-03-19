package server.ruleChecker;

import server.exceptions.PlayerRuleViolationException;
import server.gameData.player.Player;

public abstract class AbstractPlayerRule extends AbstractRule<Player> {

	@Override
	abstract PlayerRuleViolationException getException();

}
