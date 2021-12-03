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
	long solutionCount ;
	Variable variables;

	public IntegerModel(Data data,Boolean printOnSolution) {
		this.data = data;
		// Declaring model
		model = new Model("Timetabling problem");
		this.printOnSolution = printOnSolution;
		variables = new Variable(data.courses,data.lectures);
	}

	public Model getModel() {
		return model;
	}

	public static void main(String[] args) {
		Data d = Instances.small1;
		IntegerModel integerModel = new IntegerModel(d,false);//verbose
		// build and solve
		integerModel.buildAndSolve();
		System.out.println("Solutions : "+integerModel.solutionCount);

	//	new BoolModel(d).buildAndSolve();
	}


	public void communConstraintWithBool(){
		for(int i = 0; i < data.courses; i++) {
			for(int j = 0; j < data.lectures; j++) {
				variables.W[i][j] = getModel().intVar("W"+i+","+j ,0, data.weeks-1, false);
				variables.D[i][j] = getModel().intVar("D"+i+","+j, 0, data.days-1, false);
				variables.T[i][j] = getModel().intVar("T"+i+","+j, 0, data.timesOfDay-1, false);
			}
		}


		// flat the matrix W and D and S to one array timeslots
		variables.timeslots = flatMatrixWDT();

		// Defining constraints

		//one lecture per time slot
		OneLecturePerTimeSlotConstraint();

		// lecture of a course are ordered
		orderLecturesConstraint();

		// Min/ Max nb of slots (disMin) between lectures of same course
		minMaxDistanceConstraint();
	}
	public void buildAndSolve() {

		communConstraintWithBool();
////////////////////////////////
		 //The course should be in the same period of day
		samePeriodOfDayConstraint();

		// Max nb of different days of the week for a course
		maxNbDaysConstraint();

		 //Max nb of week for a course
		maxNbWeekConstraint();

		 //No empty week
		noEmptyWeekConst();

		// Solving


		Solver solver = getModel().getSolver();
		if (printOnSolution){
			Solution solution = solver.findSolution();
			if(solution != null) {
					printSolutionPerWeek();
				//	printSolutionPerCourse(W,D,T);
			}
			// print all solutions
//			while(solver.solve()){
//				printSolutionPerWeek(timeslots);
//			}

		}else{
			while(solver.solve()) {
				continue;
			}
			solver.printStatistics();

			solutionCount = solver.getSolutionCount();
		}
	}

	private  void samePeriodOfDayConstraint() {
		for (int i = 0; i < data.courses; i++) {
			getModel().allEqual(variables.T[i]).post();
		}
	}

	private  void maxNbWeekConstraint() {
		for (int i = 0; i < data.courses; i++) {
			getModel().atMostNValues(variables.W[i], getModel().intVar(ConstraintParameters.maxWeeksForCourse),true).post();
		}
	}

	private void maxNbDaysConstraint() {
		for (int i = 0; i < data.courses; i++) {
			getModel().nValues(variables.D[i], getModel().intVar(ConstraintParameters.maxDiffDaysForACourse)).post();
		}
	}
	private  void orderLecturesConstraint() {
		for(int i = 0; i < data.courses; i++) {
			for(int j = 1; j < data.lectures; j++) {
				int first = i* data.lectures+j;
				int next = i* data.lectures+(j-1);
				variables.timeslots[first].sub(variables.timeslots[next]).ge(1).post();
			}
		}
	}

	private  void minMaxDistanceConstraint() {
		for(int i = 0; i < data.courses; i++) {
			for(int j = 1; j < data.lectures; j++) {
				int first = i* data.lectures+j;
				int next = i* data.lectures+(j-1);
				//timeslots[j]-timeslots[j-1] > minDisInSlots
				variables.timeslots[first].sub(variables.timeslots[next]).ge(ConstraintParameters.minDisInSlots).post();
				//timeslots[j]-timeslots[j-1] < maxDisInSlots

				variables.timeslots[first].sub(variables.timeslots[next]).le(ConstraintParameters.maxDisInSlots).post();
			}
		}
	}

	private  void OneLecturePerTimeSlotConstraint() {
		getModel().allDifferent(variables.timeslots).post();
	}

	private  IntVar[] flatMatrixWDT() {
		int size = data.courses* data.lectures;
		variables.timeslots = getModel().intVarArray("T", size,0 , data.weeks*data.days* data.timesOfDay-1, false);

		for(int i = 0; i < data.courses; i++) {
			for(int j = 0; j < data.lectures; j++) {
				variables.timeslots[i* data.lectures+j].eq(
						variables.W[i][j].mul(data.days* data.timesOfDay).add(variables.D[i][j].mul(data.timesOfDay)).add(variables.T[i][j])).post();
			}
		}
		return variables.timeslots;
	}

	private void noEmptyWeekConst() {
		int size = data.courses*data.lectures;
		IntVar[] flatW = model.intVarArray("F", size,0 , data.weeks-1, false);

		for (int i = 0; i < data.courses; i++) {
			for (int j = 0; j < data.lectures; j++) {
				flatW[i* data.lectures+j].eq(variables.W[i][j]).post();
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
	public void printSolutionPerCourse(){
		System.out.println("Timetabling by Courses");
		for (int i = 0; i< data.courses; i++) {
			for (int j= 0 ;j< data.lectures;j++){
				System.out.println("Course "+i+" Lecture "+j+" Week "+variables.W[i][j].getValue()+ " Day "+variables.D[i][j].getValue()+" T "+variables.T[i][j].getValue() );
			}
		}
	}

	public void printSolutionPerWeek(){
		System.out.println("Timetabling by Week C0 = course0 L0 = Lecture 0");

		int sizeTimeSlots =data.weeks*data.days*data.timesOfDay;
		ArrayList<Integer> timeSlotsArray = new ArrayList<>();
		for (int i = 0; i < sizeTimeSlots; i++) {
			timeSlotsArray.add(-1);
		}
		int size = data.courses*data.lectures;
		for (int i = 0; i < size; i++) {
			timeSlotsArray.add(i,variables.timeslots[i].getValue());
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