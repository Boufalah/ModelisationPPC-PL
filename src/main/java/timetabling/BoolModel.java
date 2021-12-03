package timetabling;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.BoolVar;

import java.util.Date;

import static org.chocosolver.util.tools.ArrayUtils.getColumn;

public class BoolModel {
    Data data;
    long solutionCount;

    public BoolModel(Data data) {
        this.data = data;
    }
    
    public void buildAndSolve() {
        Model model = new Model("Timetable boolean problem");
        BoolVar[][] x = model.boolVarMatrix("X", data.weeks*data.days*data.timesOfDay, data.courses);

        for (int i = 0; i < data.weeks*data.days*data.timesOfDay; i++) {
            model.addClausesAtMostOne(x[i]);  // max 1 lecture per time slot
        }

        for (int i = 0; i < data.courses; i++) {
            model.sum(getColumn(x, i), "=", data.lectures).post(); // M lectures per course
        }

        // No two lectures of the same course during a day
        for (int i = 0; i < data.weeks*data.days; i++) {
            for (int j = 0; j < data.courses; j++) {
                BoolVar[] vars = new BoolVar[data.timesOfDay];
                for (int k = 0; k < data.timesOfDay; k++) {
                    vars[k] = x[i*data.timesOfDay+k][j];
                }
                model.addClausesAtMostOne(vars);
            }
        }

//        // Min num of slots between lectures of the same course.
        int MIN_SLOTS = ConstraintParameters.minDisInSlots-1;
        for (int j = 0; j < data.courses; j++) {
            for (int i = 0; i < data.weeks*data.days*data.timesOfDay-MIN_SLOTS; i++) {
                BoolVar[] slots_per_course = new BoolVar[MIN_SLOTS+1];
                for (int k = 0; k < slots_per_course.length; k++) {
                    slots_per_course[k] = x[i+k][j];
                }
                model.addClausesAtMostOne(slots_per_course);
            }
        }

        // Max num of slots between lectures of the same course.
        int MAX_SLOTS = ConstraintParameters.maxDisInSlots;
        for (int j = 0; j < data.courses; j++) {
            for (int i = 0; i < data.weeks*data.days*data.timesOfDay-MAX_SLOTS; i++) {
                BoolVar[] slots_per_course = new BoolVar[MAX_SLOTS];
                for (int k = 0; k < slots_per_course.length; k++) {
                    slots_per_course[k] = x[i+k+1][j];
                }
                BoolVar c1 = model.sum(slots_per_course, ">=", x[i][j]).reify();

                BoolVar[] remaining_lectures = new BoolVar[data.weeks*data.days*data.timesOfDay-i-1];
                for (int k = 0; k < remaining_lectures.length; k++) {
                    remaining_lectures[k] = x[i+k+1][j];
                }
                BoolVar c2 = model.sum(remaining_lectures, "=", 0).reify();

                model.or(c1, c2).post();
            }
        }
//      // Max nb for weeks for a course
        // find the first occ of 1 and the last occ of 1 and sub the indexes to find the distance

//        int Max_Week = ConstraintParameters.maxWeeksForCourse;
//        for (int j = 0; j < data.courses; j++) {
//                BoolVar[] slots_per_course = new BoolVar[data.weeks*data.days*data.timesOfDay];
//                for (int k = 0; k < data.weeks*data.days*data.timesOfDay; k++) {
//                    slots_per_course[k] = x[k][j];
//                }
//        }



//        // Max num of different days of the week for a course
//        for (int course = 0; course < data.courses; course++) {
//            BoolVar[][] slots_per_day = new BoolVar[data.days][];
//            for (int day = 0; day < data.days; day++) {
//                for (int w = 0; w < data.weeks; w++) {
//                    for (int s = 0; s < data.timesOfDay; s++) {
//                        slots_per_day[day][w*data.timesOfDay+s] = x[w*data.days*data.timesOfDay+day+s][course];
//                    }
//                }
//            }
//            //constraint
//        }


        Solver solver = model.getSolver();
       /* for(int i=0; i<20 && solver.solve(); i++) {
            printMatrix(x, data.weeks*data.days*data.timesOfDay, data.courses);
            System.out.println();
        }*/
        solutionCount =0;
        while(solver.solve()){
            solutionCount++;
        }
        solver.printStatistics();
    }

    public static void printMatrix(BoolVar matrix[][], int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(matrix[i][j].getValue() == 1) {
                    System.out.print("XX ");
                } else {
                    System.out.print("-- ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
      //  Data d = new Data(3, 5, 2, 5, 5);
        Data d = Instances.small1;
        new BoolModel(d).buildAndSolve();
    }
}
