package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

public class PrimalDualDiffModel extends BaseQueenModel implements Callable {
    private IntVar[] rQueens;
    private IntVar[] cQueens;

    public PrimalDualDiffModel(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem primal/dual allDifferent", n, enumerate, print);
    }
    
    public Stats buildAndSolve() {
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

    public void printSolutions() {
        int[][] solved_matrix = new int[n][n];

        // print graphical solution
        for (int i = 0; i < n; i++) {
            solved_matrix[i][rQueens[i].getValue()] = 1;
        }

        Utilities.printMatrix(solved_matrix, n);
    }
}
