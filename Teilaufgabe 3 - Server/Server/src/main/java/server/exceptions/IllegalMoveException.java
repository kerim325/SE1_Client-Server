package server.exceptions;

public class IllegalMoveException extends PlayerRuleViolationException {

	public IllegalMoveException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
