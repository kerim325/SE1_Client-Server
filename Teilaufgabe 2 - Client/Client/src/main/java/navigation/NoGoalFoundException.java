package navigation;

public class NoGoalFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5132735605265553539L;

	public NoGoalFoundException() {
		super();
	}

	public NoGoalFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoGoalFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoGoalFoundException(String message) {
		super(message);
	}

	public NoGoalFoundException(Throwable cause) {
		super(cause);
	}

}
