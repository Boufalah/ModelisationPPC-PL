package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

import java.util.stream.IntStream;

public class PrimalDualDiffModel implements TryYourStuff {
    @Override
    public long ferre(int n, boolean print) {
        Model model = new Model(n + "-queens problem primal/dual allDifferent");
        // row-based model
        IntVar[] rQueens = model.intVarArray("RQ", n, 0, n-1, false);
        IntVar[] rDiag1 = IntStream.range(0, n).mapToObj(i -> rQueens[i].sub(i).intVar()).toArray(IntVar[]::new);
        IntVar[] rDiag2 = IntStream.range(0, n).mapToObj(i -> rQueens[i].add(i).intVar()).toArray(IntVar[]::new);
        // column-based model
        IntVar[] cQueens = model.intVarArray("CQ", n, 0, n-1, false);
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
        Solver solver = model.getSolver();
        if (print) {
            for (int i = 1; solver.solve(); i++) {
                System.out.println("****** Solution n° " + i + " ******");
                printSolution(rQueens, cQueens, n);
            }
        } else {
            while(solver.solve()) {}
        }
        long estimatedTime = solver.getTimeCountInNanoSeconds();

        return estimatedTime;
        /* Observations
            The model works and the solver returns the expected number of solutions.
            The performances, however, seem to be worse than the simple primal model by an avg factor of 1/2.
        */
    }

    public static void main(String[] args) {
        PrimalDualDiffModel m = new PrimalDualDiffModel();
//        m.ferre();
    }

    public static void printSolution(IntVar[] rQueens, IntVar[] cQueens, int n) {
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
