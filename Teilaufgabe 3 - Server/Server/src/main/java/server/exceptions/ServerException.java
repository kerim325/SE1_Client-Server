package server.exceptions;

public class ServerException extends RuntimeException {

	private final String errorName;

	public ServerException(String errorName, String errorMessage) {
		super(errorMessage);
		this.errorName = errorName;
	}

	public String getErrorName() {
		return errorName;
	}
}
