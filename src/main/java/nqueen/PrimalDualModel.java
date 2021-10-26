package nqueen;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class PrimalDualModel implements TryYourStuff {
    @Override
    public long ferre(int n, boolean print) {
        Model model = new Model(n + "-queens problem primal/dual");
        // row-based model
        IntVar[] rQueens = model.intVarArray("RQ", n, 0, n-1, false);
        // column-based model
        IntVar[] cQueens = model.intVarArray("CQ", n, 0, n-1, false);

        /* Constraints */
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                model.arithm(rQueens[i], "!=", rQueens[j]).post(); // no two queens in the same column
                model.arithm(rQueens[i], "!=", rQueens[j], "-", j - i).post();
                model.arithm(rQueens[i], "!=", rQueens[j], "+", j - i).post();
                model.arithm(cQueens[i], "!=", cQueens[j]).post(); // no two queens in the same row
                model.arithm(cQueens[i], "!=", cQueens[j], "-", j - i).post();
                model.arithm(cQueens[i], "!=", cQueens[j], "+", j - i).post();
            }
        }

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

        */
    }

    public static void main(String[] args) {
        PrimalDualModel m = new PrimalDualModel();
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
