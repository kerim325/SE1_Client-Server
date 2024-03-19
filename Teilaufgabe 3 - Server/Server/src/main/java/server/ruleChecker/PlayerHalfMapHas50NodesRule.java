package server.ruleChecker;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.PlayerHalfMapRuleViolationException;

public class PlayerHalfMapHas50NodesRule extends AbtractPlayerHalfMapRule {

	@Override
	public boolean validate(PlayerHalfMap objectToBeValidated) {
		return true;
	}

	@Override
	public PlayerHalfMapRuleViolationException getException() {
		return new PlayerHalfMapRuleViolationException("PlayerHalfMap error",
				"PlayerHalfMap does not have exactly 50 Nodes.");
	}

}
