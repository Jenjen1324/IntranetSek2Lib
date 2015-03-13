package no.northcode.jens.intranetsek2.timetable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.northcode.jens.intranetsek2.Login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A Term (a.k.a. Semester) contains classes and weeks of timetables
 * @author Jens V.
 *
 */
public class Term {
	public String name;
	public int id;
	public List<TimetableWeek> week;
	public List<StudentClass> classes;
	public Boolean isDefault;
	
	/***
	 * Defines a Term (Semester)
	 * @param name The displayname of the Term
	 * @param id The identifier which is also used in the request to get the timetable
	 */
	public Term(String name, int id) {
		this.name = name;
		this.id = id;
		this.isDefault = false;
	}

	/***
	 * Fetches all the classes for the given term
	 * @param login Login object which stores a current session
	 * @throws IOException When the webrequest fails
	 */
	public void getClasslist(Login login) throws IOException
	{
		String requestUrl = Login.url + "/" + login.school + Login.URL_TIMETABLE;
		
		String html = login.getRequest(requestUrl);
		String ttUrl = Jsoup.parse(html).getElementById("external").attr("src");
		String tthtml = login.getRequest(ttUrl); // TODO: Term selection!!!
		
		Elements options = Jsoup.parse(tthtml).getElementsByAttributeValue("name", "sc").first().getElementsByTag("option");
		for(Element e : options)
		{
			if(!e.text().contains("Bitte wählen..."))
			{
				StudentClass sc = new StudentClass();
				sc.id = Integer.parseInt(e.attr("value"));
				sc.name = e.text();
				this.classes.add(sc);
			}
		}
	}
	
	/***
	 * Gets the a list of weeks which are available to choose from in the current term
	 * @param login Login object which stores a current session
	 * @throws IOException When the webrequest fails
	 */
	public void getWeekList(Login login) throws IOException
	{
		String requestUrl = Login.url + "/" + login.school + Login.URL_TIMETABLE;
		
		String html = login.getRequest(requestUrl);
		String ttUrl = Jsoup.parse(html).getElementById("external").attr("src");
		String tthtml = login.getRequest(ttUrl); // TODO: Term selection!!!
		
		Elements options = Jsoup.parse(tthtml).getElementsByAttributeValue("name", "wk").first().getElementsByTag("option");
		for(Element e : options)
		{
			if(!e.text().contains("Bitte wählen..."))
			{
				TimetableWeek week = new TimetableWeek();
				week.id = Integer.parseInt(e.attr("value"));
				String split[] = e.text().split(" ");
				week.startDate = split[0];
				week.endDate = split[2];
				week.weekNumber = Integer.parseInt(split[4].replace(")",""));
			}
		}
	}
	
	/***
	 * Gets a list of all Terms available
	 * @param login Login object which stores a current session
	 * @return A list of Terms
	 * @throws IOException When the webrequest fails
	 */
	public static List<Term> parseTerms(Login login) throws IOException
	{
		String requestUrl = Login.url + "/" + login.school + Login.URL_TIMETABLE;
		
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
