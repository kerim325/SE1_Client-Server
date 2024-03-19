package server.exceptions;

public class PlayerNotRegisteredException extends ServerException {

	public PlayerNotRegisteredException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
