package server.ruleChecker;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.PlayerHalfMapRuleViolationException;

public abstract class AbtractPlayerHalfMapRule extends AbstractRule<PlayerHalfMap> {

	@Override
	abstract PlayerHalfMapRuleViolationException getException();

}
