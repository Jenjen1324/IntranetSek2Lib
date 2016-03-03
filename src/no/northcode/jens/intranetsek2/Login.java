package no.northcode.jens.intranetsek2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;

import no.northcode.jens.intranetsek2.exception.IntranetException;
import no.northcode.jens.intranetsek2.exception.InvalidCredentialsException;
import no.northcode.jens.intranetsek2.exception.LoginException;

// TODO: Auto-generated Javadoc
/**
 * *
 * Logs the user in and stores the login session.
 *
 * @author Jens V.
 */
public class Login {

	//private static final String domain = "intranet.tam.ch";
	/** The Constant URL. */
	public static final String URL = "https://intranet.tam.ch";
	
	/** The Constant URL_NEWS. */
	public static final String URL_NEWS = "/index/all-appointments";
	
	/** The Constant URL_TIMETABLE. */
	public static final String URL_TIMETABLE = "/timetable/ajax-get-timetable";
	//private static final String URL_LIST_CLASS = "/list/index/list/45";
	
	/** The school. */
	private String school;
	
	/** The _class. */
	private String className;
	
	/** The username. */
	public String username;
	
	/** The password. */
	private String password;

	/** The userid. */
	private int userid;


	/** The sessionid. */
	private String sessionid;

	/**
	 * *
	 * Logs the user in.
	 *
	 * @param username the username
	 * @param password the password
	 * @param school the school
	 * @throws IOException When the request fails
	 * @throws InvalidCredentialsException when the credentials are invalid
	 * @throws LoginException When the user can't log in (e.g. wrong credentials)
	 * @throws IntranetException If any interaction with the intranet is unexpected or fails
	 */
	public Login(String username, String password, String school)
	{
		this.username = username;
		this.password = password;
		this.school = school; // TODO: Change to School obj
	}
	
	public void login() throws IOException, InvalidCredentialsException, LoginException, IntranetException {
		Boolean success = false;
		
		// Post request parameters
		
		StringBuilder builder = new StringBuilder();
		builder.append("loginuser=").append(this.username).append("&loginpassword=").append(this.password).append("&loginschool=").append(this.school);
		String urlParameters = builder.toString();
		
		byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		
		/*System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + urlParameters);*/
				
		// WebRequest
		HttpsURLConnection con = this.openUrlConnection(URL, "POST");
		con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects( false );
		con.setUseCaches( true );
		
		// Write Post Data
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		if(responseCode == 500) {
			throw new LoginException("Invalid School or WebError");
		} else if(responseCode == 200) {
			throw new InvalidCredentialsException();
		} else if(responseCode != 302) {
			
		}
 
		this.readUrlConnectionResponse(con);
		
		// Read Cookies
		String headerName = null;
		for (int i=1; (headerName = con.getHeaderFieldKey(i))!=null; i++) {
		 	if (headerName.equals("Set-Cookie")) {                  
		 		String cookie = con.getHeaderField(i); 
		 		System.out.println("Login Cookie: " + cookie);
		 		String[] parts = cookie.split(";");
		 		String[] parts2 = parts[0].split("=");
		 		
		 		if(parts2[0].equals("sturmsession"))
		 			this.sessionid = parts2[1];
		 		if(parts2[0].equals("sturmuser"))
		 			success = true;
		 	}
		}
		
		if(!success)
			throw new IntranetException();
		
		// Get userid
		this.userid = Integer.parseInt(Jsoup.parse(this.getRequest(URL + "/" +  school)).select("a[onclick][title][href]").first().attr("onclick").split("\\(")[1].split(",")[0].toString().replace("\'", ""));
	}
	
	/**
	 * Cookies to string.
	 *
	 * @return the string
	 */
	private String cookiesToString() {
		StringBuilder builder = new StringBuilder();
		builder.append("sturmsession=").append(this.sessionid).append("; sturmuser=").append(this.username);
		return builder.toString();
	}
	
	/**
	 * Gets the intern url.
	 *
	 * @param suburl the suburl
	 * @return the intern url
	 */
	public String createInternUrl(String suburl) {
		StringBuilder sb = new StringBuilder();
		sb.append(URL).append("/").append(this.school).append(suburl);
		return sb.toString();
	}
	
	
	
	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * *
	 * Does a GET request to the given URL and passes the session info along.
	 *
	 * @param url Url to make the request to
	 * @return Html String of the result
	 * @throws IOException When there is an error with the request (No internet, etc)
	 */
	public String getRequest(String url) throws IOException
	{
		HttpsURLConnection con = this.openUrlConnection(url, "GET");
		con.setRequestProperty("Cookie", this.cookiesToString());
		con.connect();
		return this.readUrlConnectionResponse(con);
	}

	
	/**
	 * Gets the school.
	 *
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}
	
	/**
	 * Gets the userid.
	 *
	 * @return the userid
	 */
	public int getUserid() {
		return userid;
	}
	
	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the url connection and sets the minimum required Request Properties.
	 *
	 * @param url the url
	 * @param method the method (GET or POST)
	 * @return the url connection
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private HttpsURLConnection openUrlConnection(String url, String method) throws IOException {
		URL obj = new URL(url);
		System.out.println("Sending '" + method + "' request to URL : " + url);
		
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod(method);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		con.setRequestProperty("Charset", "utf-8");
		
		return con;
	}

	/**
	 * *
	 * Does a POST request to the given URL and passes the session info and the data along.
	 *
	 * @param url Url to make the request to
	 * @param postString Postdata to send along
	 * @return Html String of the result
	 * @throws IOException When there is an error with the request (No internet, etc)
	 */
	public String postRequest(String url, String postString) throws IOException
	{
		// Get Post Data Length
		byte[] postData = postString.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		
		System.out.println("Post Data: " + postString);
		
		// Open Url connection
		HttpsURLConnection con = this.openUrlConnection(url, "POST");
		con.setRequestProperty("Cookie", this.cookiesToString());
		// Set Custom Post Properties
		con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		con.setRequestProperty("Content-Length", Integer.toString( postDataLength ));
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects( false );
		con.setUseCaches( true );
		con.connect();
		
		// Write Post Data
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		wr.flush();
		wr.close();
		
		// Get the response
		String response = this.readUrlConnectionResponse(con);
		
		String headerName = null;
		for (int i=1; (headerName = con.getHeaderFieldKey(i))!=null; i++) {
		 	if (headerName.equals("Set-Cookie")) {                  
		 		String cookie = con.getHeaderField(i);  
		 		System.out.println("New Cookie: " + cookie);
		 	}
		}
		
		return response;
	}	
	
	/**
	 * Gets the url connection response.
	 *
	 * @param con the con
	 * @return the url connection response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String readUrlConnectionResponse(HttpsURLConnection con) throws IOException {
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		System.out.println("Response Code: " + con.getResponseCode());
		
		return response.toString();
	}
	
	/**
	 * Sets the class name.
	 *
	 * @param setClassName the new class name
	 */
	public void setClassName(String setClassName) {
		this.className = setClassName;
	}

}
