package no.northcode.jens.intranetsek2.exception;


/**
 * The Exception InvalidCredentialsException
 * Thrown when the login fails due to Invalid Credentials
 * @author Jens V.
 */
public class InvalidCredentialsException extends LoginException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1332412511821261682L;

	/**
	 * Instantiates a new invalid credentials exception.
	 *
	 * @param message the message
	 */
	public InvalidCredentialsException() {
		super("Invalid Credentials");
		// TODO Auto-generated constructor stub
	}

}
