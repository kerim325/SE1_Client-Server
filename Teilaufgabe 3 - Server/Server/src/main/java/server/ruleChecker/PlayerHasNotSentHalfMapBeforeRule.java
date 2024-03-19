package server.ruleChecker;

import server.exceptions.PlayerRuleViolationException;
import server.exceptions.PlayerSentMultipleHalfMapsException;
import server.gameData.player.Player;

public class PlayerHasNotSentHalfMapBeforeRule extends AbstractPlayerRule {

	@Override
	PlayerRuleViolationException getException() {
		return new PlayerSentMultipleHalfMapsException("PlayerSentMultipleHalfMapsError",
				"Same player cannot send more than one half map to server.");
	}

	@Override
	boolean validate(Player player) {
		return !player.isSentHalfMap();
	}

}
