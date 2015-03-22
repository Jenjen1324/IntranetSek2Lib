package no.northcode.jens.intranetsek2.timetable;

// TODO: Auto-generated Javadoc
/**
 * *
 * Week the choose from the week select.
 *
 * @author Jens V.
 */
public class TimetableWeek {
	
	/** The id. */
	private int id;
	
	/** The start date. */
	private String startDate;
	
	/** The end date. */
	private String endDate;
	
	/** The week number. */
	private int weekNumber;
	
	/**
	 * Instantiates a new timetable week.
	 *
	 * @param id the id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param weekNumber the week number
	 */
	public TimetableWeek(int id, String startDate, String endDate,
			int weekNumber) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.weekNumber = weekNumber;
	}
	
	/**
	 * Instantiates a new timetable week.
	 */
	public TimetableWeek(){
		super();
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
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public String getStartDate() {
		return startDate;
	}
	
	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public String getEndDate() {
		return endDate;
	}
	
	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Gets the week number.
	 *
	 * @return the week number
	 */
	public int getWeekNumber() {
		return weekNumber;
	}
	
	/**
	 * Sets the week number.
	 *
	 * @param weekNumber the new week number
	 */
	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TimetableWeek [id=" + id + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", weekNumber=" + weekNumber + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + weekNumber;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimetableWeek other = (TimetableWeek) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id != other.id)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (weekNumber != other.weekNumber)
			return false;
		return true;
	}
	
}
