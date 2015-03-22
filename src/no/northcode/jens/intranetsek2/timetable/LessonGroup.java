package no.northcode.jens.intranetsek2.timetable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * *
 * List of lessons in a certain cell on the timetable.
 *
 * @author Jens V.
 */
public class LessonGroup {
	
	
	/** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
	
	/** The lessons. */
	private List<Lesson> lessons;
	
	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the lessons.
	 *
	 * @return the lessons
	 */
	public List<Lesson> getLessons() {
		return lessons;
	}

	/**
	 * Sets the lessons.
	 *
	 * @param lessons the new lessons
	 */
	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}

	/**
	 * Instantiates a new lesson group.
	 */
	public LessonGroup()
	{
		lessons = new ArrayList<Lesson>();
	}
	
}
