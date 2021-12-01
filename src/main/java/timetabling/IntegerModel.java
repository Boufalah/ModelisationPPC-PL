package timetabling;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;


public class IntegerModel {
	Data data;

	public IntegerModel(Data data) {
		this.data = data;
	}

	public static void main(String[] args) {
		Data d = new Data(3, 4, 2, 2, 3);
		IntegerModel m = new IntegerModel(d);

		// Declaring model

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

		// Defining constraints

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
//
//
//		// Min/ Max nb of slots (disMin) between lectures of same course
//		for(int i = 0; i < m.data.courses; i++) {
//			for(int j = 1; j < m.data.lectures; j++) {
//				//timeslots[j]-timeslots[j-1] > minDisInSlots
//				timeslots[i*m.data.lectures+j].sub(timeslots[i*m.data.lectures+(j-1)]).gt(m.param.minDisInSlot).post();
//				//timeslots[j]-timeslots[j-1] < maxDisInSlots
//				timeslots[i*m.data.lectures+j].sub(timeslots[i*m.data.lectures+(j-1)]).lt(m.param.maxDisInDays).post();
//			}
//		}
//
//		// Max nb of different days of the week for a course
//		for (int i = 0; i < m.data.courses; i++) {
//			model.nValues(D[i],model.intVar(m.param.maxDiffDaysForACourse)).post();
//		}
//
//		// Max nb of week for a course
//		for (int i = 0; i < m.data.courses; i++) {
//			model.atMostNValues(W[i],model.intVar(m.param.maxWeeksForCourse),true).post();
//		}
//		//the course should be in the same period of day
//		for (int i = 0; i < m.data.courses; i++) {
//			model.allEqual(T[i]).post();
//		}


		/*int timeSlotperWeek = m.data.days * m.data.timesOfDay;

		// One lecture of each course per week
		if (timeSlotperWeek >= m.data.courses && m.data.lectures == m.data.weeks) {
			System.out.println("Case 1 ");
			for (int i = 0; i < m.data.courses; i++) {
				for (int j = 1; j < m.data.lectures; j++) {
					// lectures of a course can't be in the same week, is in the same day and in the same timeofday
					model.arithm(W[i][j - 1], "<", W[i][j]).post();
					model.arithm(D[i][j - 1], "=", D[i][j]).post();
					model.arithm(T[i][j - 1], "=", T[i][j]).post();
				}
			}
		}// regular

		if(timeSlotperWeek >= m.data.courses && m.data.lectures > m.data.weeks){
			System.out.println("Case 2 ");
			// distance can vary depending on nb of lectures and nb of days
			int disD = 1;
			for (int i = 0; i < m.data.courses; i++) {
				for (int j = 1; j < m.data.lectures; j++) {
					// lectures of a course can be in the same week but maximum <disD> days apart
					ReExpression y1 = W[i][j - 1].eq(W[i][j]).and(D[i][j].sub(D[i][j-1]).gt(disD));
					ReExpression y2 = W[i][j - 1].lt(W[i][j]).and(D[i][j - 1].eq(D[i][j]).and(T[i][j - 1].eq(T[i][j])));
					y1.or(y2).post();
				}
			}
		}


		if (timeSlotperWeek >= m.data.courses && m.data.lectures < m.data.weeks) {
			System.out.println("Case 3 ");
			int disW = 1;
			for (int i = 0; i < m.data.courses; i++) {
				for (int j = 1; j < m.data.lectures; j++) {
					// lectures of a course are maximum <disW> weeks apart
					ArExpression diff = W[i][j].sub(W[i][j-1]);
					ReExpression y1 = diff.le(disW).and(diff.gt(0)).and(D[i][j - 1].eq(D[i][j]).and(T[i][j - 1].eq(T[i][j])));
					y1.post();
				}
			}
		}

		IntVar[] flatW = model.intVarArray("F", size,0 , m.data.weeks-1, false);

		for (int i = 0; i < m.data.courses; i++) {
			for (int j = 0; j < m.data.lectures; j++) {
				flatW[i*m.data.lectures+j].eq(W[i][j]).post();
			}
		}

		// No empty week
		IntVar diffValues = model.intVar(m.data.weeks);
		model.nValues(flatW, diffValues).post();
*/
		// Solving

		Solver solver = model.getSolver();
		int i = 1;
		while(solver.solve()){
			System.out.println("Solution n° "+i);
			m.printSolution(W,D,T);
			i++;
			System.out.println();
		}
/*		Solution solution = solver.findSolution();

		if(solution != null) {
			m.printSolution(W,D,T);
			//m.print(W,D,T);
			ArrayList<Integer> timeSlotsArray = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				timeSlotsArray.set(i,timeslots[i].getValue());
			}
			ArrayList<Integer> sortedCourses = new ArrayList<>();
			for (int i = 0; i< size; i++) {
				sortedCourses.set(i, timeSlotsArray.indexOf(i));
			}
			for (int i = 0; i < m.data.courses; i++) {
				for (int j = 0; j < m.data.lectures; j++) {
				//	sortedCourses.set(,i*m.data.lectures+j);
				}
			}
		}*/
	}

	// pas trop de cours en simultané
	public void printSolution(IntVar[][] W,IntVar[][] D,IntVar[][] T){
		for (int i = 0; i< data.courses; i++) {
			for (int j= 0 ;j< data.lectures;j++){
				System.out.println("Course "+i+" Lecture "+j+" Week "+W[i][j].getValue()+ " Day "+D[i][j].getValue()+" T "+T[i][j].getValue() );
			}
		}
	}

//Data(3, 4, 2, 2, 3,1,10,3,3)

}