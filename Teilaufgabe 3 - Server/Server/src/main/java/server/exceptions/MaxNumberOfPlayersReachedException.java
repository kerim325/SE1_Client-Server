package server.exceptions;

public class MaxNumberOfPlayersReachedException extends ServerException {

	public MaxNumberOfPlayersReachedException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
