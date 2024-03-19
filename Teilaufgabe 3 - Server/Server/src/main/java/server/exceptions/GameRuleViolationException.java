package server.exceptions;

public class GameRuleViolationException extends RuleViolationException {

	public GameRuleViolationException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
