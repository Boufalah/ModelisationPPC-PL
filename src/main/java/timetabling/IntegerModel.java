package timetabling;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class IntegerModel {
	Data data;

	public IntegerModel(Data data) {
		this.data = data;
	}

	public static void main(String[] args) {
		IntegerModel m =new IntegerModel( new Data(2, 3, 2, 3, 2));
		
		Model model = new Model("Timetabling problem");
		
		IntVar[][] W = new IntVar[m.data.courses][m.data.lectures];
		IntVar[][] D = new IntVar[m.data.courses][m.data.lectures];
		IntVar[][] T = new IntVar[m.data.courses][m.data.lectures];
		
		for(int i = 0; i < m.data.courses; i++) {
			for(int j = 0; j < m.data.lectures; j++) {
				W[i][j] = model.intVar("W"+i+","+j ,0, m.data.weeks-1, false);
				D[i][j] = model.intVar("D"+i+","+j, 0, m.data.days-1, false);
				T[i][j] = model.intVar("T"+i+","+j, 0, m.data.timesOfDay-1, false);
			}
		}
		
		int size = m.data.courses * m.data.lectures;
		
		IntVar[] timeslots;
		timeslots = model.intVarArray("T", size, 1, m.data.weeks*m.data.days*m.data.timesOfDay, false);
		
		for(int i = 0; i < m.data.courses; i++) {
			for(int j = 0; j < m.data.lectures; j++) {
				timeslots[i*m.data.lectures+j].eq(
						W[i][j].mul(m.data.days*m.data.timesOfDay).add(D[i][j].mul(m.data.timesOfDay)).add(T[i][j])).post();
			}
		}
		
		model.allDifferent(timeslots).post();
		
		Solver solver = model.getSolver();
		Solution solution = solver.findSolution();
		
		if(solution != null) {
			//System.out.println(solution.toString());
			m.printSolution(W,D,T);
			/*
			for(int i = 0; i < size; i++) {
				System.out.println(timeslots[i]);
			}*/
		}
	}
	public void printSolution(IntVar[][] W,IntVar[][] D,IntVar[][] T){
		for (int i = 0; i< data.courses; i++) {
			for (int j= 0 ;j< data.lectures;j++){
				System.out.println("Course "+i+" Lecture "+j+" Week "+W[i][j].getValue()+ " Day "+D[i][j].getValue()+" T "+T[i][j].getValue() );
			}
		}
	}
}
