package no.northcode.jens.intranetsek2.exception;

/**
 * The Class IntranetException.
 * Thrown when any unexpected result happens after interaction with the intranet
 * @author Jens V.
 */
public class IntranetException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 656207974296941265L;

	/**
	 * Instantiates a new intranet exception.
	 *
	 * @param message the message
	 */
	public IntranetException(String message) {
		super(message);
	}
	
	public IntranetException() {
		super();
	}
	

}
