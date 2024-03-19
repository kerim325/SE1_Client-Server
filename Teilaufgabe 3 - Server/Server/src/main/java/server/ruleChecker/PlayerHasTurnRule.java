package server.ruleChecker;

import server.exceptions.IllegalMoveException;
import server.exceptions.PlayerRuleViolationException;
import server.gameData.player.Player;

public class PlayerHasTurnRule extends AbstractPlayerRule {

	@Override
	public boolean validate(Player player) {
		return player.isTurn();
	}

	@Override
	public PlayerRuleViolationException getException() {
		return new IllegalMoveException("IllegalMoveError",
				"Player made a move/sent a half map, even though it was not his/her turn.");
	}

}
