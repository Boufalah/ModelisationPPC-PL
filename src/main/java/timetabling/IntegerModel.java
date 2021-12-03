package timetabling;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;


public class IntegerModel {
	Data data;
	Model model;
	Boolean printOnSolution;

	public IntegerModel(Data data,Boolean printOnSolution) {
		this.data = data;
		// Declaring model
		model = new Model("Timetabling problem");
		this.printOnSolution = printOnSolution;
	}

	public Model getModel() {
		return model;
	}

	public static void main(String[] args) {
		Data d = Instances.small1;
		IntegerModel integerModel = new IntegerModel(d,false);//verbose
		// build and solve
		integerModel.buildAndSolve();
	}

	private void buildAndSolve() {
		IntVar[][] W = new IntVar[data.courses][data.lectures];
		IntVar[][] D = new IntVar[data.courses][data.lectures];
		IntVar[][] T = new IntVar[data.courses][data.lectures];

		for(int i = 0; i < data.courses; i++) {
			for(int j = 0; j < data.lectures; j++) {
				W[i][j] = getModel().intVar("W"+i+","+j ,0, data.weeks-1, false);
				D[i][j] = getModel().intVar("D"+i+","+j, 0, data.days-1, false);
				T[i][j] = getModel().intVar("T"+i+","+j, 0, data.timesOfDay-1, false);
			}
		}


		// flat the matrix W and D and S to one array timeslots
		IntVar[] timeslots;// size = data.courses*data.lectures
		timeslots = flatMatrixWDT( W, D, T);

		// Defining constraints

		//one lecture per time slot
		OneLecturePerTimeSlotConstraint(timeslots);

		// lecture of a course are ordered
		orderLecturesConstraint(timeslots);

		// Min/ Max nb of slots (disMin) between lectures of same course
		minMaxDistanceConstraint(timeslots);

		 //Max nb of different days of the week for a course
		maxNbDaysConstraint(D);

		 //Max nb of week for a course
		maxNbWeekConstraint(W);

		 //The course should be in the same period of day
		samePeriodOfDayConstraint(T);

		 //No empty week
		noEmptyWeekConst(W);

		// Solving


		Solver solver = getModel().getSolver();
		if (printOnSolution){
			Solution solution = solver.findSolution();
			if(solution != null) {
					printSolutionPerWeek(timeslots);
				//	printSolutionPerCourse(W,D,T);
			}
			// print all solutions
//			while(solver.solve()){
//				printSolutionPerWeek(timeslots);
//			}

		}else{
			solver.findAllSolutions();
			System.out.println("Solution Count "+solver.getSolutionCount());
		}
	}


	private  void samePeriodOfDayConstraint(IntVar[][] t) {
		for (int i = 0; i < data.courses; i++) {
			getModel().allEqual(t[i]).post();
		}
	}

	private  void maxNbWeekConstraint( IntVar[][] w) {
		for (int i = 0; i < data.courses; i++) {
			getModel().atMostNValues(w[i], getModel().intVar(ConstraintParameters.maxWeeksForCourse),true).post();
		}
	}

	private void maxNbDaysConstraint(IntVar[][] d) {
		for (int i = 0; i < data.courses; i++) {
			getModel().nValues(d[i], getModel().intVar(ConstraintParameters.maxDiffDaysForACourse)).post();
		}
	}
	private  void orderLecturesConstraint( IntVar[] timeslots) {
		for(int i = 0; i < data.courses; i++) {
			for(int j = 1; j < data.lectures; j++) {
				int first = i* data.lectures+j;
				int next = i* data.lectures+(j-1);
				timeslots[first].sub(timeslots[next]).ge(1).post();
			}
		}
	}

	private  void minMaxDistanceConstraint( IntVar[] timeslots) {
		for(int i = 0; i < data.courses; i++) {
			for(int j = 1; j < data.lectures; j++) {
				int first = i* data.lectures+j;
				int next = i* data.lectures+(j-1);
				//timeslots[j]-timeslots[j-1] > minDisInSlots
				timeslots[first].sub(timeslots[next]).ge(ConstraintParameters.minDisInSlots).post();
				//timeslots[j]-timeslots[j-1] < maxDisInSlots

				timeslots[first].sub(timeslots[next]).le(ConstraintParameters.maxDisInSlots).post();
			}
		}
	}

	private  void OneLecturePerTimeSlotConstraint(IntVar[] timeslots) {
		getModel().allDifferent(timeslots).post();
	}

	private  IntVar[] flatMatrixWDT( IntVar[][] w, IntVar[][] d, IntVar[][] t) {
		IntVar[] timeslots;
		int size = data.courses* data.lectures;
		timeslots = getModel().intVarArray("T", size,0 , data.weeks*data.days* data.timesOfDay-1, false);

		for(int i = 0; i < data.courses; i++) {
			for(int j = 0; j < data.lectures; j++) {
				timeslots[i* data.lectures+j].eq(
						w[i][j].mul(data.days* data.timesOfDay).add(d[i][j].mul(data.timesOfDay)).add(t[i][j])).post();
			}
		}
		return timeslots;
	}

	private void noEmptyWeekConst(IntVar[][] w) {
		int size = data.courses*data.lectures;
		IntVar[] flatW = model.intVarArray("F", size,0 , data.weeks-1, false);

		for (int i = 0; i < data.courses; i++) {
			for (int j = 0; j < data.lectures; j++) {
				flatW[i* data.lectures+j].eq(w[i][j]).post();
			}
		}
		IntVar diffValues = model.intVar(data.weeks);
		model.nValues(flatW, diffValues).post();
	}

	public String courseAndLecture(int v){
		int j,i;
		for (i = 0; i < data.courses ; i++) {
			for (j = 0; j < data.lectures && v!=i*data.lectures+j ; j++) {
			}
			if (j < data.lectures && v==i*data.lectures+j){
				return "(C"+i+"L"+j+")";
			}
		}
		return "";
	}
	public void printSolutionPerCourse(IntVar[][] W, IntVar[][] D, IntVar[][] T){
		System.out.println("Timetabling by Courses");
		for (int i = 0; i< data.courses; i++) {
			for (int j= 0 ;j< data.lectures;j++){
				System.out.println("Course "+i+" Lecture "+j+" Week "+W[i][j].getValue()+ " Day "+D[i][j].getValue()+" T "+T[i][j].getValue() );
			}
		}
	}

	public void printSolutionPerWeek(IntVar[] timeslots){
		System.out.println("Timetabling by Week C0 = course0 L0 = Lecture 0");

		int sizeTimeSlots =data.weeks*data.days*data.timesOfDay;
		ArrayList<Integer> timeSlotsArray = new ArrayList<>();
		for (int i = 0; i < sizeTimeSlots; i++) {
			timeSlotsArray.add(-1);
		}
		int size = data.courses*data.lectures;
		for (int i = 0; i < size; i++) {
			timeSlotsArray.add(i,timeslots[i].getValue());
		}
		for (int i = 0; i< data.weeks; i++){
			System.out.println("Week"+i+"  ");
			for (int j = 0; j < data.days; j++) {
				System.out.print("\t\tDay "+j+" [");
				for (int k = 0; k < data.timesOfDay; k++) {
					System.out.print("S"+k+" ");
					int index = i * data.days *data.timesOfDay+ j * data.timesOfDay + k;
					int valeur = timeSlotsArray.indexOf(index);

					if (valeur == -1)
						System.out.print("__  |");
					else{
						System.out.print(courseAndLecture(valeur)+"  |");
					}
				}
				System.out.println("] ");
			}
			System.out.println();
		}

	}

}