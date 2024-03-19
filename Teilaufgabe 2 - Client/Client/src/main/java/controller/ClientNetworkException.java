package controller;

public class ClientNetworkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3158297573825963877L;

	public ClientNetworkException() {
		super();
	}

	public ClientNetworkException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientNetworkException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientNetworkException(String message) {
		super(message);
	}

	public ClientNetworkException(Throwable cause) {
		super(cause);
	}

}
