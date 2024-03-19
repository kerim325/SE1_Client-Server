package server.exceptions;

public class PlayerRuleViolationException extends RuleViolationException {

	public PlayerRuleViolationException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
