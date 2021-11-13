package timetabling;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class IntegerModel {

	public static void main(String[] args) {
		Data data = new Data(2, 3, 2, 3, 2);
		
		Model model = new Model("Timetabling problem");
		
		IntVar[][] W = new IntVar[data.courses][data.lectures];
		IntVar[][] D = new IntVar[data.courses][data.lectures];
		IntVar[][] T = new IntVar[data.courses][data.lectures];
		
		for(int i = 0; i < data.courses; i++) {
			for(int j = 0; j < data.lectures; j++) {
				W[i][j] = model.intVar("W"+i+","+j ,0, data.weeks-1, false);
				D[i][j] = model.intVar("D"+i+","+j, 0, data.days-1, false);
				T[i][j] = model.intVar("T"+i+","+j, 0, data.timesOfDay-1, false);
			}
		}
		
		int size = data.courses * data.lectures;
		
		IntVar[] timeslots;
		timeslots = model.intVarArray("T", size, 1, data.weeks*data.days*data.timesOfDay, false);
		
		for(int i = 0; i < data.courses; i++) {
			for(int j = 0; j < data.lectures; j++) {
				timeslots[i*data.lectures+j].eq(
						W[i][j].mul(data.days*data.timesOfDay).add(D[i][j].mul(data.timesOfDay)).add(T[i][j])).post();
			}
		}
		
		model.allDifferent(timeslots).post();
		
		Solver solver = model.getSolver();
		Solution solution = solver.findSolution();
		
		if(solution != null) {
			System.out.println(solution.toString());
			for(int i = 0; i < size; i++) {
				System.out.println(timeslots[i]);
			}
		}
	}
}
