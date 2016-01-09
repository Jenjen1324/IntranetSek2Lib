package no.northcode.jens.intranetsek2;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

// TODO: Auto-generated Javadoc
/**
 * The Class Lesson.
 */
public class Lesson {

	private static String zoneId = "Europe/Zurich";
	
	/**
	 * The Enum LessonType.
	 */
	public enum LessonType {
		
		/** Normal lesson. */
		lesson,
		
		/** Holidays. */
		holiday,
		
		/** Overwritten lesson (yellow) */
		block,
		
		/** Canceled lesson. */
		cancel
	}
	
	/**
	 * Gets the lesson by day.
	 *
	 * @param login the login
	 * @param day the day
	 * @return the lesson by day
	 * @throws IOException If the webrequest fails
	 */
	public static ArrayList<Lesson> getLessonByDay(Login login, LocalDate day) throws IOException {
		return Lesson.getLessons(login, day, 1);
	
	}

	/**
	 * Gets the lesson by week.
	 *
	 * @param login the login
	 * @param day the day
	 * @return the lesson by week
	 * @throws IOException If the webrequest fails
	 */
	public static ArrayList<Lesson> getLessonByWeek(Login login, LocalDate day) throws IOException {
		return Lesson.getLessons(login, day, 7);
	}
	
	/**
	 * Gets the lessons.
	 *
	 * @param login the login
	 * @param day the first day
	 * @param days amount of days to cover
	 * @return the lessons
	 * @throws IOException If the webrequest fails
	 */
	public static ArrayList<Lesson> getLessons(Login login, LocalDate day, int days) throws IOException {
		LocalDateTime start = day.atStartOfDay();
		LocalDateTime end = start.plusDays(1);
		return getLessons(login, start, end);
	
	}
	
	/**
	 * Gets the lessons.
	 *
	 * @param login the login
	 * @param startTime the start time
	 * @param endTime the end time
	 * @return the lessons
	 * @throws IOException If the webrequest fails
	 */
	public static ArrayList<Lesson> getLessons(Login login, LocalDateTime startTime, LocalDateTime endTime) throws IOException {
		ZoneId zoneId = ZoneId.systemDefault();
		long start = startTime.atZone(zoneId).toEpochSecond() * 1000;
		long end = startTime.atZone(zoneId).toEpochSecond() * 1000;
		
		StringBuilder sb = new StringBuilder();
		sb.append("startDate=").append(start).append("&endDate=").append(end).append("&studentId%5B%5D=").append(login.getUserid()).append("&holidaysOnly=0");
		String postPara = sb.toString();
		String result = login.postRequest(login.createInternUrl(Login.URL_TIMETABLE), postPara);
		System.out.println(result);
		return Lesson.parseLessons(result);
	}
	
	/**
	 * Parses the lessons.
	 *
	 * @param json the json
	 * @return the array list
	 */
	public static ArrayList<Lesson> parseLessons(String json) {
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
		JSONArray data = JsonPath.read(document, "$.data[*]");
		for(Object lessonobj : data) {
			@SuppressWarnings("unchecked")
			HashMap<Object, Object> lessondata = (HashMap<Object, Object>) lessonobj;
			
			Lesson l = new Lesson();
			l.id = (Integer) lessondata.get("id");
			l.startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(((String) lessondata.get("start")).substring(6, 19))),  ZoneId.of(Lesson.zoneId));
			l.endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(((String) lessondata.get("end")).substring(6, 19))),  ZoneId.of(Lesson.zoneId));
			l.type = (String) lessondata.get("timetableEntryTypeShort");
			l.title = (String) lessondata.get("title");
			l.courseName = (String) lessondata.get("courseName");
			l.course = (String) lessondata.get("course");
			l.subjectId = (Integer) lessondata.get("courseId");
			l.subjectName = (String) lessondata.get("subjectName");
			//l.roomId = (Integer) 
			l.roomName = (String) lessondata.get("roomName");
			l.message = (String) lessondata.get("message");
			lessons.add(l);
		}
		
		return lessons;
	}
	
	/** The id. */
	private int id;
	
	/** The lesson starttime. */
	private LocalDateTime startTime;
	
	/** The lesson endtime. */
	private LocalDateTime endTime;
	
	/** The type of lesson. 
	 * E.g. if it has been canceled or not
	 * */
	//private Object type;
	private String type; // For now until I get every case enumerated
	
	/** The title. 
	 * The name you see in the intranet
	 * */
	private String title;
	
	/** The course name. 
	 * Short name of the course
	 * */
	private String courseName;
	
	/** The course. 
	 * Full name of the course
	 * */
	private String course;
	
	/** The subject id. */
	private int subjectId;

	/** The subject name. 
	 * Full name of the subject
	 * */
	private String subjectName;

	/** The room id. */
	private int roomId;

	/** The room name. */
	private String roomName;

	/** The message. 
	 * Additional information message
	 * */
	private String message;

	/**
	 * Instantiates a new lesson.
	 */
	private Lesson() { }

	/**
	 * Gets the course.
	 *
	 * @return the course
	 */
	public String getCourse() {
		return course;
	}

	/**
	 * Gets the course name.
	 *
	 * @return the course name
	 */
	public String getCourseName() {
		return courseName;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public LocalDateTime getEndTime() {
		return endTime;
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
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the room id.
	 *
	 * @return the room id
	 */
	public int getRoomId() {
		return roomId;
	}

	
	/**
	 * Gets the room name.
	 *
	 * @return the room name
	 */
	public String getRoomName() {
		return roomName;
	}
	
	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	/**
	 * Gets the subject id.
	 *
	 * @return the subject id
	 */
	public int getSubjectId() {
		return subjectId;
	}
	
	/**
	 * Gets the subject name.
	 *
	 * @return the subject name
	 */
	public String getSubjectName() {
		return subjectName;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb
			.append(this.type).append(" ")
			.append(this.startTime.getHour()).append(":").append(this.startTime.getMinute()).append(" - ")
			.append(this.endTime.getHour()).append(":").append(this.endTime.getMinute()).append(": ")
			.append(" ").append(this.title).append(" ZI: ").append(this.roomName);
		
		return sb.toString();
	}
	
}
