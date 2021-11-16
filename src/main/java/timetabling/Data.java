package timetabling;

public class Data {
	
	int weeks;
	
	int days;
	
	int timesOfDay;
	
	int courses;
	
	int lectures;
	
	int minDisInDays;
	
	int maxDisInDays;
	
	int maxDiffDaysForACourse;
	
	int maxWeeksForCourse;

	public Data(int weeks, int days, int timeslots, int courses, int lectures) {
		super();
		this.weeks = weeks;
		this.days = days;
		this.timesOfDay = timeslots;
		this.courses = courses;
		this.lectures = lectures;
	}
}
