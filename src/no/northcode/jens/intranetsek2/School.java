package no.northcode.jens.intranetsek2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A school where the user logs on to
 * @author Jens V.
 *
 */
public class School {

	public String name;
	public String id;
	
	/***
	 * Gets the list of schools of which you can choose
	 * Currently fetches them from the shiboleet page because I didn't bother looking for where the list is loaded on the frontpage
	 * @return A list of schools to choose from
	 * @throws IOException When the download of the page fails
	 */
	public static List<School> getSchoolList() throws IOException
	{
		List<School> schools = new ArrayList<School>();
		
		Document doc = Jsoup.connect("https://aai.tam.ch/idp/Authn/UserPassword").get();
		Elements elements = doc.getElementsByAttributeValue("name", "loginschool").first().getElementsByTag("option");
		
		for(Element e : elements)
		{
			if(!e.attr("value").equals("-1"))
			{
				School s = new School();
				s.name = e.text();
				s.id = e.attr("value");
				schools.add(s);
			}
		}
		
		return schools;
	}
}