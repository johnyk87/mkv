package jk.mkv;

public class MKVException extends Exception {

	private static final long serialVersionUID = 3946971053408121437L;

	public MKVException() {
	}

	public MKVException(String message) {
		super(message);
	}

	public MKVException(Throwable cause) {
		super(cause);
	}

	public MKVException(String message, Throwable cause) {
		super(message, cause);
	}

}
