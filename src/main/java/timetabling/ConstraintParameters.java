package timetabling;

public class ConstraintParameters {
	
	int minDisInSlot;
	
	int maxDisInDays;
	
	int maxDiffDaysForACourse;
	
	int maxWeeksForCourse;

	public ConstraintParameters(int minDisInDays, int maxDisInDays, int maxDiffDaysForACourse, int maxWeeksForCourse) {
		this.minDisInSlot = minDisInDays;
		this.maxDisInDays = maxDisInDays;
		this.maxDiffDaysForACourse = maxDiffDaysForACourse;
		this.maxWeeksForCourse = maxWeeksForCourse;
	}
}
