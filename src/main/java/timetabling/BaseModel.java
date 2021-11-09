package timetabling;

public class BaseModel {
	
	int weeks;
	
	int days;
	
	int timeslots;
	
	int courses;
	
	int lectures;

	public BaseModel(int weeks, int days, int timeslots, int courses, int lectures) {
		super();
		this.weeks = weeks;
		this.days = days;
		this.timeslots = timeslots;
		this.courses = courses;
		this.lectures = lectures;
	}
}
