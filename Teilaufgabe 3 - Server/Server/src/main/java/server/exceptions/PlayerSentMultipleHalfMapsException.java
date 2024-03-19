package server.exceptions;

public class PlayerSentMultipleHalfMapsException extends PlayerRuleViolationException {

	public PlayerSentMultipleHalfMapsException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
