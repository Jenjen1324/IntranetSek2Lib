package no.northcode.jens.intranetsek2.timetable;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import no.northcode.jens.intranetsek2.Login;
import no.northcode.jens.intranetsek2.timetable.Lesson.LessonStatus;
import no.northcode.jens.intranetsek2.timetable.Lesson.LessonType;

// TODO: Auto-generated Javadoc
/**
 * A class (group of students).
 *
 * @author Jens V.
 */
public class StudentClass {

	/** The name. */
	private String name;
	
	/** The id. */
	private int id;
	
	/** The lessons. */
	private List<LessonGroup> lessons;
	
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
	 * Gets the lessons.
	 *
	 * @return the lessons
	 */
	public List<LessonGroup> getLessons() {
		return lessons;
	}

	/**
	 * Sets the lessons.
	 *
	 * @param lessons the new lessons
	 */
	public void setLessons(List<LessonGroup> lessons) {
		this.lessons = lessons;
	}
	
	/**
	 * Instantiates a new student class.
	 */
	public StudentClass()
	{
		this.lessons = new ArrayList<LessonGroup>();
	}
	
	/**
	 * *
	 * Parses a timetable of the given week.
	 *
	 * @param login Login object of a running session
	 * @param week Integer of which week to choose
	 * @throws IOException When the webrequest fails
	 */
	public void parseWeek(Login login, int week) throws IOException
	{
		// Build the request parameters
		String requestUrl = Login.URL + "/" + login.getSchool() + Login.URL_TIMETABLE;
		String postParameter = "sc=" + this.id + "&wk=" + week;
		
		// Get the timetable Page
		String html = login.getRequest(requestUrl);
		String ttUrl = Jsoup.parse(html).getElementById("external").attr("src");
		String tthtml = login.postRequest(ttUrl, postParameter);
		
		// TODO: Remove that weeks lessons
		
		// Parse the timetable
		Elements rows = Jsoup.parse(tthtml).getElementsByTag("tbody").first().getElementsByTag("tr");
		
		// Initialize required index vars
		int rowCount = rows.size();
		int skip[] = new int[rowCount];
		String dates[] = new String[5];
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		
		for(Element row : rows)
		{
			// Get dates
			Elements headings = row.getElementsByTag("th");
			if(headings.size() > 1)
			{
				int headingIndex = 0;
				for(Element cell : headings)
				{
					if(!cell.attr("class").equals("lesson-col"))
					{
						dates[headingIndex] = cell.html().split("<br>")[1];
						headingIndex++;
					}
				}
			}
			else
			{
				// Parse the time
				String times[] = row.getElementsByTag("th").first()
						.text().split("–");
				Elements cells = row.getElementsByTag("td");
				
				// Loop through the different lessons
				int cellIndex = 0;
				for(Element cell : cells)
				{
					LessonGroup lg = new LessonGroup();
					
					// Adapt the cellIndex if there was a multilesson above
					while(skip[cellIndex] > 0)
					{
						skip[cellIndex]--;
						cellIndex++;
					}
					
					// This shouldn't be happening but does and the result apparently is still correct... dafuq?
					if(cellIndex < dates.length)
					{
						// Set the time for the lessongroup
						String startDate = dates[cellIndex] + " " + times[0];
						String endDate = dates[cellIndex] + " " + times[1]; // TODO: If multicell, set time to end of lesson
						try
						{
							lg.setStartTime(format.parse(startDate));
							lg.setEndTime(format.parse(endDate));
							
							// Check if multicell and add the skip value
							if(cell.hasAttr("rowspan"))
							{
								skip[cellIndex] += Integer.parseInt(cell.attr("rowspan"));
							}
							
							// Normal cell
							if(cell.attr("class").contains("middle"))
							{
								String lessonsString[] = cell.html().split("<br clear=\"all\">");
								int lessonCount = lessonsString.length;
								
								// Check if it has that fancy idiotic lesson type (instrument)
								if(cell.attr("class").contains("instrument"))
								{
									String instLesson = lessonsString[lessonCount - 1];
									Document instDoc = Jsoup.parseBodyFragment(instLesson);
									for(Element e : instDoc.getElementsByTag("span"))
									{
										String split[] = e.html().split("<br>");
										Lesson l = new Lesson();
										l.setName(split[0]);
										l.setLocation(split[1]);
										l.setType(LessonType.instrument);
										// TODO: Find out if lesson status can change!
										lg.getLessons().add(l);
									}
									lessonCount--;
								}
								
								// Iterate through the different lessons
								for(int i = 0; i < lessonCount; i++)
								{
									Lesson l = new Lesson();
									
									Document fragment = Jsoup.parseBodyFragment(lessonsString[i]);
									Elements spans = fragment.getElementsByTag("span");
									if(spans.first().attr("class").contains("span"))
									{
										// Making every field optional as sometimes some of the values are missing but it's still a valid lesson
										try { l.setName(spans.get(0).getElementsByTag("strong").first().text()); } catch (Exception e) { l.setName("Unknown"); }
										try { l.setTeacherInitials(spans.get(0).html().split("&nbsp;")[1]); } catch (Exception e) { }
										try { l.setToolTip(spans.get(0).attr("onmouseover").replace("tooltip('", "").replace("');", "")); } catch (Exception e) { }
										try { l.setLocation(spans.get(1).text()); } catch (Exception e) { }
										try {
											Element s = spans.get(0);
											if(s.attr("class").contains("killfree"))
												l.setStatus(LessonStatus.dropped);
											else if(s.attr("class").contains("killmoved"))
												l.setStatus(LessonStatus.moved);
											else if(s.attr("class").contains("substsubst"))
												l.setStatus(LessonStatus.movedTarget);
										} catch (Exception e) { }
										
										// Find out what status the lesson has
										try	{
											if(!l.getStatus().equals(LessonStatus.normal))
											{
												if(i < (lessonCount - 1))
												{
													Element nextSpan = Jsoup.parseBodyFragment(lessonsString[i+1]).getElementsByTag("span").first();
													if(nextSpan.attr("class").contains("comment"))
													{
														l.setKillMessage(nextSpan.text());
													}
												}
											}
										} catch (Exception e) { };
										lg.getLessons().add(l);
									}
								}
								this.lessons.add(lg);
							}
							// Block lesson type (usually spans over multiple cells)
							else if(cell.attr("class").equals("empty dayoff")) 
							{
								Lesson l = new Lesson();
								l.setName(cell.text());
								l.setType(LessonType.holiday);
								lg.getLessons().add(l);
								this.lessons.add(lg);
							}
						}
						catch(ParseException ex)
						{
							System.out.println("Failed to parse time of cell");
						}
					}
					cellIndex++;
				}
			}
		}
	}

	
	
}
