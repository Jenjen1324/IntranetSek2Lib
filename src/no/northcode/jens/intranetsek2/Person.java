package no.northcode.jens.intranetsek2;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A person entity (from a student-/teacher-/classlist)
 * @author Jens V.
 *
 */
public class Person {
	
	// TODO: private members, generate getters and setters
	public String name;
	public String surname;
	public String phone;
	public String email;
	public String address;
	public String pcode;
	public String place;
	
	public PersonType type;
	
	public String _class;
	public String subject;
	
	public enum PersonType {
		teacher,
		student
	}
	
	public static List<Person> parseFromStudentList(String html)
	{
		List<Person> persons = new ArrayList<Person>();
		
		Document doc = Jsoup.parse(html);
		Elements rows = doc.getElementById("listen").getElementsByTag("tr");
		for(Element row : rows)
		{
			Person p = new Person();
			Elements cells = row.getElementsByTag("td");
			p._class  = cells.get(0).text();
			p.name    = cells.get(1).text();
			p.surname = cells.get(2).text();
			p.phone   = cells.get(3).text();
			p.email   = cells.get(4).text();
			
			persons.add(p);
		}
		
		return persons;
		
	}
}
