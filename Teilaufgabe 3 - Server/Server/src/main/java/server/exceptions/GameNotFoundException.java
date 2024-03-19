package server.exceptions;

public class GameNotFoundException extends ServerException {

	public GameNotFoundException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
