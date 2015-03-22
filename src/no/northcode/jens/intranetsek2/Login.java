package no.northcode.jens.intranetsek2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import no.northcode.jens.intranetsek2.timetable.Term;

// TODO: Auto-generated Javadoc
/**
 * *
 * Logs the user in and stores the login session.
 *
 * @author Jens V.
 */
public class Login {

	/** The school. */
	public String school;
	
	/** The _class. */
	public String _class;
	
	/** The terms. */
	public List<Term> terms;
	
	/** The default term. */
	public Term defaultTerm;
	
	/** The news. */
	public List<News> news;
	
	/** The username. */
	public String username;
	
	/** The password. */
	private String password;
	
	/** The sessionid. */
	private String sessionid;
	
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
		
		/*
		StringBuilder builder = new StringBuilder();
		builder.append("loginuser=").append(this.username).append("&loginpassword=").append(this.password).append("&loginschool=").append(this.school);
		
		String urlParametersTemp = builder.toString();
		*/
		
		String urlParameters = "loginuser=" + 
				this.username + 
				"&loginpassword=" + 
				this.password + 
				"&loginschool=" + 
				this.school;
		byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		
		System.out.println("\nSending 'POST' request to URL : " + URL);
		System.out.println("Post parameters : " + urlParameters);
		
		URL obj = null;
		try {
			obj = new URL(URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// WebRequest
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty( "charset", "utf-8");
		con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects( false );
		con.setUseCaches( false );
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		wr.flush();
		wr.close();
		
		System.out.println("Response Code : " + con.getResponseCode());
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String headerName = null;
		for (int i=1; (headerName = con.getHeaderFieldKey(i))!=null; i++) {
		 	if (headerName.equals("Set-Cookie")) {                  
		 		String cookie = con.getHeaderField(i);  
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
		
		//print result
		System.out.println(response.toString());
 
		
	}
	
	/**
	 * *
	 * Fetches the news of the frontpage.
	 *
	 * @return A list of News objects
	 * @throws IOException When the WebRequest fails
	 */
	public List<News> getNews() throws IOException
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
		
		//TODO: user StringBuilder
		String cookies = "sturmuser=" + this.username + "; sturmsession=" + this.sessionid;
		byte[] postData = postString.getBytes(Charset.forName("UTF-8"));
		int postDataLength = postData.length;
		
		URL obj = new URL(url);
		System.out.println("Sending 'POST' request to URL : " + url);
		
		
		//TODO: code duplication
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty( "charset", "utf-8");
		con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		con.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		con.setRequestProperty("Cookie", cookies);
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setInstanceFollowRedirects( false );
		con.setUseCaches( false );
		con.connect();
		
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(postData);
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		System.out.println("Post parameters : " + postString);
		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
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
		//TODO: user StringBuilder
		String cookies = "sturmuser=" + this.username + "; sturmsession=" + this.sessionid;
		
		URL obj = new URL(url);
		System.out.println("Sending 'GET' request to URL : " + url);
		
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty( "charset", "utf-8");
		con.setRequestProperty("Content-Type", "text/html");
		con.setRequestProperty("Cookie", cookies);
		con.connect();
		
		System.out.println("Response Code: " + con.getResponseCode());
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}	
	
	public static String buildParameters(Map<String,String> parameters)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		
		for(Map.Entry<String, String> entry : parameters.entrySet())
		{
			if(!first)
				sb.append("&");
			else
				first = false;
			
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
		}
		
		
		return sb.toString();
		
	}
}
