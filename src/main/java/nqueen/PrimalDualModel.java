package nqueen;

import org.chocosolver.solver.variables.IntVar;

public class PrimalDualModel extends BaseQueenModel implements Callable {
    private IntVar[] rQueens;
    private IntVar[] cQueens;

    public PrimalDualModel(int n, boolean enumerate, boolean print) {
        super(n + "-queen problem primal/dual", n, enumerate, print);
    }

    public Stats buildAndSolve() {
        // row-based model
        rQueens = model.intVarArray("RQ", n, 0, n-1, false);
        // column-based model
        cQueens = model.intVarArray("CQ", n, 0, n-1, false);

        /* Constraints */
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                model.arithm(rQueens[i], "!=", rQueens[j]).post(); // no both queens on same column
                model.arithm(rQueens[i], "!=", rQueens[j], "-", j - i).post(); // no both queens on same major diagonal
                model.arithm(rQueens[i], "!=", rQueens[j], "+", j - i).post(); // no both queens on same minor diagonal

                model.arithm(cQueens[i], "!=", cQueens[j]).post(); // no both queens on same row
                model.arithm(cQueens[i], "!=", cQueens[j], "-", j - i).post(); // no both queens on same major diagonal
                model.arithm(cQueens[i], "!=", cQueens[j], "+", j - i).post(); // no both queens on same minor diagonal
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rQueens[i].eq(j).iff(cQueens[j].eq(i)).post(); // linking constraint between the two models
            }
        }

        /* Solving and enumerating */
        Stats stats = solve(this);

        return stats;
        /* Observations

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
