package no.northcode.jens.intranetsek2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TODO: Auto-generated Javadoc
/**
 * A school where the user logs on to.
 *
 * @author Jens V.
 */
public class School {

	/** The name. */
	private String name;
	
	/** The id. */
	private String id;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Instantiates a new school.
	 *
	 * @param id the id
	 * @param name the name
	 */
	public School(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Instantiates a new school.
	 *
	 * @param id the id
	 */
	public School(String id)
	{
		this.id = id;
	}
	
	/**
	 * *
	 * Gets the list of schools of which you can choose
	 * Currently fetches them from the shiboleet page because I didn't bother looking for where the list is loaded on the frontpage.
	 *
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
				School s = new School(e.text(), e.attr("value"));
				schools.add(s);
			}
		}
		
		return schools;
	}
}