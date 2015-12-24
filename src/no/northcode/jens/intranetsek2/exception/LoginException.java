package no.northcode.jens.intranetsek2.exception;

/**
 * Exception thrown when the login fails/expired
 * @author Jens V.
 *
 */
public class LoginException extends Exception {

	/**
	 * Instantiates a new login exception.
	 * Thrown when there's an error while logging in
	 * @param message the message
	 */
	public LoginException(String message)
	{
		super(message);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8198859471790489556L;

}
