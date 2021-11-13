package timetabling;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

public class IntegerModel {
	Data data;

	public IntegerModel(Data data) {
		this.data = data;
	}

	public static void main(String[] args) {
		IntegerModel m = new IntegerModel( new Data(2, 3, 2, 2, 3));

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
		timeslots = model.intVarArray("T", size,0 , m.data.weeks*m.data.days*m.data.timesOfDay-1, false);
		
		for(int i = 0; i < m.data.courses; i++) {
			for(int j = 0; j < m.data.lectures; j++) {
				timeslots[i*m.data.lectures+j].eq(
						W[i][j].mul(m.data.days*m.data.timesOfDay).add(D[i][j].mul(m.data.timesOfDay)).add(T[i][j])).post();
			}
		}

		model.allDifferent(timeslots).post();

		int timeSlotperWeek = m.data.days * m.data.timesOfDay;
		if (timeSlotperWeek >= m.data.courses && m.data.lectures <= m.data.weeks) {
			System.out.println("Case 1 ");
			for (int i = 0; i < m.data.courses; i++) {
				for (int j = 1; j < m.data.lectures; j++) {
					// lectures of a course can't be in the same week,is in the same day and in the same timeofday
					model.arithm(W[i][j - 1], "<", W[i][j]).post();
					model.arithm(D[i][j - 1], "=", D[i][j]).post();
					model.arithm(T[i][j - 1], "=", T[i][j]).post();
				}
			}
		}

		if(timeSlotperWeek >= m.data.courses && m.data.lectures > m.data.weeks){

			System.out.println("Case 2 ");
			// distance can vary depending on nb of lectures and nb of days
			int dis = 1 ;
			for (int i = 0; i < m.data.courses; i++) {
				for (int j = 1; j < m.data.lectures; j++) {
					// lectures of a course can be in the same week but different day (with a distance),
					ReExpression y1 = W[i][j - 1].eq(W[i][j]).and(D[i][j].sub(D[i][j-1]).gt(dis)) ;
					ReExpression y2 = W[i][j - 1].lt(W[i][j]).and(D[i][j - 1].eq(D[i][j]).and(T[i][j - 1].eq(T[i][j])));
					y1.or(y2).post();
				}
			}
		}

		Solver solver = model.getSolver();
		int i = 1;
		while(solver.solve()){
			System.out.println("Solution n° "+i);
			m.printSolution(W,D,T);
			i++;
			System.out.println();
		}
		/*Solution solution = solver.findSolution();

		if(solution != null) {
			//m.printSolution(W,D,T);
			for (int j = 0; j < size; j++) {
				System.out.println(timeslots[j]);
			}
		}*/
	}
	public void printSolution(IntVar[][] W,IntVar[][] D,IntVar[][] T){
		for (int i = 0; i< data.courses; i++) {
			for (int j= 0 ;j< data.lectures;j++){
				System.out.println("Course "+i+" Lecture "+j+" Week "+W[i][j].getValue()+ " Day "+D[i][j].getValue()+" T "+T[i][j].getValue() );
			}
		}
	}
}
