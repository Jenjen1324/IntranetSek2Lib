package no.northcode.jens.intranetsek2.timetable;

/**
 * A single lesson in a cell
 * @author Jens V.
 *
 */
public class Lesson {

	public String name;
	public String location;
	public String teacherInitials;
	public String toolTip;
	public String killMessage;
	public LessonStatus status;
	public LessonType type;
	
	public enum LessonStatus {
		normal,
		moved,
		movedTarget,
		dropped
	}
	
	public enum LessonType {
		normal,
		holiday,
		instrument
	}
	
	public Lesson()
	{
		this.name = "";
		this.location = "";
		this.teacherInitials = "";
		this.toolTip = "";
		this.status = LessonStatus.normal;
		this.type = LessonType.normal;
	}
	
}
