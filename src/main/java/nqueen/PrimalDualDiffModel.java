package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

public class PrimalDualDiffModel extends BaseQueenModel implements Callable, TryYourStuff {
    private IntVar[] rQueens;
    private IntVar[] cQueens;

    public PrimalDualDiffModel(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem primal/dual allDifferent", n, enumerate, print);
    }

    @Override
    public Stats ferre() {
        // row-based model
        rQueens = model.intVarArray("RQ", n, 0, n-1, false);
        IntVar[] rDiag1 = IntStream.range(0, n).mapToObj(i -> rQueens[i].sub(i).intVar()).toArray(IntVar[]::new);
        IntVar[] rDiag2 = IntStream.range(0, n).mapToObj(i -> rQueens[i].add(i).intVar()).toArray(IntVar[]::new);
        // column-based model
        cQueens = model.intVarArray("CQ", n, 0, n-1, false);
        IntVar[] cDiag1 = IntStream.range(0, n).mapToObj(i -> cQueens[i].sub(i).intVar()).toArray(IntVar[]::new);
        IntVar[] cDiag2 = IntStream.range(0, n).mapToObj(i -> cQueens[i].add(i).intVar()).toArray(IntVar[]::new);

        /* Constraints */
        model.post(
                model.allDifferent(rQueens), // no two queens in the same column
                model.allDifferent(rDiag1),
                model.allDifferent(rDiag2),
                model.allDifferent(cQueens), // no two queens int the same row
                model.allDifferent(cDiag1),
                model.allDifferent(cDiag2)
        );
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rQueens[i].eq(j).iff(cQueens[j].eq(i)).post(); // linking constraint between the two models
            }
        }

        /* Solving and enumerating */
        Stats stats = solve(this);

        return stats;
        /* Observations
            The model works and the solver returns the expected number of solutions.
            The performances, however, seem to be worse than the simple primal model by an avg factor of 1/2.
        */
    }

    @Override
    public BaseQueenModel.Stats pizzoli() {
        /* java <CLASSNAME> n printSolutionFlag printTimeFlag
         */
        int n = 8;
        boolean printSolutionFlag = true;
        boolean printTimeFlag = true;

        Model model = new Model(n + "-QP_rows");
        IntVar[] row_vars = model.intVarArray("R_Q", n, 1, n, false);
        IntVar[] col_vars = model.intVarArray("C_Q", n, 1, n, false);

        IntVar[] row_diag1 = IntStream.range(0, n).mapToObj(i -> row_vars[i].sub(i).intVar()).toArray(IntVar[]::new);
        IntVar[] row_diag2 = IntStream.range(0, n).mapToObj(i -> row_vars[i].add(i).intVar()).toArray(IntVar[]::new);

        model.allDifferent(row_vars).post();
        model.allDifferent(row_diag1).post();
        model.allDifferent(row_diag2).post();
        // It's useless define the constraints on the Dual problem: Dual problem will get constraints from the iff link

        for(int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                row_vars[j].eq(i).iff(col_vars[i].eq(j)).post();
            }
        }

        /* Solving and enumerating */
        Stats stats = solve(this);

        return stats;
    }

    public static void main(String[] args) {
        PrimalDualDiffModel m = new PrimalDualDiffModel(6, true, true);
        m.ferre();
//        m.pizzoli(args);
    }

    public void printSolutions() {
        int[][] solved_matrix = new int[n][n];
        // check solutions
        for (int i = 0; i < n; i++) {
            int rQueen = rQueens[i].getValue();
            System.out.print(rQueens[i]);
            System.out.println("  " + cQueens[rQueen]);
        }
        // print graphical solution
        for (int i = 0; i < n; i++) {
            solved_matrix[i][rQueens[i].getValue()] = 1;
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}
