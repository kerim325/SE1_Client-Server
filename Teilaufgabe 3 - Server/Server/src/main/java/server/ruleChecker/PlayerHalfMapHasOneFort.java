package server.ruleChecker;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.PlayerHalfMapRuleViolationException;

public class PlayerHalfMapHasOneFort extends AbtractPlayerHalfMapRule {

	private final static int NUMBER_OF_FORTS = 1;

	@Override
	public boolean validate(PlayerHalfMap halfMap) {
		return halfMap.getMapNodes().stream().filter(node -> node.isFortPresent()).count() == NUMBER_OF_FORTS;
	}

	@Override
	public PlayerHalfMapRuleViolationException getException() {
		return new PlayerHalfMapRuleViolationException("Player halfmap fort count error",
				"Player halfmap should have exactly one halfmap");
	}

}
