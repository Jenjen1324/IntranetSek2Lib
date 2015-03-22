package no.northcode.jens.intranetsek2.timetable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.northcode.jens.intranetsek2.Login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TODO: Auto-generated Javadoc
/**
 * A Term (a.k.a. Semester) contains classes and weeks of timetables
 * @author Jens V.
 *
 */
public class Term {

	/** The name. */
	private String name;
	
	/** The id. */
	private int id;
	
	/** The week. */
	private List<TimetableWeek> week;
	
	/** The classes. */
	private List<StudentClass> classes;
	
	/** The is default. */
	private Boolean isDefault;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the week.
	 *
	 * @return the week
	 */
	public List<TimetableWeek> getWeek() {
		return week;
	}

	/**
	 * Sets the week.
	 *
	 * @param week the new week
	 */
	public void setWeek(List<TimetableWeek> week) {
		this.week = week;
	}

	/**
	 * Gets the classes.
	 *
	 * @return the classes
	 */
	public List<StudentClass> getClasses() {
		return classes;
	}

	/**
	 * Sets the classes.
	 *
	 * @param classes the new classes
	 */
	public void setClasses(List<StudentClass> classes) {
		this.classes = classes;
	}

	/**
	 * Gets the checks if is default.
	 *
	 * @return the checks if is default
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * Sets the checks if is default.
	 *
	 * @param isDefault the new checks if is default
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * *
	 * Defines a Term (Semester).
	 *
	 * @param name The displayname of the Term
	 * @param id The identifier which is also used in the request to get the timetable
	 */
	public Term(String name, int id) {
		this.name = name;
		this.id = id;
		this.isDefault = false;
	}

	/**
	 * *
	 * Fetches all the classes for the given term.
	 *
	 * @param login Login object which stores a current session
	 * @return the classlist
	 * @throws IOException When the webrequest fails
	 */
	public void getClasslist(Login login) throws IOException
	{
		String requestUrl = Login.URL + "/" + login.getSchool() + Login.URL_TIMETABLE;
		
		String html = login.getRequest(requestUrl);
		String ttUrl = Jsoup.parse(html).getElementById("external").attr("src");
		String tthtml = login.getRequest(ttUrl); // TODO: Term selection!!!
		
		Elements options = Jsoup.parse(tthtml).getElementsByAttributeValue("name", "sc").first().getElementsByTag("option");
		for(Element e : options)
		{
			if(!e.text().contains("Bitte wählen..."))
			{
				StudentClass sc = new StudentClass();
				sc.setId(Integer.parseInt(e.attr("value")));
				sc.setName(e.text());
				this.classes.add(sc);
			}
		}
	}
	
	/**
	 * *
	 * Gets the a list of weeks which are available to choose from in the current term.
	 *
	 * @param login Login object which stores a current session
	 * @return the week list
	 * @throws IOException When the webrequest fails
	 */
	public void getWeekList(Login login) throws IOException
	{
		String requestUrl = Login.URL + "/" + login.getSchool() + Login.URL_TIMETABLE;
		
		String html = login.getRequest(requestUrl);
		String ttUrl = Jsoup.parse(html).getElementById("external").attr("src");
		String tthtml = login.getRequest(ttUrl); // TODO: Term selection!!!
		
		Elements options = Jsoup.parse(tthtml).getElementsByAttributeValue("name", "wk").first().getElementsByTag("option");
		for(Element e : options)
		{
			if(!e.text().contains("Bitte wählen..."))
			{
				TimetableWeek week = new TimetableWeek();
				week.setId(Integer.parseInt(e.attr("value")));
				String split[] = e.text().split(" ");
				week.setStartDate(split[0]);
				week.setEndDate(split[2]);
				week.setWeekNumber(Integer.parseInt(split[4].replace(")","")));
			}
		}
	}
	
	/**
	 * *
	 * Gets a list of all Terms available.
	 *
	 * @param login Login object which stores a current session
	 * @return A list of Terms
	 * @throws IOException When the webrequest fails
	 */
	public static List<Term> parseTerms(Login login) throws IOException
	{
		String requestUrl = Login.URL + "/" + login.getSchool() + Login.URL_TIMETABLE;
		
		String html = login.getRequest(requestUrl);
		String ttUrl = Jsoup.parse(html).getElementById("external").attr("src");
		String tthtml = login.getRequest(ttUrl);
		
		List<Term> terms = new ArrayList<Term>();
		
		// Get the options from the selects
		Elements options = Jsoup.parse(tthtml).getElementById("semester").getElementsByTag("option");
		for(Element e : options)
		{
			int id = Integer.parseInt(e.attr("value"));
			String name = e.text();
			Term t = new Term(name, id);
			if(e.hasAttr("selected"))
				t.isDefault = true;
			terms.add(t);
		}
		
		return terms;
	}
}
