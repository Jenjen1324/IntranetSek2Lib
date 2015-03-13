package no.northcode.jens.intranetsek2;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * News item from the newsfeed on the page
 * @author Jens V.
 *
 */
public class News {

	// public ? fromDate
	// public ? toDate
	
	public String date;
	public String text;
	public String location;
	
	/***
	 * An object which stores a news entry
	 * @param date When the news post was issued
	 * @param text Content of the post
	 * @param location Where the event is (optional)
	 */
	private News(String date, String text, String location)
	{
		this.date = date;
		this.text = text;
		this.location = location;
	}
	
	/***
	 * Parses the news page and converts them to News objects
	 * @param html Source of the news page
	 * @return List of News objects
	 */
	public static List<News> parseNews(String html)
	{
		List<News> news = new ArrayList<News>();
		
		// Parse the document
		Document doc = Jsoup.parse(html);
		Elements elements = doc.getElementById("content").getElementsByTag("li");
		
		for(Element element : elements)
		{
			// Extract the different values from the DOM Elements
			String date = element.getElementsByTag("h3").first().text();
			String location = element.getElementsByTag("div").first().text();
			String text = element.text().replace(date, "").replace(location, "");
			
			News n = new News(date, text, location);
			news.add(n);
		}
		
		return news;
	}
	
}
