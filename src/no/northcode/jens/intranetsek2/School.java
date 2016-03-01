package no.northcode.jens.intranetsek2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

// TODO: Auto-generated Javadoc
/**
 * A school where the user logs on to.
 *
 * @author Jens V.
 */
public class School {

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
		
		Document doc = Jsoup.connect("https://intranet.tam.ch").get();
		Elements elements = doc.getElementsByTag("script");
		
		for(Element e : elements)
		{
			if(e.html().contains("Login.schools=")) {
				String[] tmp = e.html().split("Login\\.schools=");
				String json = tmp[1].split(";")[0].toString();
				//System.out.println(json);
				
				JSONArray data = JsonPath.read(json, "$.*.*");
				
				for(Object obj : data) {
					@SuppressWarnings("unchecked")
					HashMap<Object, Object> sdata = (HashMap<Object, Object>) obj;
					School s = new School();
					s.id = (String) sdata.get("short_name");
					s.name = (String) sdata.get("name");
					s.type = (String) sdata.get("type");
					schools.add(s);
				}
				
				break;
			}
		}
		
		return schools;
	}
	
	public static HashMap<String, List<School>> getStructuredList(List<School> schools) {
		HashMap<String, List<School>> map = new HashMap<String, List<School>>();
		for(School s : schools) {
			if(!map.containsKey(s.type)) {
				map.put(s.type, new ArrayList<School>());
			}
			map.get(s.type).add(s);
		}
		
		return map;
	}
	
	public static School findSchoolByName(String name, List<School> schools) {
		for(School s : schools) {
			if(s.name.equals(name))
				return s;
		}
		return null;
	}
	
	/** The name. */
	private String name;
	
	/** The id. */
	private String id;
	
	/** The type. */
	private String type;
	
	/**
	 * Instantiates a new school.
	 */
	private School() {
		
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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}