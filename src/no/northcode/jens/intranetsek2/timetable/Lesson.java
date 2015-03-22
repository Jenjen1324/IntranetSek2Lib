package no.northcode.jens.intranetsek2.timetable;

// TODO: Auto-generated Javadoc
/**
 * A single lesson in a cell.
 *
 * @author Jens V.
 */
public class Lesson {

	/** The name. */
	private String name;
	
	/** The location. */
	private String location;
	
	/** The teacher initials. */
	private String teacherInitials;
	
	/** The tool tip. */
	private String toolTip;
	
	/** The kill message. */
	private String killMessage;
	
	/** The status. */
	private LessonStatus status;
	
	/** The type. */
	private LessonType type;
	
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
	 * Gets the location.
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the teacher initials.
	 *
	 * @return the teacher initials
	 */
	public String getTeacherInitials() {
		return teacherInitials;
	}

	/**
	 * Sets the teacher initials.
	 *
	 * @param teacherInitials the new teacher initials
	 */
	public void setTeacherInitials(String teacherInitials) {
		this.teacherInitials = teacherInitials;
	}

	/**
	 * Gets the tool tip.
	 *
	 * @return the tool tip
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * Sets the tool tip.
	 *
	 * @param toolTip the new tool tip
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * Gets the kill message.
	 *
	 * @return the kill message
	 */
	public String getKillMessage() {
		return killMessage;
	}

	/**
	 * Sets the kill message.
	 *
	 * @param killMessage the new kill message
	 */
	public void setKillMessage(String killMessage) {
		this.killMessage = killMessage;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public LessonStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(LessonStatus status) {
		this.status = status;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public LessonType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(LessonType type) {
		this.type = type;
	}

	
	/**
	 * The Enum LessonStatus.
	 */
	public enum LessonStatus {
		
		/** The normal. */
		normal,
		
		/** The moved. */
		moved,
		
		/** The moved target. */
		movedTarget,
		
		/** The dropped. */
		dropped
	}
	
	/**
	 * The Enum LessonType.
	 */
	public enum LessonType {
		
		/** The normal. */
		normal,
		
		/** The holiday. */
		holiday,
		
		/** The instrument. */
		instrument
	}
	
	/**
	 * Instantiates a new lesson.
	 */
	public Lesson()
	{
		this.name = "?";
		this.location = "";
		this.teacherInitials = "";
		this.toolTip = "";
		this.status = LessonStatus.normal;
		this.type = LessonType.normal;
	}
	
}
