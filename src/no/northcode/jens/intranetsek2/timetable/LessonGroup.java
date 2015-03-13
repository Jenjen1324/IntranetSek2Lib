package no.northcode.jens.intranetsek2.timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * List of lessons in a certain cell on the timetable
 * @author Jens V.
 *
 */
public class LessonGroup {
	
	public Date startTime;
	public Date endTime;
	
	public List<Lesson> lessons;
	
	public LessonGroup()
	{
		lessons = new ArrayList<Lesson>();
	}
	
}
