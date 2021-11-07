package timetabling;

import java.util.Random;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class FirstModel {
	
	static int maxNumberOfLectures = 5; // Per course
	
	static int maxNumberOfAvailableRooms = 5; // Per period
	
	// Number of courses
	static int q = 5;
	// Number of lectures for each course
	static int[] k = new int[q];
	
	// Number of curricula
	static int r = 5;
	// Groups of courses
	static int[][] s = new int[r][];
	
	// Number of periods
	static int p = 5;
	// Number of available rooms for each period
	static int[] l = new int[p];
	
	public static void main(String[] args) {
		Model model = new Model("Course timetabling problem");
		
		Random r = new Random();
		
		int totalLectures;
		int totalSlots;
		
		do {
			totalLectures = 0;
			totalSlots = 0;
			
			initialize(r);

			for(int i = 0; i < q; i++) {
				totalLectures += k[i];
			}

			for(int i = 0; i < p; i++) {
				totalSlots += l[i];
			}
		} while (totalLectures > totalSlots);
		
		IntVar[] lectures = new IntVar[totalLectures];
		
		for(int i = 0; i < totalLectures; i++) {
			lectures[i] = model.intVar("L_"+i, 1, p);
		}
		
		for(int i = 1; i <= p; i++) {
			IntVar cap = model.intVar("cap_"+i, 0, l[i-1]);
			model.count(i, lectures, cap).post();
		}
		
		Solver solver = model.getSolver();
		Solution solution = solver.findSolution();
		
		if(solution != null){
			System.out.println("Total lectures to schedule : "+totalLectures+"\n");
			int totalLecturesScheduled = 0;
		    for(int i = 1; i <= p; i++) {
		    	int x = 0;
		    	for(int j = 0; j < totalLectures; j++) {
		    		if(lectures[j].getValue() == i) {
		    			x += 1;
		    		}
		    	}
		    	totalLecturesScheduled += x;
		    	System.out.println("Period #"+i+" : "+l[i-1]+" rooms available");
		    	System.out.println("Lectures scheduled : "+x);
		    }
		    System.out.println("\nTotal lectures scheduled : "+totalLecturesScheduled);
		}
		else {
			System.out.println("No solution found.");
		}
	}

	private static void initialize(Random r) {
		for(int i = 0; i < q; i++) {
			k[i] = r.nextInt(maxNumberOfLectures) + 1;
		}
		
		for(int i = 0; i < p; i++) {
			l[i] = r.nextInt(maxNumberOfAvailableRooms) + 1;
		}
	}
	
	
	

}
