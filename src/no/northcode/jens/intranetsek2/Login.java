package no.northcode.jens.intranetsek2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

// TODO: Auto-generated Javadoc
/**
 * *
 * Logs the user in and stores the login session.
 *
 * @author Jens V.
 */
public class Login {



	/** The school. */
	private String school;
	
	/** The _class. */
	private String _class;

	/** The news. */
	private List<News> news;
	
	/** The username. */
	public String username;
	
	/** The password. */
	private String password;
	
	/** The sessionid. */
	private String sessionid;
	
	/**
	 * Gets the _class.
	 *
	 * @return the _class
	 */
	public String get_class() {
		return _class;
	}

	/**
	 * Sets the _class.
	 *
	 * @param _class the new _class
	 */
	public void set_class(String _class) {
		this._class = _class;
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
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the news.
	 *
	 * @param news the new news
	 */
	public void setNews(List<News> news) {
		this.news = news;
	}
	
	/**
	 * Gets the news.
	 *
	 * @return the news
	 */
	public List<News> getNews() {
		return news;
	}
	
	//private static final String domain = "intranet.tam.ch";
	/** The Constant URL. */
	public static final String URL = "https://intranet.tam.ch";
	
	/** The Constant URL_NEWS. */
	public static final String URL_NEWS = "/index/all-appointments";
	
	/** The Constant URL_TIMETABLE. */
	public static final String URL_TIMETABLE = "/external/index/act/tt_oneclassNew";
	//private static final String URL_LIST_CLASS = "/list/index/list/45";
	
	/**
	 * *
	 * Logs the user in.
	 *
	 * @param username the username
	 * @param password the password
	 * @param school the school
	 * @throws IOException When the request fails
	 * @throws LoginException When the user can't log in (e.g. wrong credentials)
	 */
	public Login(String username, String password, String school) throws IOException, LoginException
	{
		this.username = username;
		this.password = password;
		this.school = school; // TODO: Change to School obj
		
		Boolean success = false;
		
		// Post request parameters
		
		StringBuilder builder = new StringBuilder();
		builder.append("loginuser=").append(this.username).append("&loginpassword=").append(this.password).append("&loginschool=").append(this.school);
		String urlParameters = builder.toString();
		
		byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + urlParameters);
				
		// WebRequest
		HttpsURLConnection con = this.getUrlConnection(URL, "POST");
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
		
		System.out.println("Response Code : " + con.getResponseCode());
 
		this.getUrlConnectionResponse(con);
		
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
			throw new LoginException("Login failed");
	}
	
	/**
	 * *
	 * Fetches the news of the frontpage.
	 *
	 * @return A list of News objects
	 * @throws IOException When the WebRequest fails
	 */
	public List<News> fetchNews() throws IOException
	{
		String requestUrl = URL + "/" + school + URL_NEWS;
		String response = getRequest(requestUrl);
		
		return News.parseNews(response);
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
		HttpsURLConnection con = this.getUrlConnection(url, "POST");
		// Set Custom Post Properties
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
		String response = this.getUrlConnectionResponse(con);
		
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
	 * *
	 * Does a GET request to the given URL and passes the session info along.
	 *
	 * @param url Url to make the request to
	 * @return Html String of the result
	 * @throws IOException When there is an error with the request (No internet, etc)
	 */
	public String getRequest(String url) throws IOException
	{
		HttpsURLConnection con = this.getUrlConnection(url, "GET");
		con.connect();
		return this.getUrlConnectionResponse(con);
	}	
	
	private HttpsURLConnection getUrlConnection(String url, String method) throws IOException {
		URL obj = new URL(url);
		System.out.println("Sending '" + method + "' request to URL : " + url);

		StringBuilder builder = new StringBuilder();
		builder.append("sturmsession=").append(this.sessionid).append("; sturmuser=").append(this.username);
		String cookies = builder.toString();
		
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		con.setRequestProperty("Charset", "utf-8");
		con.setRequestProperty("Cookie", cookies);
		
		return con;
	}
	
	private String getUrlConnectionResponse(HttpsURLConnection con) throws IOException {
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

}
