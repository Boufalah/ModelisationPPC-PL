package timetabling;

import org.chocosolver.solver.Solver;

public class Data {
	
	int weeks;
	
	int days;
	
	int timesOfDay;
	
	int courses;
	
	int lectures;

	public Data(int weeks, int days, int timeslots, int courses, int lectures) {
		super();
		this.weeks = weeks;
		this.days = days;
		this.timesOfDay = timeslots;
		this.courses = courses;
		this.lectures = lectures;
	}
}
