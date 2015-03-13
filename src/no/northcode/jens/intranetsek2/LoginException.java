package no.northcode.jens.intranetsek2;

/**
 * Exception thrown when the login fails/expired
 * @author Jens V.
 *
 */
public class LoginException extends Exception {

	public LoginException(String message)
	{
		super(message);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8198859471790489556L;

}
